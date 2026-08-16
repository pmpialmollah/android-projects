package com.nsoft.notificationhistory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nsoft.notificationhistory.data.local.dao.NotificationDao
import com.nsoft.notificationhistory.data.local.entity.AppSettingEntity
import com.nsoft.notificationhistory.data.local.entity.NotificationEntity

@Database(
    entities = [NotificationEntity::class, AppSettingEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
