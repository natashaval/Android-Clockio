package com.natasha.clockio.notification.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.notification.entity.Notif
import kotlinx.android.synthetic.main.item_notif.view.*

class NotifViewHolder(context: Context, itemView: View, mListener: NotifAdapter.OnNotifClickListener?) : RecyclerView.ViewHolder(itemView) {
  private val context = context
  private val v = itemView
  private val listener = mListener
  private val TAG: String = NotifViewHolder::class.java.simpleName

  fun bind(notif: Notif?) {
    if (notif != null) {
      v.notifTitle.text = notif.title
      v.notifContent.text = notif.content
      /*if (!notif.isOpen) {
        val colorLight = ContextCompat.getColor(context, R.color.colorPrimaryLight)
        v.notifCardView.setBackgroundColor(colorLight)
      } else {
        val colorTransparent = ContextCompat.getColor(context, R.color.float_transparent)
        v.notifCardView.setBackgroundColor(colorTransparent)
      }*/
      v.setOnClickListener {
        Log.d(TAG, "notif clicked")
//        openFragment(notif)
        listener?.onNotifClick(notif)
      }
    }
  }

  private fun openFragment(notif: Notif) {
    val mFragment = NotifDetailsFragment.newInstance(notif)
    context?.let {
      val ctx = it as HomeActivity
      ctx.addFragmentBackstack(mFragment)
    }
  }

  companion object {
    fun create(parent: ViewGroup, listener: NotifAdapter.OnNotifClickListener?): NotifViewHolder {
      val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.item_notif, parent, false)
      return NotifViewHolder(parent.context, view, listener)
    }
  }
}