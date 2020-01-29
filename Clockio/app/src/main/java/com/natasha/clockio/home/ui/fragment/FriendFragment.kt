package com.natasha.clockio.home.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.natasha.clockio.R
import com.natasha.clockio.base.util.observeOnce
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.adapter.FriendAdapter
import com.natasha.clockio.home.ui.adapter.PaginationScrollListener
import com.natasha.clockio.home.viewmodel.FriendViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_friend.*
import kotlinx.android.synthetic.main.item_loading.*
import javax.inject.Inject



class FriendFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
  companion object {
    private val TAG: String = FriendFragment::class.java.simpleName
    fun newInstance() = FriendFragment()
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: FriendViewModel

  private var isLoading: Boolean = false
//  lateinit var listViewType: ArrayList<Int>
  private val PAGE_START = 0
  private val PAGE_SIZE = 10
  private var isLastPage = false
  private var totalPages = 1
  private var currentPage = PAGE_START
  lateinit var friendAdapter: FriendAdapter
  private var employeeList = mutableListOf<Employee>()

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.navigation_friend)
    return inflater.inflate(R.layout.fragment_friend, container, false)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(FriendViewModel::class.java)
    friendSwipeRefresh.setOnRefreshListener(this)
    observeFindAllEmployee()
    showFriends(arrayListOf())
  }

  fun showFriends(friends: MutableList<Employee>) {
    isLoading = false
    friendAdapter = FriendAdapter(context!!, friends)
    val linearLayoutManager = LinearLayoutManager(activity)
    friendRecyclerView.apply {
      layoutManager = linearLayoutManager
      adapter = friendAdapter
    }

//    https://blog.iamsuleiman.com/android-pagination-tutorial-getting-started-recyclerview/
    friendRecyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
      override fun loadMoreItems() {
        isLoading = true
        currentPage ++
        doFindAllEmployee(currentPage, PAGE_SIZE)
      }

      override fun getTotalPageCount(): Int {
        return totalPages
      }

      override fun isLastPage(): Boolean {
        return isLastPage
      }

      override fun isLoading(): Boolean {
        return isLoading
      }
    })
    doFindAllEmployee(PAGE_START, PAGE_SIZE)
  }

  fun observeFindAllEmployee() {
    viewModel.employeePage.observe(this, Observer {
      employeeList = it.content!!.toMutableList()
      totalPages = it.totalPages - 1
      currentPage = it.number
      isLastPage = it.last
      Log.d(TAG, "observeFindAll Friends currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
//      showFriends(employeeList)
    })
  }

  fun doFindAllEmployee(page: Int?, size: Int?) {
    viewModel.findAllEmployee(page, size)
//    observeFindAllEmployee()
    Log.d(TAG, "doFindAll Friends currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")

    Handler().postDelayed({
      if (currentPage != PAGE_START) friendAdapter.removeLoading()
      friendAdapter.addAll(employeeList)
      friendSwipeRefresh.isRefreshing = false

      if (currentPage < totalPages) {
        friendAdapter.addLoading()
      } else {
        isLastPage = true
        friendAdapter.removeLoading()
      }
      isLoading = false
    }, 1500)
  }

  override fun onRefresh() {
    currentPage = PAGE_START
    isLastPage = false
    friendAdapter.clear()
    doFindAllEmployee(PAGE_START, PAGE_SIZE)
  }
}
