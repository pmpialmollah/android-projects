package com.nsoft.notificationhistory.ui.notificationHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsoft.notificationhistory.domain.model.AppNotification
import com.nsoft.notificationhistory.domain.usecase.ClearAllNotificationsUseCase
import com.nsoft.notificationhistory.domain.usecase.DeleteNotificationUseCase
import com.nsoft.notificationhistory.domain.usecase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationHistoryViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val clearAllNotificationsUseCase: ClearAllNotificationsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filterPackageName = MutableStateFlow<String?>(null)
    val filterPackageName = _filterPackageName.asStateFlow()

    private val _notifications = getNotificationsUseCase()

    val filteredNotifications: StateFlow<List<AppNotification>> = combine(
        _notifications,
        _searchQuery,
        _filterPackageName
    ) { notifications, query, packageName ->
        notifications.filter { notification ->
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                notification.title?.contains(query, ignoreCase = true) == true ||
                        notification.text?.contains(query, ignoreCase = true) == true ||
                        notification.appName.contains(query, ignoreCase = true)
            }

            val matchesPackage = if (packageName == null) {
                true
            } else {
                notification.packageName == packageName
            }

            matchesQuery && matchesPackage
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterPackageChanged(packageName: String?) {
        _filterPackageName.value = packageName
    }

    fun deleteNotification(notification: AppNotification) {
        viewModelScope.launch {
            deleteNotificationUseCase(notification)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            clearAllNotificationsUseCase()
        }
    }
}