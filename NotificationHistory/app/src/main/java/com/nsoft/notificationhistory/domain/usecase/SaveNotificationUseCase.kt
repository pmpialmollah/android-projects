package com.nsoft.notificationhistory.domain.usecase

import com.nsoft.notificationhistory.domain.model.AppNotification
import com.nsoft.notificationhistory.domain.repository.NotificationRepository

class SaveNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: AppNotification) {
        if (repository.isAppEnabled(notification.packageName)) {
            repository.insertNotification(notification)
        }
    }
}
