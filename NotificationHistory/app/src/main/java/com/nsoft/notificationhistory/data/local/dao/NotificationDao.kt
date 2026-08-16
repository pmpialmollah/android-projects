package com.nsoft.notificationhistory.data.local.dao

import androidx.room.*
import com.nsoft.notificationhistory.data.local.entity.AppSettingEntity
import com.nsoft.notificationhistory.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY postTime DESC")
    fun getNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("SELECT * FROM app_settings")
    fun getAllAppSettings(): Flow<List<AppSettingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppSetting(setting: AppSettingEntity)

    @Query("SELECT isEnabled FROM app_settings WHERE packageName = :packageName")
    suspend fun isAppEnabled(packageName: String): Boolean?

    @Query("""
        SELECT COUNT(*) FROM notifications 
        WHERE packageName = :packageName 
        AND (title = :title OR (title IS NULL AND :title IS NULL)) 
        AND (text = :text OR (text IS NULL AND :text IS NULL)) 
        AND postTime BETWEEN :minTime AND :maxTime
    """)
    suspend fun countSimilar(
        packageName: String,
        title: String?,
        text: String?,
        minTime: Long,
        maxTime: Long
    ): Int
}
