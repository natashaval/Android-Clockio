package com.natasha.clockio.notification.ui

import android.content.Context
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
import com.natasha.clockio.home.ui.HomeActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_notif.*
import javax.inject.Inject

class NotifFragment : Fragment() {

  companion object {
    fun newInstance() = NotifFragment()
    private val TAG: String = NotifFragment::class.java.simpleName
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: NotifViewModel
  private val adapter = NotifAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.navigation_notif)
    return inflater.inflate(R.layout.fragment_notif, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(NotifViewModel::class.java)

    //    val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
    //    notifRecyclerView.addItemDecoration(decoration)

    observeAdapter()
    addNotifClick()
    swipeDelete()
    swipeRefresh()
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  private fun observeAdapter() {
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
      notifRecyclerView.visibility = View.GONE
    } else {
      notifNoResult.visibility = View.GONE
      notifRecyclerView.visibility = View.VISIBLE
    }
  }

  private fun addNotifClick() {
    notifAddButton.setOnClickListener {
      Log.d(TAG, "FAB notif clicked!")
      fragmentManager?.beginTransaction()?.
          replace(R.id.content, NotifAddFragment.newInstance())?.
          addToBackStack(null)?.
          commit()
    }
  }

  private fun swipeRefresh() {
    notifSwipeRefresh.setOnRefreshListener {
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

}
