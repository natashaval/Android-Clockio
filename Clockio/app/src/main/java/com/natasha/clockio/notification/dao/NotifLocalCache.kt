package com.natasha.clockio.notification.dao

import android.util.Log
import androidx.paging.DataSource
import com.natasha.clockio.notification.entity.Notif
import java.util.concurrent.Executor

class NotifLocalCache(
    private val notifDao: NotifDao,
    private val ioExecutor: Executor
) {
    private val TAG: String = NotifLocalCache::class.java.simpleName

    fun insertAll(notifs: List<Notif>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d(TAG, "inserting notif in local ${notifs.size}")
            notifDao.insertAll(notifs)
            insertFinished()
        }
    }

    fun getAll(): DataSource.Factory<Int, Notif> {
        return notifDao.findAllNotif()
    }

    fun delete(notif: Notif, deleteFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d(TAG, "delete notif in local")
            notifDao.delete(notif)
            deleteFinished()
        }
    }

    fun insert(notif: Notif, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d(TAG, "insert notif $notif")
            notifDao.insert(notif)
            insertFinished()
        }
    }
}