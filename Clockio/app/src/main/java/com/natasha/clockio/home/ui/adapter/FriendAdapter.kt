package com.natasha.clockio.home.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Employee
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.item_friend_call.view.*

//https://androidwave.com/pagination-in-recyclerview/
//https://www.codepolitan.com/cara-membuat-pagination-atau-load-more-menggunakan-recyclerview-part-1-59c689b1b2e76

class FriendAdapter constructor(val context: Context, private var friends: List<Employee>):
    RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

  private var isLoaderVisible: Boolean = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent?.context)
    return when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_friend, parent, false)
        return FriendHolder(context, inflatedView)
      }
      else -> {
        val inflatedView = layoutInflater.inflate(R.layout.item_loading, parent, false)
        return ViewHolderLoading(inflatedView)
      }
    }
  }

  override fun getItemCount(): Int = friends.size

  override fun getItemViewType(position: Int): Int {
    if (position == friends.size - 1 && isLoaderVisible) {
      return ITEM_VIEW_TYPE_LOADING
    } else {
      return ITEM_VIEW_TYPE_CONTENT
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//    val viewType = listViewType[position]
    val item = friends[position]
    val friendHolder = holder as FriendHolder
    friendHolder.bind(item)
    /*when(viewType) {
      ITEM_VIEW_TYPE_CONTENT -> {
        val friendHolder = holder as FriendHolder
        friendHolder.bind(item)
      }
      else -> {

      }
    }*/
  }

  fun refresh(friends: List<Employee>, listViewType: List<Int>) {
    Log.d(TAG, "refresh friends")
    this.friends = friends
//    this.listViewType = listViewType
    notifyDataSetChanged()
  }

//  https://blog.iamsuleiman.com/android-pagination-tutorial-getting-started-recyclerview/
  fun addItem(friend: Employee) {
    friends.plus(friend)
    notifyItemInserted(friends.size - 1)
  }

  fun removeItem(friend: Employee) {
    val position = friends.indexOf(friend)
    val item = friends.get(position)
    if (item != null) {
      friends.minus(item)
      notifyItemRemoved(position)
    }
  }

  fun addAll(newFriends: List<Employee>) {
    for(friend in newFriends) {
      addItem(friend)
    }
  }

  fun isEmpty(): Boolean {
    return itemCount == 0
  }

  fun addLoading() {
    isLoaderVisible = true
    addItem(Employee())
  }

  fun removeLoading() {
    isLoaderVisible = false
    val position = friends.size - 1
    val item = friends.get(position)
    if (item != null) {
      friends.minus(item)
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
      view.friendName.text = emp.firstName + " " + emp.lastName
      view.friendDepartment.text = emp.department.name
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
    }
  }

  inner class ViewHolderLoading(itemView: View) : ViewHolder(itemView)
}