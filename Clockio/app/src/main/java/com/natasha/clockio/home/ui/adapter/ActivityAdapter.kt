package com.natasha.clockio.home.ui.adapter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.natasha.clockio.MainActivity
import com.natasha.clockio.R
import com.natasha.clockio.activity.ui.ActivityDetailsFragment
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.ui.HomeActivity
import kotlinx.android.synthetic.main.item_activity.view.*

//https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
class ActivityAdapter constructor(private val activities: MutableList<Activity>):
  RecyclerView.Adapter<ActivityAdapter.ActivityHolder>() {

  private val TAG: String = ActivityAdapter::class.java.simpleName
  private var listener: OnActivityClickListener? = null

  override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
    val itemActivity = activities[position]
    holder.bind(itemActivity)
  }

  override fun onCreateViewHolder(parent: ViewGroup,
                                  viewType: Int): ActivityHolder {
    val inflatedView = parent.inflate(R.layout.item_activity, false)
    return ActivityHolder(inflatedView)
  }

  fun addAll(newActivities: List<Activity>) {
    activities.clear() //clear previous data
    Log.d(TAG, "activity adapter add All ${newActivities.size}")
    activities.addAll(newActivities)
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = activities.size

  inner class ActivityHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private val TAG: String = ActivityHolder::class.java.simpleName
    private var view: View = v

    init {
      v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      Log.d(TAG, "click $p0")
    }

    fun bind(activity: Activity) {
      view.activityTitle.text = activity.title
      view.activityContent.text = activity.content
      view.activityDate.text = activity.date.toString()
      view.setOnClickListener {
        Log.d(TAG, "activity clicked $activity")
        listener?.onActivityClick(activity)
      }
    }
    //    https://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
  }

  fun setListener(listener: OnActivityClickListener) {
    this.listener = listener
  }

  interface OnActivityClickListener {
    fun onActivityClick(actvy: Activity)
  }
}