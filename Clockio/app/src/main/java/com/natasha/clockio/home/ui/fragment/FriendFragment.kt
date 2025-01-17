package com.natasha.clockio.home.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.constant.UserConst
import com.natasha.clockio.friend.ui.EmployeeAddFragment
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.adapter.FriendAdapter
import com.natasha.clockio.home.ui.adapter.PaginationScrollListener
import com.natasha.clockio.home.viewmodel.FriendViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_friend.*
import javax.inject.Inject



class FriendFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
  companion object {
    private val TAG: String = FriendFragment::class.java.simpleName
    fun newInstance() = FriendFragment()
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: FriendViewModel

  private var isLoading: Boolean = false
  private val pageStart = 0
  private val pageSize = 10
  private var isLastPage = false
  private var totalPages = 1
  private var currentPage = pageStart
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
    showFriends(employeeList)
    showFriendVisibility()
    showAddEmployee()
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

  private fun showFriends(friends: MutableList<Employee>) {
    isLoading = false
    friendAdapter = FriendAdapter(this, friends, friends)
    friendAdapter.setListener(object: FriendAdapter.OnItemClickListener {
      override fun onPhoneClick(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
          data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
      }

      override fun onEmailClick(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
          data = Uri.parse("mailto:$email")
        }
        if (intent.resolveActivity(activity!!.packageManager) != null) {
          startActivity(intent)
        }
      }

      override fun onLocationClick(latitude: Double, longitude: Double) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
          data = Uri.parse("geo:0,0?q=$latitude,$longitude()")
        }
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(activity!!.packageManager) != null){
          startActivity(intent)
        }
      }

      override fun onWhatsappClick(phone: String) {
//        https://stackoverflow.com/questions/38422300/how-to-open-whatsapp-using-an-intent-in-your-android-app
        // use country code with phone number
        val url = "https://api.whatsapp.com/send?phone=$phone"
        try {
          val packageManager = activity!!.packageManager
          packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
          val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
          }
          startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
          Toast.makeText(activity, "Whatsapp not installed in your phone", Toast.LENGTH_SHORT).show()
          e.printStackTrace()
        }
      }

    })
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
        doFindAllEmployee(currentPage, pageSize)
      }
      override fun getTotalPageCount(): Int = totalPages
      override fun isLastPage(): Boolean = isLastPage
      override fun isLoading(): Boolean = isLoading
    })
    doFindAllEmployee(pageStart, pageSize)
  }

  private fun observeFindAllEmployee() {
    viewModel.employeePage.observe(this, Observer {
      employeeList = it.content!!.toMutableList()
      totalPages = it.totalPages - 1
      currentPage = it.number
      isLastPage = it.last
      Log.d(TAG, "observeFindAll Friends currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
      showFriendVisibility()
      attachAdapter(employeeList)
    })
  }

  fun doFindAllEmployee(page: Int?, size: Int?) {
    viewModel.findAllEmployee(page, size)
    Log.d(TAG, "doFindAll Friends currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
  }

  private fun attachAdapter(list: List<Employee>) {
    Handler().postDelayed({
      if (currentPage != pageStart) friendAdapter.removeLoading()
      friendAdapter.addAll(list)
      friendSwipeRefresh?.isRefreshing = false

      if (!isLastPage) {
        friendAdapter.addLoading()
      } else {
        isLastPage = true
      }
      isLoading = false
    }, 1500)
  }

  override fun onRefresh() {
    currentPage = pageStart
    isLastPage = false
    friendAdapter.clear()
    doFindAllEmployee(pageStart, pageSize)
  }

  private fun showAddEmployee() {
    val userRole = sharedPref.getString(PreferenceConst.USER_ROLE_KEY, UserConst.ROLE_USER)
    if (userRole == UserConst.ROLE_ADMIN) employeeAddButton.visibility = View.VISIBLE

    employeeAddButton.setOnClickListener {
      Log.d(TAG, "FAB friend clicked!")
      fragmentManager?.beginTransaction()?.
        replace(R.id.content, EmployeeAddFragment.newInstance())?.
        addToBackStack(null)?.
        commit()
    }
  }

  private fun showFriendVisibility() {
    if (employeeList.isEmpty()) {
      friendLoadingIcon.visibility = View.VISIBLE
      friendLoadingTextView.visibility = View.VISIBLE
      friendSwipeRefresh.visibility = View.INVISIBLE
      friendRecyclerView.visibility = View.INVISIBLE
    } else {
      friendLoadingIcon.visibility = View.INVISIBLE
      friendLoadingTextView.visibility = View.INVISIBLE
      friendSwipeRefresh.visibility = View.VISIBLE
      friendRecyclerView.visibility = View.VISIBLE
    }
  }
}
