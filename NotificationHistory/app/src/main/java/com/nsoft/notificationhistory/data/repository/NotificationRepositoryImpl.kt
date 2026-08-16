package com.nsoft.notificationhistory.data.repository

import android.util.Log
import com.nsoft.notificationhistory.data.local.dao.NotificationDao
import com.nsoft.notificationhistory.data.local.entity.AppSettingEntity
import com.nsoft.notificationhistory.data.mapper.toDomain
import com.nsoft.notificationhistory.data.mapper.toEntity
import com.nsoft.notificationhistory.domain.model.AppNotification
import com.nsoft.notificationhistory.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepositoryImpl(
    private val dao: NotificationDao,
) : NotificationRepository {

    override fun getNotifications(): Flow<List<AppNotification>> {
        return dao.getNotifications().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertNotification(notification: AppNotification) {
        val toleranceMs = 2000L // 2-second window to treat as duplicate

        val existingCount = dao.countSimilar(
            packageName = notification.packageName,
            title = notification.title,
            text = notification.text,
            minTime = notification.postTime - toleranceMs,
            maxTime = notification.postTime + toleranceMs
        )

        if (existingCount == 0) {
            dao.insertNotification(notification.toEntity())
        } else {
            Log.d("PialNotification", "Duplicate skipped: ${notification.title}")
        }
    }

    override suspend fun deleteNotification(notification: AppNotification){
        dao.deleteNotification(notification.toEntity())
    }

    override suspend fun clearAllNotifications(){
        dao.deleteAllNotifications()
    }

    override fun getAllAppSettings(): Flow<List<AppSettingEntity>> {
        return dao.getAllAppSettings()
    }

    override suspend fun toggleAppSetting(packageName: String, isEnabled: Boolean) {
        dao.insertAppSetting(AppSettingEntity(packageName, isEnabled))
    }

    override suspend fun isAppEnabled(packageName: String): Boolean {
        return dao.isAppEnabled(packageName) ?: false
    }
}
