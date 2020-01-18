package com.natasha.clockio.notification.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.natasha.clockio.base.database.BaseDao
import com.natasha.clockio.notification.entity.Notif

@Dao
interface NotifDao: BaseDao<Notif> {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(notifList: List<Notif>)

  @Query("SELECT * FROM notif ORDER BY updatedAt DESC")
  fun findAllNotif(): DataSource.Factory<Int, Notif>
}