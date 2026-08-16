package com.nsoft.notificationhistory.domain.usecase

import com.nsoft.notificationhistory.domain.repository.NotificationRepository
import javax.inject.Inject

class ClearAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        repository.clearAllNotifications()
    }
}