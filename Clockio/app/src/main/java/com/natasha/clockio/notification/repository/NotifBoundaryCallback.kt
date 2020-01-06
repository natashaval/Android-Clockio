package com.natasha.clockio.notification.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.natasha.clockio.notification.dao.NotifLocalCache
import com.natasha.clockio.notification.entity.Notif

class NotifBoundaryCallback(
    private val networkSource: NotifNetworkSource,
    private val localCache: NotifLocalCache
): PagedList.BoundaryCallback<Notif>() {

    private val TAG: String = NotifBoundaryCallback::class.java.simpleName
    private var lastRequestedPage = 0
    private val _networkErrors = MutableLiveData<String>()

    val networkErrors: LiveData<String>
        get() = _networkErrors

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        Log.d(TAG, "notif onZeroItemsLoaded")
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Notif) {
        Log.d(TAG, "notif onItemAtEndLoaded")
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return
        isRequestInProgress = true
        networkSource.getAll(lastRequestedPage, NETWORK_PAGE_SIZE, { notifs ->
            localCache.insertAll(notifs) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 5
    }
}