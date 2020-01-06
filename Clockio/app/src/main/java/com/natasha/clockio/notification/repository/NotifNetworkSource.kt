package com.natasha.clockio.notification.repository

import android.util.Log
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.notification.entity.Notif
import com.natasha.clockio.notification.entity.NotifRequest
import com.natasha.clockio.notification.service.NotifApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class NotifNetworkSource @Inject constructor(private val notifApi: NotifApi) {
    private val TAG: String = NotifNetworkSource::class.java.simpleName

    fun getAll(page: Int, size: Int,
               onSuccess: (notifList: List<Notif>) -> Unit,
               onError: (error: String) -> Unit) {
        Log.d(TAG, "notif api page: $page, size: $size")
        notifApi.getAllNotification(page, size).enqueue(object: Callback<PageResponse<Notif>> {
            override fun onFailure(call: Call<PageResponse<Notif>>, t: Throwable) {
                Log.d(TAG, "notif failed ${t.message}")
                onError(t.message?: "unknown error")
            }

            override fun onResponse(
                call: Call<PageResponse<Notif>>,
                response: Response<PageResponse<Notif>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "notif success ${response.body()}")
                    val notifs = response.body()?.content ?: emptyList()
                    onSuccess(notifs)
                } else {
                    Log.d(TAG, "notif failed ${response.errorBody()}")
                    onError(response.errorBody()?.toString() ?: "Unknown error")
                }
            }

        })
    }

    suspend fun createNotif(notif: NotifRequest): Response<DataResponse> {
        return notifApi.createNotif(notif)
    }

    suspend fun deleteNotif(id: Long): Response<DataResponse> {
        return notifApi.deleteNotif(id)
    }
}