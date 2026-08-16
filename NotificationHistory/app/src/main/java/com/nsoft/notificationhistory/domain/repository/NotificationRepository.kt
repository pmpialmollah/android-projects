package com.nsoft.notificationhistory.domain.repository

import com.nsoft.notificationhistory.data.local.entity.AppSettingEntity
import com.nsoft.notificationhistory.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<AppNotification>>
    suspend fun insertNotification(notification: AppNotification)
    suspend fun deleteNotification(notification: AppNotification)
    suspend fun clearAllNotifications()

    fun getAllAppSettings(): Flow<List<AppSettingEntity>>
    suspend fun toggleAppSetting(packageName: String, isEnabled: Boolean)
    suspend fun isAppEnabled(packageName: String): Boolean
}
