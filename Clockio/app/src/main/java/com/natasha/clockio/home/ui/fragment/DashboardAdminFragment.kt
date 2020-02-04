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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.natasha.clockio.R
import com.natasha.clockio.activity.ui.ActivityFragment
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.adapter.CheckinAdapter
import com.natasha.clockio.home.ui.adapter.PaginationScrollListener
import com.natasha.clockio.home.viewmodel.DashboardViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_dashboard_admin.*
import javax.inject.Inject

class DashboardAdminFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
  companion object {
    private val TAG: String = DashboardAdminFragment::class.java.simpleName
    fun newInstance() = DashboardAdminFragment()
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: DashboardViewModel

  private var isLoading: Boolean = false
  private val pageStart = 0
  private val pageSize = 10
  private var isLastPage = false
  private var totalPages = 1
  private var currentPage = pageStart
  lateinit var checkinAdapter: CheckinAdapter
  private var employeeList = mutableListOf<Employee>()

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.navigation_dashboard)
    return inflater.inflate(R.layout.fragment_dashboard_admin, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)
    setHasOptionsMenu(true)
    observeCheckIn()
    showCheckIn(employeeList)
    checkinSwipeRefresh.setOnRefreshListener(this)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.action_search -> return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.search, menu)

    val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = menu.findItem(R.id.action_search).actionView as SearchView
    searchView.apply {
      setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
    }
    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        checkinAdapter.filter.filter(query)
        return false
      }
      override fun onQueryTextChange(newText: String?): Boolean {
        checkinAdapter.filter.filter(newText)
        return false
      }
    })
  }

  private fun observeCheckIn() {
    viewModel.checkinPage.observe(this, Observer {
      employeeList = it.content!!.toMutableList()
      totalPages = it.totalPages - 1
      currentPage = it.number
      isLastPage = it.last
      Log.d(TAG, "observe checkin currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
      attachAdapter(employeeList)
    })
  }

  private fun showCheckIn(checkin: MutableList<Employee>) {
    isLoading = false
    checkinAdapter = CheckinAdapter(this, checkin, checkin)
    checkinAdapter.setListener(object: CheckinAdapter.OnCheckInClickListener {
      override fun onClick(employee: Employee) {
        Log.d(TAG, "on Click checkin Employee $employee")
        val frag = ActivityFragment.newInstance(employee.id)
        val act = activity as HomeActivity
        act.addFragmentBackstack(frag)
      }
    })
    val linearLayoutManager = LinearLayoutManager(activity)
    checkinRecyclerView.apply {
      layoutManager = linearLayoutManager
      adapter = checkinAdapter
    }
    checkinRecyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
      override fun loadMoreItems() {
        isLoading = true
        currentPage ++
        doFindAllEmployee(currentPage, pageSize)
      }
      override fun getTotalPageCount(): Int = totalPages
      override fun isLastPage(): Boolean = isLastPage
      override fun isLoading(): Boolean = isLoading
    })
    doFindAllEmployee(pageStart, pageSize)
  }

  fun doFindAllEmployee(page: Int?, size: Int?) {
    viewModel.findAllEmployeeByCheckIn(page, size)
    Log.d(TAG, "doFindAll checkin currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
  }

  fun attachAdapter(list: List<Employee>) {
    Handler().postDelayed({
      if (currentPage != pageStart) checkinAdapter.removeLoading()
      checkinAdapter.addAll(list)
      checkinSwipeRefresh?.isRefreshing = false

      if (currentPage < totalPages) {
        checkinAdapter.addLoading()
      } else {
        isLastPage = true
        checkinAdapter.removeLoading()
      }
      isLoading = false
    }, 1500)
  }

  override fun onRefresh() {
    currentPage = pageStart
    isLastPage = false
    checkinAdapter.clear()
    doFindAllEmployee(pageStart, pageSize)
  }
}
