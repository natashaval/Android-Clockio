package com.natasha.clockio.home.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Employee
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.item_friend_call.view.*
import kotlinx.android.synthetic.main.item_status.view.*

//https://androidwave.com/pagination-in-recyclerview/
//https://www.codepolitan.com/cara-membuat-pagination-atau-load-more-menggunakan-recyclerview-part-1-59c689b1b2e76

class FriendAdapter constructor(val context: Context, private var friends: MutableList<Employee>):
    RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

  private var isLoaderVisible: Boolean = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent?.context)
    Log.d(TAG, "layout inflater viewType $viewType")
    return when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_friend, parent, false)
        FriendHolder(context, inflatedView)
      }
      else -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_loading, parent, false)
        ViewHolderLoading(inflatedView)
      }
    }
  }

  override fun getItemCount(): Int {
    Log.d(TAG, "friend getItemCount ${friends.size}")
    return friends.size
  }

  override fun getItemViewType(position: Int): Int {
    Log.d(TAG, "getItemViewType friendsize ${friends.size}")
    if (position == friends.size - 1 && isLoaderVisible) {
      return ITEM_VIEW_TYPE_LOADING
    } else {
      return ITEM_VIEW_TYPE_CONTENT
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val viewType = getItemViewType(position)
    val item = friends[position]
//    val friendHolder = holder as FriendHolder
//    friendHolder.bind(item)
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

  fun refresh(friends: List<Employee>, listViewType: List<Int>) {
    Log.d(TAG, "refresh friends")
//    this.friends = friends
//    this.listViewType = listViewType
    notifyDataSetChanged()
  }

//  https://blog.iamsuleiman.com/android-pagination-tutorial-getting-started-recyclerview/
  fun addItem(friend: Employee) {
    Log.d(TAG, "adapter add item $friend")
//    friends.plus(friend)
    friends.add(friend)
    notifyItemInserted(friends.size - 1)
  }

  fun removeItem(friend: Employee) {
    val position = friends.indexOf(friend)
    val item = friends.get(position)
    if (item != null) {
//      friends.minus(item)
      friends.remove(item)
      notifyItemRemoved(position)
    }
  }

  fun addAll(newFriends: List<Employee>) {
    Log.d(TAG, "adapter add All $newFriends")
    /*for(friend in newFriends) {
      addItem(friend)
    }*/
    friends.addAll(newFriends)
    notifyDataSetChanged()
  }

  fun isEmpty(): Boolean {
    return itemCount == 0
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
//      friends.minus(item)
      friends.remove(item)
      notifyItemRemoved(position)
    }
  }

  fun clear() {
    isLoaderVisible = false
    while (itemCount > 0) {
      removeItem(friends.get(0))
    }
  }

  companion object {
    private val TAG: String = FriendAdapter::class.java.simpleName
    val ITEM_VIEW_TYPE_CONTENT = 1
    val ITEM_VIEW_TYPE_LOADING = 2
  }

  open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

  inner class FriendHolder(context: Context, v: View): ViewHolder(v), View.OnClickListener {
    private val TAG: String = FriendHolder::class.java.simpleName
    private val view = v
    init {
      v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      Log.d(TAG, "click $p0")
    }

    fun bind(emp: Employee) {
      Log.d(TAG, "holder bind $emp")
      view.friendName.text = emp.firstName + " " + emp.lastName
      view.friendDepartment.text = emp.department.name
      setStatus(emp)
      view.friendPhone.setOnClickListener {
        Log.d(TAG, "phone clicked ${emp.phone}")
      }
      view.friendEmail.setOnClickListener {
        Log.d(TAG, "email clicked ${emp.email}")
      }
      view.friendWhatsapp.setOnClickListener {
        Log.d(TAG, "whatsapp clicked ${emp.phone} how to check??")
      }
      view.friendLocation.setOnClickListener {
        Log.d(TAG, "location clicked (${emp.latitude},${emp.longitude}")
      }
      Glide.with(context).load(emp.profileUrl)
          .apply(RequestOptions.circleCropTransform()).into(view.friendImage)
    }

    private fun setStatus(emp: Employee) {
      view.statusText.text = emp.status?.toLowerCase()?.capitalize()
      var iconPosition: Int = R.drawable.ic_status_online_24dp
      when(emp.status?.toLowerCase()) {
        "online" -> iconPosition = R.drawable.ic_status_online_24dp
        "meeting" -> iconPosition = R.drawable.ic_status_meeting_24dp
        "away" -> iconPosition = R.drawable.ic_status_away_24dp
        "offline" -> iconPosition = R.drawable.ic_status_offline_24dp
      }
      view.statusIcon.setImageResource(iconPosition)
    }
  }

  inner class ViewHolderLoading(itemView: View) : ViewHolder(itemView)
}