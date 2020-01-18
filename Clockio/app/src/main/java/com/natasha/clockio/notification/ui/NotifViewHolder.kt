package com.natasha.clockio.notification.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.notification.entity.Notif
import kotlinx.android.synthetic.main.item_notif.view.*

class NotifViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val context = context
    private val v = itemView
    private val TAG: String = NotifViewHolder::class.java.simpleName

    init {
      v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        Log.d(TAG, "notif click $p0")
    }

    fun bind(notif: Notif?) {
        if (notif != null) {
            v.notifTitle.text = notif.title
            v.notifContent.text = notif.content
            v.setOnClickListener {
                Log.d(TAG, "notif clicked")
                openFragment(notif)
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
        fun create(parent: ViewGroup): NotifViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notif, parent, false)
            return NotifViewHolder(parent.context, view)
        }
    }
}