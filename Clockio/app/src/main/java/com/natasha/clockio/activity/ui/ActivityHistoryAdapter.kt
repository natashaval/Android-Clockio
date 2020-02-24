package com.natasha.clockio.activity.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.ui.adapter.ActivityAdapter
import com.natasha.clockio.home.ui.adapter.ITEM_VIEW_TYPE_CONTENT
import com.natasha.clockio.home.ui.adapter.ITEM_VIEW_TYPE_LOADING
import com.natasha.clockio.home.ui.adapter.OpenViewHolder
import kotlinx.android.synthetic.main.item_activity.view.*

class ActivityHistoryAdapter constructor(var activities: MutableList<Activity>):
    RecyclerView.Adapter<OpenViewHolder>() {
  companion object {
    private val TAG:String = ActivityHistoryAdapter::class.java.simpleName
  }

  private var isLoaderVisible: Boolean = false
  private var listener: ActivityAdapter.OnActivityClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenViewHolder {
    val layoutInflater = LayoutInflater.from(parent?.context)
    Log.d(TAG, "layout inflater viewType $viewType")
    return when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_activity, parent, false)
        HistoryHolder(inflatedView)
      }
      else -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_loading, parent, false)
        ViewHolderLoading(inflatedView)
      }
    }
  }

  override fun getItemCount(): Int {
    Log.d(TAG, "history getItemCount ${activities.size}")
    return activities.size
  }

  override fun getItemViewType(position: Int): Int {
    Log.d(TAG, "getItemViewType history size ${activities.size}")
    return if (position == activities.size - 1 && isLoaderVisible) {
      ITEM_VIEW_TYPE_LOADING
    } else {
      ITEM_VIEW_TYPE_CONTENT
    }

  }

  override fun onBindViewHolder(holder: OpenViewHolder, position: Int) {
    val viewType = getItemViewType(position)
    val item = activities[position]
    when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        Log.d(TAG, "onbindviewHolder history CONTENT")
        val historyHolder = holder as HistoryHolder
        historyHolder.bind(item)
      }
      else -> {
        Log.d(TAG, "onbindviewholder history LOADING")
      }
    }
  }

  fun addItem(act: Activity) {
    activities.add(act)
    notifyItemInserted(activities.size - 1)
  }

  fun removeItem(act: Activity) {
    val position = activities.indexOf(act)
    val item = activities[position]
    if (item != null) {
      activities.remove(item)
      notifyItemRemoved(position)
    }
  }

  fun addAll(newActivities: List<Activity>) {
    Log.d(TAG, "history add All $newActivities")
    activities.addAll(newActivities)
    Log.d(TAG, "history add All size ${activities.size}")
    notifyItemRangeInserted(0, newActivities.size)
  }

  fun addLoading() {
    Log.d(TAG, "adapter history addLoading")
    isLoaderVisible = true
    addItem(Activity())
  }

  fun removeLoading() {
    Log.d(TAG, "adapter history removeLoading size ${activities.size}")
    isLoaderVisible = false
    val position = activities.size - 1
//    if (!isLastPage){
      val item = activities[position]
      if (item != null) {
        activities.remove(item)
        notifyItemRemoved(position)
      }
//    }
    notifyDataSetChanged()
  }

  fun clear() {
    isLoaderVisible = false
    val size = activities.size
    activities.clear()
    notifyItemRangeRemoved(0, size)
  }

  inner class HistoryHolder(private val v: View): OpenViewHolder(v){
    private val TAG:String = HistoryHolder::class.java.simpleName
    fun bind(activity: Activity) {
      Log.d(TAG, "history bind $activity")
      v.activityTitle.text = activity.title
      v.activityContent.text = activity.content
      v.activityDate.text = activity.date.toString()
      v.setOnClickListener {
        Log.d(TAG, "activity clicked $activity")
        listener?.onActivityClick(activity)
      }
    }
  }

  inner class ViewHolderLoading(itemView: View) : OpenViewHolder(itemView)

  fun setListener(listener: ActivityAdapter.OnActivityClickListener) {
    this.listener = listener
  }
}