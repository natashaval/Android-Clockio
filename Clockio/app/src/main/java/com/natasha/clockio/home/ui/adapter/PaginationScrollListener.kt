package com.natasha.clockio.home.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//https://blog.iamsuleiman.com/android-pagination-tutorial-getting-started-recyclerview/
abstract class PaginationScrollListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {
  val layoutManager = layoutManager
  val PAGE_START = 1
  val PAGE_SIZE = 10
  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val visibleItemCount = layoutManager.childCount
    val totalItemCount = layoutManager.itemCount
    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

    if (!isLoading() && !isLastPage()) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        loadMoreItems()
      }
    }
  }

  protected abstract fun loadMoreItems()
  abstract fun getTotalPageCount(): Int
  abstract fun isLastPage(): Boolean
  abstract fun isLoading(): Boolean

}