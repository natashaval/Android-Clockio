package com.natasha.clockio.home.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.ui.adapter.ActivityAdapter
import com.natasha.clockio.home.viewmodel.ActivityViewModel
import com.natasha.clockio.home.viewmodel.EmployeeViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_activity.view.*
import kotlinx.android.synthetic.main.item_profile.*
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat
import javax.inject.Inject

class ActivityFragment : Fragment() {

  companion object {
    fun newInstance() = ActivityFragment()
  }

  private val TAG: String = ActivityFragment::class.java.simpleName
  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var employeeViewModel: EmployeeViewModel
  private lateinit var activityViewModel: ActivityViewModel
  private lateinit var statusArray: Array<String>
  private lateinit var statusIconArray: Array<Int>
  private var employeeId: String? = ""
  private var employeeStatus: String? = null
  private lateinit var adapter: StatusSpinnerAdapter

  private lateinit var activityAdapter: ActivityAdapter
  private lateinit var linearLayoutManager: LinearLayoutManager

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    statusArray = activity!!.resources.getStringArray(R.array.status_array)
    statusIconArray = arrayOf(
        R.drawable.ic_status_online_24dp, R.drawable.ic_status_meeting_24dp,
        R.drawable.ic_status_away_24dp, R.drawable.ic_status_offline_24dp)
    activity!!.actionBar?.setTitle(R.string.navigation_activity)

    return inflater.inflate(R.layout.fragment_activity, container, false)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    employeeViewModel = ViewModelProvider(this, factory).get(EmployeeViewModel::class.java)
    activityViewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)

    getEmployee()
    getStatus()
    getActivityToday()

    observeEmployee()
    observeActivity()
  }

  private fun getStatus() {
    adapter = StatusSpinnerAdapter(context!!, R.layout.item_status, statusArray, statusIconArray)
    statusSpinner.adapter = adapter
    selectStatus()
    statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(p0: AdapterView<*>?) {
      }

      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val status: String = statusSpinner.selectedItem.toString()
//        Toast.makeText(activity!!, "Status selected: $status", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onItemSelected tapi var employeeStatus $employeeStatus dibanding ${status.toLowerCase()}")
        if (employeeStatus!= null && !employeeStatus.equals(status.toLowerCase())) {
          Log.d(TAG, "status changed! $status")
          employeeViewModel.updateStatus(employeeId!!, status)
        }
      }

    }
  }

  private fun getEmployee() {
    employeeId = sharedPref.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
    employeeViewModel.getEmployee(employeeId!!)
  }

  private fun getActivityToday() {
    activityViewModel.getActivityToday(employeeId!!)
  }

  private fun observeEmployee() {
    employeeViewModel.employee.observe(this, Observer {
      when(it.status) {
        BaseResponse.Status.LOADING -> {
          statusProgressBar.visibility = View.VISIBLE
        }
        BaseResponse.Status.SUCCESS -> {
          statusProgressBar.visibility = View.INVISIBLE
          val response = it.data as Employee
          employeeName.text = response.firstName + " " + response.lastName
          employeeDept.text = response.department.name
          val dateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
          employeeLastCheckin.text = getString(R.string.employee_last_checkin, dateFormat.format(response.checkIn))
          response.status?.let {
            employeeStatus = it.toLowerCase()
            // https://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position:
            Log.d(TAG, "BaseResponse status $employeeStatus")
            selectStatus()
          }
          Glide.with(this).load(response.profileUrl)
              .apply(RequestOptions.circleCropTransform()).into(employeeProfile)
        }
        BaseResponse.Status.ERROR, BaseResponse.Status.FAILED -> {
          statusProgressBar.visibility = View.INVISIBLE
        }
      }
    })
  }

  private fun observeActivity() {
    activityViewModel.activityToday.observe(this, Observer {
      when(it.status) {
        BaseResponse.Status.SUCCESS -> {
          var data = it.data as List<Activity>
          Log.d(TAG, "activity $data")
          showActivity(data)
        }
      }
    })
  }

  private fun selectStatus() {
    val statusPosition: Int = adapter.getPosition(StringUtils.capitalize(employeeStatus))
    statusSpinner.setSelection(statusPosition)
  }

  private fun showActivity(activities: List<Activity>) {
    Log.d(TAG, "show Activity $activities")
    activityAdapter = ActivityAdapter(activities)
    linearLayoutManager = LinearLayoutManager(activity)
    activityRecyclerView.apply {
      layoutManager = linearLayoutManager
      adapter = activityAdapter
    }
  }

}
