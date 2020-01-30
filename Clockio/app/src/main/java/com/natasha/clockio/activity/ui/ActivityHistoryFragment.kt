package com.natasha.clockio.activity.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker

import com.natasha.clockio.R
import com.natasha.clockio.activity.viewmodel.ActivityViewModel
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.adapter.ActivityAdapter
import com.natasha.clockio.home.ui.adapter.PaginationScrollListener
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_activity_history.*
import kotlinx.android.synthetic.main.item_activity_recyler_view.*
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class ActivityHistoryFragment : Fragment() {

  companion object {
    fun newInstance() = ActivityHistoryFragment()
    private val TAG: String = ActivityHistoryFragment::class.java.simpleName
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: ActivityViewModel
  private var selectedDate: androidx.core.util.Pair<Long, Long> = androidx.core.util.Pair(0,0)

  private var employeeId: String? = ""
  private var isLoading: Boolean = false
  private val pageStart = 0
  private val pageSize = 10
  private var isLastPage = false
  private var totalPages = 1
  private var currentPage = pageStart
  lateinit var historyAdapter: ActivityHistoryAdapter
  private var historyList = mutableListOf<Activity>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_activity_history, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)

    employeeId = sharedPref.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
    setHistoryDate()
    observeActivityHistory()
    showHistory(arrayListOf())
    showActivity()
  }


  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onStart() {
    super.onStart()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onOpen()
  }

  override fun onStop() {
    super.onStop()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onClose()
  }

  private fun setHistoryDate() {
    activityHistoryDateInput.setOnClickListener {
      val builder = MaterialDatePicker.Builder.dateRangePicker()
      builder.apply {
        setTheme(resolveOrThrow(context!!, R.attr.materialCalendarTheme))
        build()
      }
      val picker = builder.build()
      try {
        addCalendarListener(picker)
        picker.show(activity?.supportFragmentManager!!, picker.toString())
      } catch (e: IllegalArgumentException) {
        Log.e(TAG, "calendar error ${e.message}")
        e.printStackTrace()
      }
    }
  }

  private fun resolveOrThrow(context: Context, @AttrRes attributeResId: Int): Int {
    val typedValue = TypedValue()
    if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
      return typedValue.data
    }
    throw IllegalArgumentException(context.resources.getResourceName(attributeResId))
  }

  private fun addCalendarListener(picker: MaterialDatePicker<*>) {
    picker.addOnPositiveButtonClickListener { selection ->
      selectedDate = selection as androidx.core.util.Pair<Long, Long>
      Log.d(TAG, "Date String = ${picker.headerText}:: Date epoch values:: ${selectedDate.first} to ${selectedDate.second}")
//      https://stackoverflow.com/questions/6850874/how-to-create-a-java-date-object-of-midnight-today-and-midnight-tomorrow
      val aDay = 24*60*60*1000
      val startDate = Date(selectedDate.first!!)
      val endDate = Date(selectedDate.second!! + aDay)
      Log.d(TAG, "Real Date start: $startDate end: $endDate")
      activityHistoryDateInput.setText(picker.headerText)

      historyAdapter.clear()
      doGetActivityHistory(pageStart, pageSize)
    }
    picker.addOnNegativeButtonClickListener { dialog ->
      Log.d(TAG, "calendar history negative")
    }
    picker.addOnCancelListener { dialog ->
      Log.d(TAG, "calendar history cancel")
    }
  }

  private fun observeActivityHistory() {
    viewModel.activityPage.observe(this, androidx.lifecycle.Observer {
      historyList = it.content!!.toMutableList()
      Log.d(TAG, "history fragment ${it.content}")
      totalPages = it.totalPages - 1
      currentPage = it.number
      isLastPage = it.last
      Log.d(TAG, "observeFindAll history sizeChanged?: ${historyList.size} currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
      showActivity()
    })
  }

  private fun showHistory(activities: MutableList<Activity>) {
    isLoading = false
    historyAdapter = ActivityHistoryAdapter(activities)
    historyAdapter.setListener(object: ActivityAdapter.OnActivityClickListener {
      override fun onActivityClick(actvy: Activity) {
        val mFragment = ActivityDetailsFragment.newInstance()
        val mBundle = Bundle()
        mBundle.putParcelable(ParcelableConst.ITEM_ACTIVITY, actvy)
        mFragment.arguments = mBundle
        val act = activity as HomeActivity
        act.addFragmentBackstack(mFragment)
      }
    })
    val linearLayoutManager = LinearLayoutManager(activity)
    activityRecyclerView.apply {
      layoutManager = linearLayoutManager
      adapter = historyAdapter
      addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    activityRecyclerView.addOnScrollListener(object: PaginationScrollListener(linearLayoutManager) {
      override fun loadMoreItems() {
        isLoading = true
        currentPage++
        doGetActivityHistory(currentPage, pageSize)
      }

      override fun getTotalPageCount(): Int = totalPages

      override fun isLastPage(): Boolean = isLastPage

      override fun isLoading(): Boolean = isLoading
    })
  }

  fun doGetActivityHistory(page: Int?, size: Int?) {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val startDate = formatter.format(selectedDate.first)
    val endDate = formatter.format(selectedDate.second)
    viewModel.getActivityHistory(employeeId!!, startDate, endDate, page, size)

    Log.d(TAG, "doGetAll history size: ${historyList.size} currPage: $currentPage totalPage: $totalPages islastPage: $isLastPage")
    Handler().postDelayed({
      if (currentPage != pageStart) historyAdapter.removeLoading()
      historyAdapter.addAll(historyList)
      historyAdapter.notifyDataSetChanged()
      if (currentPage < totalPages) {
        historyAdapter.addLoading()
      } else {
        isLastPage = true
        historyAdapter.removeLoading()
      }
      isLoading = false
    }, 1000)
  }

  private fun showActivity() {
    if(historyList.isEmpty()) {
      Log.d(TAG, "show history empty ${historyList.size}")
      activityHistoryNotAvailableLabel.visibility = View.VISIBLE
      activityHistoryRecyclerViewLayout.visibility = View.INVISIBLE
    } else {
      Log.d(TAG, "show history ${historyList.size}")
      activityHistoryNotAvailableLabel.visibility = View.INVISIBLE
      activityHistoryRecyclerViewLayout.visibility = View.VISIBLE
    }
  }
}
