package com.natasha.clockio.notification.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.natasha.clockio.R
import com.natasha.clockio.activity.ui.ActivityDetailsFragment
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.notification.entity.Notif
import kotlinx.android.synthetic.main.fragment_notif_details.*
import kotlinx.android.synthetic.main.item_start_end_time.*

class NotifDetailsFragment : Fragment() {

  private var notif: Notif? = null

  companion object {
    fun newInstance(param1: Notif) =
        NotifDetailsFragment().apply {
          arguments = Bundle().apply {
            putParcelable(ParcelableConst.ITEM_NOTIF, param1)
          }
        }
    private val TAG: String = NotifDetailsFragment::class.java.simpleName
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      notif = it.getParcelable(ParcelableConst.ITEM_NOTIF)
      Log.d(TAG, "notif details received $notif")
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_notif_details, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    notif?.let {
      setDetail(it)
    }
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

  private fun setDetail(notif: Notif) {
    notifTitleDetails.text = notif.title
    notifContentDetails.text = notif.content
    startDetails.text = notif.startDate.toString()
    endDetails.text = notif.endDate.toString()
    notifLocationDetails.text = notif.location
    notifPostTimeDetails.text = resources.getString(R.string.notif_post_time, notif.updatedAt, notif.updatedBy)
  }

}