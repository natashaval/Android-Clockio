package com.natasha.clockio.home.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.natasha.clockio.R
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.viewmodel.ActivityViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_profile.*
import javax.inject.Inject

class ActivityFragment : Fragment() {

  companion object {
    fun newInstance() = ActivityFragment()
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: ActivityViewModel
  private lateinit var statusArray: Array<String>
  private lateinit var statusIconArray: Array<Int>

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    statusArray = activity!!.resources.getStringArray(R.array.status_array)
    statusIconArray = arrayOf(R.drawable.ic_status_online_24dp, R.drawable.ic_status_away_24dp, R.drawable.ic_status_offline_24dp)
    return inflater.inflate(R.layout.fragment_activity, container, false)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)

    getStatus()
    getEmployee()
    observeEmployee()
  }

//  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//    viewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)
//  }

  private fun getStatus() {
    val adapter = StatusSpinnerAdapter(context!!, R.layout.item_status, statusArray, statusIconArray)
    statusSpinner.adapter = adapter
  }

  private fun getEmployee() {
    val employeeId = sharedPref.getString(getString(R.string.employee_id_key), "")
    viewModel.getEmployee(employeeId!!)
  }

  private fun observeEmployee() {
    viewModel.employee.observe(this, Observer {
      when(it.status) {
        BaseResponse.Status.LOADING -> {
          statusProgressBar.visibility = View.VISIBLE
        }
        BaseResponse.Status.SUCCESS -> {
          statusProgressBar.visibility = View.INVISIBLE
          val response = it.data as Employee
          employeeName.text = response.firstName + " " + response.lastName
          employeeDept.text = response.department.name
          employeeLastCheckin.text = getString(R.string.employee_last_checkin, response.checkIn)
          Glide.with(this).load(response.profileUrl)
              .apply(RequestOptions.circleCropTransform()).into(employeeProfile)
        }
        BaseResponse.Status.ERROR, BaseResponse.Status.FAILED -> {
          statusProgressBar.visibility = View.INVISIBLE
        }
      }
    })
  }

}
