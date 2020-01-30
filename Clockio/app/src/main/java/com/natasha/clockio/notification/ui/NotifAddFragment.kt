package com.natasha.clockio.notification.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.natasha.clockio.R
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.ui.*
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.notification.entity.NotifRequest
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_notif_add.*
import kotlinx.android.synthetic.main.item_start_end_input.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
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
    observeNotifAdd()
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
        createNotif()
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

  private fun createNotif() {
    val title = notifTitleInput.text.toString()
    val content = notifContentInput.text.toString()
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
    val startDate = dateFormatter.parse(startDateInput.text.toString() + " " + startTimeInput.text.toString())
    val endDate = dateFormatter.parse(endDateInput.text.toString() + " " + endTimeInput.text.toString())
    val request = NotifRequest(title, content, startDate, endDate, 0.0, 0.0)
    Log.d(TAG, "create notif $request")
    viewModel.createNotif(request)

//    reattachSavedNotif()
  }

  private fun observeNotifAdd() {
    viewModel.notifAddResult.observe(this, Observer {
      when(it.status) {
        BaseResponse.Status.SUCCESS -> {
          it.data?.let {result ->
            Log.d(TAG, "create notif success $result")
            val response = result as DataResponse
            alertSuccess(activity!!,response.message)
          }
        }
        BaseResponse.Status.FAILED -> {
          it.data?.let {result->
            Log.d(TAG, "create notif failed $result")
            val response = result as DataResponse
            alertFailed(activity!!,response.message)
          }
        }
        BaseResponse.Status.ERROR -> {
          Log.d(TAG, "create notif error ${it.data}")
          alertError(activity!!, it.data.toString())
        }
      }
    })
  }

  private fun reattachSavedNotif() {
    val i: NotifFragment.OnNotifReattachListener = activity as NotifFragment.OnNotifReattachListener
    i.onNotifReattach()
  }
}
