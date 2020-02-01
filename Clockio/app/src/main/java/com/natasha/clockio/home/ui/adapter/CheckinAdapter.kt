package com.natasha.clockio.home.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.natasha.clockio.R
import com.natasha.clockio.base.constant.UserConst
import com.natasha.clockio.home.entity.Employee
import kotlinx.android.synthetic.main.item_checkin.view.*
import kotlinx.android.synthetic.main.item_profile_image.view.*
import kotlinx.android.synthetic.main.item_status.view.*
import java.text.SimpleDateFormat

class CheckinAdapter constructor(val fragment: Fragment,
    private var checkin: MutableList<Employee>,
    private var checkinFilter: MutableList<Employee>):
    RecyclerView.Adapter<OpenViewHolder>(), Filterable {

  private var isLoaderVisible: Boolean = false
  private var listener: OnCheckInClickListener? = null

  companion object {
    private val TAG: String = CheckinAdapter::class.java.simpleName
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenViewHolder {
    val layoutInflater = LayoutInflater.from(parent?.context)
    Log.d(TAG, "layout inflater viewType $viewType")
    return when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_checkin, parent, false)
        CheckinHolder(inflatedView)
      }
      else -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_loading, parent, false)
        ViewHolderLoading(inflatedView)
      }
    }
  }

  override fun getItemCount(): Int = checkinFilter.size

  override fun getItemViewType(position: Int): Int {
    return if (position == checkinFilter.size - 1 && isLoaderVisible) {
      ITEM_VIEW_TYPE_LOADING
    } else {
      ITEM_VIEW_TYPE_CONTENT
    }
  }

  override fun onBindViewHolder(holder: OpenViewHolder, position: Int) {
    val viewType = getItemViewType(position)
    val item = checkinFilter[position]
    when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        Log.d(TAG, "onbindviewHolder CONTENT")
        val checkinHolder = holder as CheckinHolder
        checkinHolder.bind(item)
      }
      else -> {
        Log.d(TAG, "onbindviewholder LOADING")
      }
    }
  }

  fun addItem(friend: Employee) {
    Log.d(TAG, "adapter add item $friend")
    checkin.add(friend)
    notifyItemInserted(checkin.size - 1)
  }

  fun addAll(newFriends: List<Employee>) {
    Log.d(TAG, "adapter add All $newFriends")
    checkin.addAll(newFriends)
    Log.d(TAG, "adapter add All checkin: ${checkin.size} checkinFilter: ${checkinFilter.size}")
    notifyDataSetChanged()
  }

  fun addLoading() {
    Log.d(TAG, "adapter addLoading")
    isLoaderVisible = true
    addItem(Employee())
  }

  fun removeLoading() {
    Log.d(TAG, "adapter removeLoading")
    isLoaderVisible = false
    val position = checkin.size - 1
    val item = checkin[position]
    if (item != null) {
      checkin.remove(item)
      notifyItemRemoved(position)
    }
  }

  fun clear() {
    isLoaderVisible = false
    checkin.clear()
  }

  override fun getFilter(): Filter {
    return object: Filter() {
      override fun performFiltering(p0: CharSequence?): FilterResults {
        val charString = p0.toString()
        if (charString.isEmpty()) {
          checkinFilter = checkin
        } else {
          var filteredList = arrayListOf<Employee>()
          for (check in checkin) {
            if (check.firstName.toLowerCase().contains(
                    charString.toLowerCase()) || check.lastName?.toLowerCase()?.contains(
                    charString.toLowerCase()) != false) {
              filteredList.add(check)
            }
          }
          checkinFilter = filteredList
        }
        val filterResults = FilterResults()
        filterResults.values = checkinFilter
        return filterResults
      }

      override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
        checkinFilter = p1?.values as MutableList<Employee>
        notifyDataSetChanged()
      }

    }
  }

  inner class CheckinHolder(private val v: View): OpenViewHolder(v), View.OnClickListener {
    init {
      v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      Log.d(TAG, "click $p0")
    }

    fun bind(emp: Employee) {
      v.checkinName.text = emp.firstName + " " + emp.lastName
      v.checkinDepartment.text = emp.department.name
      emp.checkIn?.let {
        val dateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
        v.checkinDate.text = fragment.activity?.resources?.getString(R.string.employee_last_checkin, dateFormat.format(it))
      }
      setStatus(emp)
      if (emp.profileUrl.isNullOrBlank()) {
        v.profileInitial.letter = emp.firstName + " " + emp.lastName
      } else {
        v.profileImage.visibility = View.VISIBLE
        Glide.with(fragment).load(emp.profileUrl)
            .apply(RequestOptions.circleCropTransform()).into(v.profileImage)
      }
    }

    private fun setStatus(emp: Employee) {
      val statusText = emp.status?.toLowerCase()?.capitalize()
      v.statusText.text = statusText
      var iconPosition: Int = R.drawable.ic_status_online_24dp
      when(statusText) {
        UserConst.STATUS_ONLINE -> iconPosition = R.drawable.ic_status_online_24dp
        UserConst.STATUS_MEETING -> iconPosition = R.drawable.ic_status_meeting_24dp
        UserConst.STATUS_AWAY -> iconPosition = R.drawable.ic_status_away_24dp
        UserConst.STATUS_OFFLINE -> iconPosition = R.drawable.ic_status_offline_24dp
      }
      v.statusIcon.setImageResource(iconPosition)
    }

  }

  fun setListener(listener: OnCheckInClickListener) {
    this.listener = listener
  }

  interface OnCheckInClickListener {
    fun onClick(employee: Employee)
  }
}