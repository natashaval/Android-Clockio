package com.natasha.clockio.notification.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.notification.entity.Notif
import kotlinx.android.synthetic.main.item_notif.view.*

class NotifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val v = itemView

    fun bind(notif: Notif?) {
        if (notif != null) {
            v.notifTitle.text = notif.title
            v.notifContent.text = notif.content
        }
    }

    companion object {
        fun create(parent: ViewGroup): NotifViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notif, parent, false)
            return NotifViewHolder(view)
        }
    }
}