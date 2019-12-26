package com.natasha.clockio.notification.service

import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.notification.entity.Notif
import retrofit2.Call
import retrofit2.http.*

interface NotifApi {
  @GET("/api/notification")
  fun getAllNotification(@Query("page") page: Int,
      @Query("size") size: Int): Call<PageResponse<Notif>>

  @GET("/api/notification/{id}")
  fun findById(@Path("id") id: Long)

  @POST("/api/notification")
  fun createNotif(@Body notif: Notif)

  @PUT("/api/notification/{id}")
  fun updateNotif(@Path("id") id: Long, @Body notif: Notif)

  @DELETE("/api/notification/{id}")
  fun deleteNotif(@Path("id") id:Long)
}