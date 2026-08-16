package com.nsoft.notificationhistory.domain.usecase

import com.nsoft.notificationhistory.domain.model.AppNotification
import com.nsoft.notificationhistory.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: AppNotification) {
        repository.deleteNotification(notification)
    }
}