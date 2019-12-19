package com.natasha.clockio.home.ui.adapter

import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Activity
import kotlinx.android.synthetic.main.item_activity.view.*

//https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
class ActivityAdapter(private val activities: List<Activity>?):
    RecyclerView.Adapter<ActivityAdapter.ActivityHolder>() {

    private val TAG: String = ActivityAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        activities?.let {
            val itemActivity = it[position]
            holder.bind(itemActivity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): ActivityHolder {
        val inflatedView = parent.inflate(R.layout.item_activity, false)
        return ActivityHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        activities?.let {
            return it.size
        }
        return 0
    }

    class ActivityHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
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
        }

    }
}