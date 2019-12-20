package com.natasha.clockio.activity.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.natasha.clockio.R
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import kotlinx.android.synthetic.main.fragment_activity_add.*

class ActivityAddFragment : Fragment() {

  companion object {
    fun newInstance() = ActivityAddFragment()
    val TAG: String = ActivityAddFragment::class.java.simpleName
  }

  private var tp: TimePickerDialog? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.activity_create_title)
    return inflater.inflate(R.layout.fragment_activity_add, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    setTime()
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
    if (tp?.isShowing!!) tp?.dismiss()
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

}
