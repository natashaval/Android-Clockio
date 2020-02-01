package com.natasha.clockio.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

open class OpenViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

class ViewHolderLoading(itemView: View) : OpenViewHolder(itemView)

const val ITEM_VIEW_TYPE_CONTENT = 1
const val ITEM_VIEW_TYPE_LOADING = 2