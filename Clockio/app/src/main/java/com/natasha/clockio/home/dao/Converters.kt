package com.natasha.clockio.home.dao

import androidx.room.TypeConverter
import java.util.*

class Converters {
  companion object {
    @TypeConverter @JvmStatic fun fromTimestamp(timestamp: Long?): Date? {
      return timestamp?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(date: Date?): Long? {
      return date?.time?.toLong()
    }

    /*@TypeConverter
    @JvmStatic
    fun fromUUID(id: UUID) : String? {
      return id.toString()
    }*/
  }
}