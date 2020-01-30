package com.natasha.clockio.activity.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker

import com.natasha.clockio.R
import com.natasha.clockio.activity.viewmodel.ActivityViewModel
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_activity_history.*
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject

class ActivityHistoryFragment : Fragment() {

  companion object {
    fun newInstance() = ActivityHistoryFragment()
    private val TAG: String = ActivityHistoryFragment::class.java.simpleName
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: ActivityViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_activity_history, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)

    setHistoryDate()
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
    activityHistoryDateInput.setOnClickListener {
      val builder = MaterialDatePicker.Builder.dateRangePicker()
      builder.apply {
        setTheme(resolveOrThrow(context!!, R.attr.materialCalendarTheme))
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

  private fun resolveOrThrow(context: Context, @AttrRes attributeResId: Int): Int {
    val typedValue = TypedValue()
    if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
      return typedValue.data
    }
    throw IllegalArgumentException(context.resources.getResourceName(attributeResId))
  }

  private fun addCalendarListener(picker: MaterialDatePicker<*>) {
    picker.addOnPositiveButtonClickListener { selection ->
      val time= selection as androidx.core.util.Pair<Long, Long>
      Log.d(TAG, "Date String = ${picker.headerText}:: Date epoch values:: ${time.first} to ${time.second}")
      val startDate = Date(time.first!!)
      val endDate = Date(time.second!!)
      Log.d(TAG, "Real Date start: $startDate end: $endDate")
      activityHistoryDateInput.setText(picker.headerText)
    }
    picker.addOnNegativeButtonClickListener { dialog ->
      Log.d(TAG, "calendar history negative")
    }
    picker.addOnCancelListener { dialog ->
      Log.d(TAG, "calendar history cancel")
    }
  }

}
