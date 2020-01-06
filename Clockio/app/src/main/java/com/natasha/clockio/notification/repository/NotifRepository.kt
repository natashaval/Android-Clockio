package com.natasha.clockio.notification.repository

import android.util.Log
import androidx.paging.LivePagedListBuilder
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.base.util.ResponseUtils
import com.natasha.clockio.notification.dao.NotifLocalCache
import com.natasha.clockio.notification.entity.Notif
import com.natasha.clockio.notification.entity.NotifRequest
import com.natasha.clockio.notification.entity.NotifResult
import com.natasha.clockio.notification.service.NotifApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class NotifRepository @Inject constructor(
    private val networkSource: NotifNetworkSource,
    private val localCache: NotifLocalCache) {

    private val TAG: String = NotifRepository::class.java.simpleName

    fun getAllNotif(): NotifResult {
        Log.d(TAG, "Repository get All notif")
        val dataSourceFactory = localCache.getAll()
        val boundaryCallback = NotifBoundaryCallback(networkSource, localCache)
        val networkErrors = boundaryCallback.networkErrors

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return NotifResult(data, networkErrors)
    }

    companion object {
        const val DATABASE_PAGE_SIZE = 2
    }

    suspend fun createNotif(req: NotifRequest): BaseResponse<Any> {
        val response = networkSource.createNotif(req)
        return ResponseUtils.convertResponse(response)
    }
}