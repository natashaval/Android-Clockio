package com.natasha.clockio.notification.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration

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
      Toast.makeText(activity, "Whoops Notif Network Errors", Toast.LENGTH_SHORT).show()
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

}
