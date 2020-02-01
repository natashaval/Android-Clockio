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
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.item_friend_call.view.*
import kotlinx.android.synthetic.main.item_profile_image.view.*
import kotlinx.android.synthetic.main.item_status.view.*

//https://androidwave.com/pagination-in-recyclerview/
//https://www.codepolitan.com/cara-membuat-pagination-atau-load-more-menggunakan-recyclerview-part-1-59c689b1b2e76

class FriendAdapter constructor(val fragment: Fragment,
    private var friends: MutableList<Employee>,
    private var friendsFiltered: MutableList<Employee>):
    RecyclerView.Adapter<OpenViewHolder>(), Filterable {

  private val TAG: String = FriendAdapter::class.java.simpleName
  private var isLoaderVisible: Boolean = false
  private var listener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenViewHolder {
    val layoutInflater = LayoutInflater.from(parent?.context)
    Log.d(TAG, "layout inflater viewType $viewType")
    return when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_friend, parent, false)
        FriendHolder(inflatedView)
      }
      else -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_loading, parent, false)
        ViewHolderLoading(inflatedView)
      }
    }
  }

  override fun getItemCount(): Int = friendsFiltered.size

  override fun getItemViewType(position: Int): Int {
    Log.d(TAG, "getViewItemType position: $position filteredSize: ${friendsFiltered.size}")
    return if (position == friendsFiltered.size - 1 && isLoaderVisible) {
      ITEM_VIEW_TYPE_LOADING
    } else {
      ITEM_VIEW_TYPE_CONTENT
    }
  }

  override fun onBindViewHolder(holder: OpenViewHolder, position: Int) {
    val viewType = getItemViewType(position)
    val item = friendsFiltered[position]
    when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        Log.d(TAG, "onbindviewHolder CONTENT")
        val friendHolder = holder as FriendHolder
        friendHolder.bind(item)
      }
      else -> {
        Log.d(TAG, "onbindviewholder LOADING")
      }
    }
  }

  //  https://blog.iamsuleiman.com/android-pagination-tutorial-getting-started-recyclerview/
  fun addItem(friend: Employee) {
    Log.d(TAG, "adapter add item $friend")
    friends.add(friend)
    notifyItemInserted(friends.size - 1)
  }

  fun removeItem(friend: Employee) {
    val position = friends.indexOf(friend)
    val item = friends[position]
    if (item != null) {
      friends.remove(item)
      notifyItemRemoved(position)
    }
  }

  fun addAll(newFriends: List<Employee>) {
    Log.d(TAG, "adapter add All $newFriends")
    friends.addAll(newFriends)
    Log.d(TAG, "adapter add All friends: ${friends.size} friendsFiltered: ${friendsFiltered.size}")
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
    val position = friends.size - 1
    val item = friends[position]
    if (item != null) {
      friends.remove(item)
      notifyItemRemoved(position)
    }
  }

  fun clear() {
    isLoaderVisible = false
    friends.clear()
  }

  inner class FriendHolder(private val v: View): OpenViewHolder(v), View.OnClickListener {
    private val TAG: String = FriendHolder::class.java.simpleName
    init {
      v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      Log.d(TAG, "click $p0")
    }

    fun bind(emp: Employee) {
      Log.d(TAG, "holder bind $emp")
      v.friendName.text = emp.firstName + " " + emp.lastName
      v.friendDepartment.text = emp.department.name
      setStatus(emp)
      v.friendPhone.setOnClickListener {
        Log.d(TAG, "phone clicked ${emp.phone}")
        emp.phone?.let {
          listener?.onPhoneClick(it)
        }
      }
      v.friendEmail.setOnClickListener {
        Log.d(TAG, "email clicked ${emp.email}")
        emp.email?.let {
          listener?.onEmailClick(it)
        }
      }
      v.friendWhatsapp.setOnClickListener {
        Log.d(TAG, "whatsapp clicked ${emp.phone} how to check??")
        emp.phone?.let {
          listener?.onWhatsappClick(it)
        }
      }
      v.friendLocation.setOnClickListener {
        Log.d(TAG, "location clicked (${emp.latitude},${emp.longitude}")
        if (emp.latitude != null && emp.longitude != null) {
          listener?.onLocationClick(emp.latitude, emp.longitude)
        }
      }
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

  override fun getFilter(): Filter {
    //    https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/
    return object: Filter() {
      override fun performFiltering(p0: CharSequence?): FilterResults {
        val charString = p0.toString()
        if (charString.isEmpty()) {
          friendsFiltered = friends
        } else {
          var filteredList = arrayListOf<Employee>()
          for (friend in friends) {
            if (friend.firstName.toLowerCase().contains(
                    charString.toLowerCase()) || friend.lastName?.toLowerCase()?.contains(
                    charString.toLowerCase()) != false) {
              filteredList.add(friend)
            }
          }
          friendsFiltered = filteredList
        }
        val filterResults = FilterResults()
        filterResults.values = friendsFiltered
        return filterResults
      }

      override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
        isLoaderVisible = false
        friendsFiltered = p1?.values as MutableList<Employee>
        notifyDataSetChanged()
      }

    }
  }

  fun setListener(listener: OnItemClickListener) {
    this.listener = listener
  }

  interface OnItemClickListener {
    fun onPhoneClick(phone: String)
    fun onEmailClick(email: String)
    fun onLocationClick(latitude: Double, longitude: Double)
    fun onWhatsappClick(phone: String)
  }
}