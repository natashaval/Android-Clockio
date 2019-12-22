package com.natasha.clockio.activity.ui

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.natasha.clockio.MapsFragment

import com.natasha.clockio.R
import com.natasha.clockio.activity.entity.ActivityCreateRequest
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.ui.alertError
import com.natasha.clockio.base.ui.alertFailed
import com.natasha.clockio.base.ui.alertSuccess
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.home.viewmodel.ActivityViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_activity_add.*
import java.util.*
import javax.inject.Inject

class ActivityAddFragment : Fragment() {

  companion object {
    fun newInstance() = ActivityAddFragment()
    val TAG: String = ActivityAddFragment::class.java.simpleName
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var activityViewModel: ActivityViewModel
  private var tp: TimePickerDialog? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.activity_create_title)
    return inflater.inflate(R.layout.fragment_activity_add, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    activityViewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)
    setHasOptionsMenu(true)

    setMap()
    setTime()
    observeCreateActivityResult()
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onStart() {
    super.onStart()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onOpen()
  }

  override fun onStop() {
    super.onStop()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onClose()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.save, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.action_save -> {
        Log.d(TAG, "activity saved button clicked!")
        createActivity()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setTime() {
    activityStartInput.setOnClickListener {
      Log.d(TAG, "activity start time clicked!")
      if (tp == null) {
        val ft = fragmentManager!!.beginTransaction()
        val startTime: DialogFragment = TimePickerFragment(
          activityStartInput)
        startTime.show(ft, "TimePicker")
      }
    }

    activityEndInput.setOnClickListener {
      Log.d(TAG, "activity end time clicked!")
      if (tp == null) {
        val ft = fragmentManager!!.beginTransaction()
        val endTime: DialogFragment = TimePickerFragment(
          activityEndInput)
        endTime.show(ft, "TimePicker")
      }
    }
  }

  private fun createActivity() {
    var employeeId = sharedPref.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
    val title = activityTitleInput.text.toString()
    val content = activityContentInput.text.toString()
    val startTime = activityStartInput.text.toString()
    val endTime = activityEndInput.text.toString()
    val request = ActivityCreateRequest(title, content, Date(), startTime, endTime, 0.0, 0.0)
    activityViewModel.createActivity(employeeId!!, request)
  }

  private fun observeCreateActivityResult() {
    activityViewModel.activityResult.observe(this, androidx.lifecycle.Observer {
      when(it.status) {
        BaseResponse.Status.SUCCESS -> {
          it.data?.let {result ->
            Log.d(TAG, "create activity success $result")
            val response = result as DataResponse
            alertSuccess(activity!!,response.message)
          }
        }
        BaseResponse.Status.FAILED -> {
          it.data?.let {result->
            Log.d(TAG, "create activity failed $result")
            val response = result as DataResponse
            alertFailed(activity!!,response.message)
          }
        }
        BaseResponse.Status.ERROR -> {
          Log.d(TAG, "create activity error ${it.data}")
          alertError(activity!!, it.data.toString())
        }
      }
    })
  }

  private fun setMap() {
    Log.d(TAG, "addFragment setMap")
    fragmentManager?.beginTransaction()
      ?.replace(R.id.activityMapInput, MapsFragment.newInstance(), MapsFragment.TAG)
      ?.commit()
  }
}
