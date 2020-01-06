package com.natasha.clockio.notification.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.natasha.clockio.R
import com.natasha.clockio.base.ui.DatePickerFragment
import com.natasha.clockio.base.ui.TimePickerFragment
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.item_start_end_input.*
import javax.inject.Inject

class NotifAddFragment : Fragment() {

  companion object {
    fun newInstance() = NotifAddFragment()
    private val TAG: String = NotifAddFragment::class.java.simpleName
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: NotifViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.notif_create_title)
    return inflater.inflate(R.layout.fragment_notif_add, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(NotifViewModel::class.java)
    setHasOptionsMenu(true)
    setDate()
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
        Log.d(TAG, "notif saved clicked!")
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setDate() {
    startDateInput.setOnClickListener {
      showDateDialogFragment(startDateInput)
    }

    endDateInput.setOnClickListener {
      showDateDialogFragment(endDateInput)
    }

    startTimeInput.setOnClickListener {
      showTimeDialogFragment(startTimeInput)
    }

    endTimeInput.setOnClickListener {
      showTimeDialogFragment(endTimeInput)
    }
  }

  private fun showDateDialogFragment(editText: EditText) {
    val ft = fragmentManager!!.beginTransaction()
    val dateFragment = DatePickerFragment(editText)
    dateFragment.show(ft, "DatePicker")
  }

  private fun showTimeDialogFragment(editText: EditText) {
    val ft = fragmentManager!!.beginTransaction()
    val timeFragment = TimePickerFragment(editText)
    timeFragment.show(ft, "TimePicker")
  }
}
