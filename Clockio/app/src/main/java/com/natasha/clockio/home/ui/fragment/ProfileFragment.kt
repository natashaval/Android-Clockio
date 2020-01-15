package com.natasha.clockio.home.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.util.observeOnce
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.viewmodel.ProfileViewModel
import com.natasha.clockio.location.GpsUtils
import com.natasha.clockio.location.LocationViewModel
import com.natasha.clockio.location.entity.Location
import com.natasha.clockio.location.worker.LocationWorker
import com.natasha.clockio.login.ui.LoginActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_location.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileFragment : Fragment() {

  companion object {
    fun newInstance() = ProfileFragment()
    private val TAG:String = ProfileFragment::class.java.simpleName
    const val LOCATION_REQUEST = 100
    const val GPS_REQUEST = 101
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var profileViewModel: ProfileViewModel
  private lateinit var locationViewModel: LocationViewModel

  private var isGps: Boolean = false
  var employeeId: String? = null
  private val workManager = WorkManager.getInstance(context!!)

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.navigation_profile)
    return inflater.inflate(R.layout.fragment_profile, container, false)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    profileViewModel = ViewModelProvider(this, factory).get(
        ProfileViewModel::class.java)
    locationViewModel = ViewModelProvider(this, factory).get(LocationViewModel::class.java)
    setHasOptionsMenu(true)
    GpsUtils(context!!).turnOnGps(object : GpsUtils.OnGpsListener {
      override fun gpsStatus(isGpsEnable: Boolean) {
        isGps = isGpsEnable
      }
    })

    getEmployee()
    logout()
    locationClick()
  }

  override fun onStart() {
    super.onStart()
    Log.d(TAG, "onStart loc")
    invokeLocationAction()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      Log.d(TAG, "onActivityResult requestCode $requestCode")
      if (requestCode == GPS_REQUEST) {
        isGps = true
        invokeLocationAction()
      }
    }
  }

  override fun onResume() {
    super.onResume()
    invokeLocationAction()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.settings, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.action_settings -> {
        Log.d(TAG, "Settings icon clicked!")
        fragmentManager?.
            beginTransaction()?.
            replace(R.id.content, SettingsFragment())?.
            addToBackStack(null)?.
            commit()
        return true
      }
    }
    return false
  }

  private fun getEmployee() {
    employeeId = sharedPref.getString(PreferenceConst.EMPLOYEE_ID_KEY, null);
    Log.d(TAG, "sharedPref id $employeeId")
    profileViewModel.setId(employeeId)

    profileViewModel.employee.observe(this, Observer {response ->
      Log.d(TAG, "Employee get $response")
      response.data?.let {
        Log.d(TAG, "Employee set fragment")
        var employee = response.data
        profileNameTextView.text = employee.firstName + " " + employee.lastName
        profileDepartmentTextView.text = employee.department.name
        Glide.with(this).load(employee.profileUrl)
            .apply(RequestOptions.circleCropTransform()).into(profileImageView)
      }
    })
  }

  private fun logout() {
    logout.setOnClickListener {
      sharedPref.edit().clear().apply()
      activity?.finish()
      val intent = Intent(activity, LoginActivity::class.java)
      startActivity(intent)
    }
  }

  private fun invokeLocationAction() {
    Log.d(TAG, "permissionGranted = ${isPermissionGranted()} && permissionRationale = ${shouldShowPermissionRationale()}")
    when {
      !isGps -> {
        Log.d(TAG, "isGPS enabled")
        latLong.text = getString(R.string.enable_gps)
      }

      isPermissionGranted() -> startLocationUpdate()
      shouldShowPermissionRationale() -> latLong.text = getString(R.string.location_permission)

      else -> {
        Log.d(TAG, "requestPermission")
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST
        )
      }
    }
  }

  private fun startLocationUpdate() {
    Log.d(TAG, "locationUpdate Happened")
    locationViewModel.getLocationData().observe(this, Observer {
      latLong.text = getString(R.string.latLong, it.latitude, it.longitude)
    })
  }

  private fun observeOnceLocation() {
    locationViewModel.getLocationData().observeOnce(this, Observer {
      val loc = Location(employeeId!!, it.latitude, it.longitude)
//      Log.d(TAG, "worker observe once ($latitude, $longitude)")
      Log.d(TAG, "worker observe once $loc")
      locationWorker(loc)
    })
  }

  private fun locationClick() {
    locationWorkerButton.setOnClickListener {
      Log.d(TAG, "send location button clicked!")
      observeOnceLocation()
//      workManager.enqueueUniqueWork(LocationWorker.LOCATION_WORKER_TAG, ExistingWorkPolicy.REPLACE, simpleWorkRequest)
    }
    locationWorkerCancelButton.setOnClickListener {
      Log.d(TAG, "worker cancel button clicked!")
      workManager.cancelUniqueWork(LocationWorker.LOCATION_WORKER_TAG)
    }
    workManager.getWorkInfosForUniqueWorkLiveData(LocationWorker.LOCATION_WORKER_TAG).observe(this, Observer {workInfos ->
      for (info in workInfos) {
        Log.d(TAG, "worker observe ${info.state}")
      }
    })
  }

  private fun locationWorker(loc: Location) {
//    val simpleWorkRequest = LocationWorker.buildOneTimeRequest(loc)
//    Log.d(TAG, "worker inputData before emp $employeeId ($latitude, $longitude)")
    Log.d(TAG, "worker inputData before location $loc")
    val inputData = Data.Builder()
        .putString(LocationWorker.LOCATION_WORKER_EMP, employeeId)
        .putDouble(LocationWorker.LOCATION_WORKER_LATITUDE, loc.latitude)
        .putDouble(LocationWorker.LOCATION_WORKER_LONGITUDE, loc.longitude)
        .build()

    Log.d(TAG, "worker inputData emp ${inputData.getString(LocationWorker.LOCATION_WORKER_EMP)} latitude ${inputData.getDouble(LocationWorker.LOCATION_WORKER_LATITUDE, 0.0)} longitude ${inputData.getDouble(LocationWorker.LOCATION_WORKER_LONGITUDE, 0.0)}")

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val simpleWorkRequest = OneTimeWorkRequest.Builder(LocationWorker::class.java)
        .addTag(LocationWorker.LOCATION_WORKER_TAG)
        .setConstraints(constraints)
        .setInputData(inputData)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
        .build()

    val workerId = simpleWorkRequest.id

    workManager.getWorkInfoByIdLiveData(workerId).observeOnce(this, Observer { workInfo ->
      workInfo?.let {
        Log.d("FragmentWork", "Locationworker ${it.state}")
      }
    })
  }

  private fun isPermissionGranted() =
      ContextCompat.checkSelfPermission(context!!,
          Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
      ContextCompat.checkSelfPermission(context!!,
          Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

  private fun shouldShowPermissionRationale() =
      shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
      shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    Log.d(TAG, "onRequestPermissionResult $requestCode")
    when(requestCode) {
      LOCATION_REQUEST -> {
        isGps = true
        invokeLocationAction()
      }
    }
  }
}
