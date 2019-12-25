package com.natasha.clockio.notification.repository

import com.natasha.clockio.notification.dao.NotifDao
import com.natasha.clockio.notification.service.NotifApi
import javax.inject.Inject

class NotifRepository @Inject constructor(
    private val notifDao: NotifDao,
    private val notifApi: NotifApi) {

}