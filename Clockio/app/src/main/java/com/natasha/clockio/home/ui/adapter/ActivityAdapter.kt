package com.natasha.clockio.home.ui.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Activity
import kotlinx.android.synthetic.main.item_activity.view.*
import java.text.SimpleDateFormat

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

  inner class ActivityHolder(private val v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private val TAG: String = ActivityHolder::class.java.simpleName

    init {
      v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      Log.d(TAG, "click $p0")
    }

    fun bind(actvy: Activity) {
      v.activityTitle.text = actvy.title
      v.activityContent.text = actvy.content
      val dateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
      v.activityDate.text = dateFormat.format(actvy.date).toString()
      v.setOnClickListener {
        Log.d(TAG, "activity clicked $actvy")
        listener?.onActivityClick(actvy)
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