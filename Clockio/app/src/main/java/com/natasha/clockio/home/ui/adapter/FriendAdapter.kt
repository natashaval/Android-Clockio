package com.natasha.clockio.home.ui.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Employee
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.item_friend_call.view.*

//https://androidwave.com/pagination-in-recyclerview/
//https://www.codepolitan.com/cara-membuat-pagination-atau-load-more-menggunakan-recyclerview-part-1-59c689b1b2e76

class FriendAdapter constructor(val context: Context, private val friends: List<Employee>):
    RecyclerView.Adapter<FriendAdapter.FriendHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val inflatedView = parent.inflate(R.layout.item_friend, false)
        return FriendHolder(context, inflatedView)
    }

    override fun getItemCount(): Int = friends.size

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        val item = friends[position]
    }

    companion object {
        private val TAG: String = FriendAdapter::class.java.simpleName
        val ITEM_VIEW_TYPE_CONTENT = 1
        val ITEM_VIEW_TYPE_LOADING = 2
    }

    class FriendHolder(context: Context, v: View): RecyclerView.ViewHolder(v), View.OnClickListener {
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
}