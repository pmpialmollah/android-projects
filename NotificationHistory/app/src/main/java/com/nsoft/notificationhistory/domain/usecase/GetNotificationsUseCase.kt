package com.nsoft.notificationhistory.domain.usecase

import com.nsoft.notificationhistory.domain.model.AppNotification
import com.nsoft.notificationhistory.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<AppNotification>> {
        return repository.getNotifications()
    }
}