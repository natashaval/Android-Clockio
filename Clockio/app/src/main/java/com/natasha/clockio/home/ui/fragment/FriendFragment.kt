package com.natasha.clockio.home.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
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
    setHasOptionsMenu(true)
    viewModel = ViewModelProvider(this, factory).get(FriendViewModel::class.java)
    friendSwipeRefresh.setOnRefreshListener(this)
    observeFindAllEmployee()
    showFriends(arrayListOf())
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//    https://developer.android.com/training/search/setup
//    https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/
    inflater.inflate(R.menu.search, menu)

    val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = menu.findItem(R.id.action_search).actionView as SearchView
    searchView.apply {
      setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
    }
    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        friendAdapter.filter.filter(query)
        return false
      }
      override fun onQueryTextChange(newText: String?): Boolean {
        friendAdapter.filter.filter(newText)
        return false
      }
    })
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.action_search -> return true
    }
    return super.onOptionsItemSelected(item)
  }

  fun showFriends(friends: MutableList<Employee>) {
    isLoading = false
    friendAdapter = FriendAdapter(context!!, friends, friends)
    val linearLayoutManager = LinearLayoutManager(activity)
    friendRecyclerView.apply {
      layoutManager = linearLayoutManager
      adapter = friendAdapter
      itemAnimator = DefaultItemAnimator()
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
    }, 1000)
  }

  override fun onRefresh() {
    currentPage = PAGE_START
    isLastPage = false
    friendAdapter.clear()
    doFindAllEmployee(PAGE_START, PAGE_SIZE)
  }
}
