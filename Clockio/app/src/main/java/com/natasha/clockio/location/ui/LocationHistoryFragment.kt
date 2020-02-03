package com.natasha.clockio.location.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.ui.customResolveOrThrow
import com.natasha.clockio.base.util.observeOnce
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.location.LocationViewModel
import com.natasha.clockio.location.entity.LocationModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_location_history.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LocationHistoryFragment : Fragment() {
  private var employeeId: String? = null
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: LocationViewModel

  private var selectedDate: androidx.core.util.Pair<Long, Long> = androidx.core.util.Pair(0,0)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      employeeId = it.getString(PreferenceConst.EMPLOYEE_ID_KEY)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.action_location_history)
    return inflater.inflate(R.layout.fragment_location_history, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(LocationViewModel::class.java)

    setHistoryDate()
    observeLocationHistory()
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

  private fun setHistoryDate() {
    locationHistoryDateInput.setOnClickListener {
      val builder = MaterialDatePicker.Builder.dateRangePicker()
      builder.apply {
        setTheme(customResolveOrThrow(context!!, R.attr.materialCalendarTheme))
        build()
      }
      val picker = builder.build()
      try {
        addCalendarListener(picker)
        picker.show(activity?.supportFragmentManager!!, picker.toString())
      } catch (e: IllegalArgumentException) {
        Log.e(TAG, "calendar error ${e.message}")
        e.printStackTrace()
      }
    }
  }

  private fun addCalendarListener(picker: MaterialDatePicker<*>) {
    picker.addOnPositiveButtonClickListener { selection ->
      selectedDate = selection as androidx.core.util.Pair<Long, Long>
      Log.d(TAG, "Date String = ${picker.headerText}:: Date epoch values:: ${selectedDate.first} to ${selectedDate.second}")
      //      https://stackoverflow.com/questions/6850874/how-to-create-a-java-date-object-of-midnight-today-and-midnight-tomorrow
      locationHistoryDateInput.setText(picker.headerText)
      val formatter = SimpleDateFormat("yyyy-MM-dd")
      val startDate = formatter.format(selectedDate.first)
      val endDate = formatter.format(selectedDate.second)
      viewModel.getLocationHistory(employeeId!!, startDate, endDate)
    }
    picker.addOnNegativeButtonClickListener { _ ->
      Log.d(TAG, "calendar history negative")
    }
    picker.addOnCancelListener { _ ->
      Log.d(TAG, "calendar history cancel")
    }
  }

  private fun observeLocationHistory() {
    viewModel.locationHistory.observeOnce(this, androidx.lifecycle.Observer {
      setMap(it)
    })
  }

  private fun setMap(locationList: ArrayList<LocationModel>) {
    val frag = MapsFragment.newInstance()
    val bundle = Bundle().apply {
      putParcelableArrayList(ParcelableConst.LOCATION_HISTORY, locationList)
      putBoolean(ParcelableConst.LOCATION_SAVE, false)
    }
    frag.arguments = bundle
    fragmentManager?.beginTransaction()
        ?.replace(R.id.locationHistoryMap, frag, TAG)
        ?.commit()
  }

  companion object {
    private val TAG: String = LocationHistoryFragment::class.java.simpleName
    @JvmStatic fun newInstance(employeeId: String) = LocationHistoryFragment().apply {
      arguments = Bundle().apply {
        putString(PreferenceConst.EMPLOYEE_ID_KEY, employeeId)
      }
    }
  }
}
