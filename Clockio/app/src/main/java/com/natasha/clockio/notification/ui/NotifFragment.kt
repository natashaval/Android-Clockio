package com.natasha.clockio.notification.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.constant.UserConst
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.notification.entity.Notif
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_notif.*
import javax.inject.Inject

class NotifFragment : Fragment() {

  companion object {
    fun newInstance() = NotifFragment()
    private val TAG: String = NotifFragment::class.java.simpleName
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: NotifViewModel
  private lateinit var act: HomeActivity
  private var adapter = NotifAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.navigation_notif)
    return inflater.inflate(R.layout.fragment_notif, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(NotifViewModel::class.java)

    observeAdapter()
    addNotifClick()
    swipeDelete()
    swipeRefresh()
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onResume() {
//    https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload/20702418
    Log.d(TAG, "notif onResume")
//    fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    super.onResume()
  }

  private fun observeAdapter() {
    adapter.setListener(object : NotifAdapter.OnNotifClickListener {
      override fun onNotifClick(notif: Notif) {
        viewModel.updateNotif(notif)
        act.addFragmentBackstack(NotifDetailsFragment.newInstance(notif))
      }

    })
    notifRecyclerView.adapter = adapter
    Log.d(TAG, "notif trigger!")
    viewModel.notifs.observe(this, Observer {
      Log.d(TAG, "notifList: ${it.size}")
      showEmptyList(it?.size == 0)
      adapter.submitList(it)
    })
    viewModel.networkErrors.observe(this, Observer {
      Toast.makeText(activity, "Whoops Notif Errors", Toast.LENGTH_SHORT).show()
    })
  }

  private fun showEmptyList(show: Boolean) {
    if (show) {
      notifNoResult.visibility = View.VISIBLE
      notifNoResultIcon.visibility = View.VISIBLE
      notifRecyclerView.visibility = View.INVISIBLE
    } else {
      notifNoResult.visibility = View.INVISIBLE
      notifNoResultIcon.visibility = View.INVISIBLE
      notifRecyclerView.visibility = View.VISIBLE
    }
  }

  private fun addNotifClick() {
    val userRole = sharedPref.getString(PreferenceConst.USER_ROLE_KEY, UserConst.ROLE_USER)
    if (userRole == UserConst.ROLE_ADMIN) notifAddButton.visibility = View.VISIBLE
    else notifAddButton.visibility = View.INVISIBLE
    notifAddButton.setOnClickListener {
      Log.d(TAG, "FAB notif clicked!")
      act.addFragmentBackstack(NotifAddFragment.newInstance())
    }
  }

  private fun swipeRefresh() {
    notifSwipeRefresh.setOnRefreshListener {
      Log.d(TAG, "notif swipeRefresh")
      notifSwipeRefresh.isRefreshing = false
    }
  }

  private fun swipeDelete() {
    //  https://stackoverflow.com/questions/49827752/how-to-implement-drag-and-drop-and-swipe-to-delete-in-recyclerview
//    https://demonuts.com/kotlin-recyclerview-swipe-to-delete/

    val swipeHandler = object: SwipeToDeleteCallback(activity!!) {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val deleteNotif = adapter.getNotif(viewHolder.adapterPosition)
        Log.d(TAG, "to be deleted $deleteNotif")
        deleteNotif?.let {
          viewModel.deleteNotif(it)
          adapter.removeItem(viewHolder.adapterPosition)
          Toast.makeText(activity, "Notif Deleted!", Toast.LENGTH_SHORT).show()
        }
      }
    }

    val itemTouchHelper = ItemTouchHelper(swipeHandler)
    itemTouchHelper.attachToRecyclerView(notifRecyclerView)
  }

  interface OnNotifReattachListener {
    fun onNotifReattach()
  }

  internal var reattachCallback: OnNotifReattachListener? = null
  fun setOnNotifReattachListener(callback: OnNotifReattachListener) {
    this.reattachCallback = callback
  }

  fun reAttachFragment() {
    Log.d(TAG, "notif reattach")
    val frg = fragmentManager?.findFragmentByTag(TAG)
    frg?.let {
      fragmentManager?.beginTransaction()?.detach(it)?.attach(it)?.commit()
    }
  }
}
