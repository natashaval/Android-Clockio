package com.natasha.clockio.home.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import com.natasha.clockio.R
import com.natasha.clockio.home.ui.viewmodel.ProfileViewModel
import com.natasha.clockio.location.GpsUtils
import com.natasha.clockio.location.LocationViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.item_location.*

class ProfileFragment : Fragment() {

  companion object {
    fun newInstance() = ProfileFragment()
  }

  //    @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var profileViewModel: ProfileViewModel
  private lateinit var locationViewModel: LocationViewModel

  private val TAG:String = ProfileFragment::class.java.simpleName
  private var isGps: Boolean = false

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.profile_fragment, container, false)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
    // TODO: Use the ViewModel
    GpsUtils(context!!).turnOnGps(object : GpsUtils.OnGpsListener {
      override fun gpsStatus(isGpsEnable: Boolean) {
        isGps = isGpsEnable
      }
    })
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

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101
