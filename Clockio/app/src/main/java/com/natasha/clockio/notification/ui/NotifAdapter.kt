package com.natasha.clockio.notification.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.notification.entity.Notif

class NotifAdapter: PagedListAdapter<Notif, RecyclerView.ViewHolder>(REPO_COMPARATOR) {
  private var listener: OnNotifClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return NotifViewHolder.create(parent, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val notifItem = getItem(position)
    if (notifItem != null) {
      (holder as NotifViewHolder).bind(notifItem)
    }
  }

  fun removeItem(position: Int) {
    notifyItemRemoved(position)
  }

  fun getNotif(position: Int) = getItem(position)

  fun setListener(listener: OnNotifClickListener) {
    this.listener = listener
  }

  interface OnNotifClickListener {
    fun onNotifClick(notif: Notif)
  }

  companion object {
    private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Notif>() {
      override fun areItemsTheSame(oldItem: Notif, newItem: Notif): Boolean =
          oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: Notif, newItem: Notif): Boolean =
          oldItem == newItem

    }
  }
}