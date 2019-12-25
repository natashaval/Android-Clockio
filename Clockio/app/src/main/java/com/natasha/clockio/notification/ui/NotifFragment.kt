package com.natasha.clockio.notification.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.natasha.clockio.R
import com.natasha.clockio.home.ui.HomeActivity
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NotifFragment : Fragment() {

  companion object {
    fun newInstance() = NotifFragment()
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: NotifViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.navigation_notif)
    return inflater.inflate(R.layout.fragment_notif, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(NotifViewModel::class.java)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

}
