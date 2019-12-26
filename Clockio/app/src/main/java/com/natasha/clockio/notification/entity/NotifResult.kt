package com.natasha.clockio.notification.entity

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import java.io.Serializable

data class NotifResult (
    val data: LiveData<PagedList<Notif>>,
    val networkErrors: LiveData<String>
)