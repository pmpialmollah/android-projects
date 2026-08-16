package com.nsoft.notificationhistory.domain.usecase

import com.nsoft.notificationhistory.domain.repository.NotificationRepository

class ToggleAppSettingUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(packageName: String, isEnabled: Boolean) {
        repository.toggleAppSetting(packageName, isEnabled)
    }
}
