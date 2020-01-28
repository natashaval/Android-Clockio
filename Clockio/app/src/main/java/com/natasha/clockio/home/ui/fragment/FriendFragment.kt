package com.natasha.clockio.home.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.natasha.clockio.R
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.viewmodel.FriendViewModel
import javax.inject.Inject

class FriendFragment : Fragment() {

    companion object {
        fun newInstance() = FriendFragment()
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: FriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val act = activity as HomeActivity
        act.supportActionBar?.setTitle(R.string.navigation_friend)
        return inflater.inflate(R.layout.fragment_friend, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(FriendViewModel::class.java)
    }

}
