package com.natasha.clockio.notification.service

import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.notification.entity.Notif
import com.natasha.clockio.notification.entity.NotifRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NotifApi {
  @GET("/api/notification")
  fun getAllNotification(@Query("page") page: Int,
      @Query("size") size: Int): Call<PageResponse<Notif>>

  @GET("/api/notification/{id}")
  fun findById(@Path("id") id: Long)

  @POST("/api/notification")
  suspend fun createNotif(@Body notif: NotifRequest) : Response<DataResponse>

  @PUT("/api/notification/{id}")
  fun updateNotif(@Path("id") id: Long, @Body notif: Notif)

  @DELETE("/api/notification/{id}")
  suspend fun deleteNotif(@Path("id") id:Long) : Response<DataResponse>
}