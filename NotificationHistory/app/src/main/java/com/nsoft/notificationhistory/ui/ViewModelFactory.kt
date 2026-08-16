package com.nsoft.notificationhistory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nsoft.notificationhistory.di.AppContainer
import com.nsoft.notificationhistory.ui.notificationHistory.NotificationHistoryViewModel
import com.nsoft.notificationhistory.ui.settings.SettingsViewModel

class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NotificationHistoryViewModel::class.java) -> {
                NotificationHistoryViewModel(
                    appContainer.getNotificationsUseCase,
                    appContainer.deleteNotificationUseCase,
                    appContainer.clearAllNotificationsUseCase
                ) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(
                    appContainer.getInstalledAppsUseCase,
                    appContainer.toggleAppSettingUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
