package com.natasha.clockio.activity.ui

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

// https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
//https://stackoverflow.com/questions/33149101/how-to-add-timepicker-using-fragment
class TimePickerFragment constructor(editText: EditText): DialogFragment(), TimePickerDialog.OnTimeSetListener {
  private val TAG:String = TimePickerFragment::class.java.simpleName
  private val myCalendar: Calendar = Calendar.getInstance()
  private val editText = editText

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    Log.d(TAG, "calendar on focus change")
    val hour = myCalendar.get(Calendar.HOUR_OF_DAY)
    val minute = myCalendar.get(Calendar.MINUTE)
    return TimePickerDialog(activity, this, hour, minute, true)
  }

  override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
    Log.d(TAG, "calendar timepicker $hourOfDay:$minute")
    this.editText.setText("$hourOfDay:$minute")
  }
}