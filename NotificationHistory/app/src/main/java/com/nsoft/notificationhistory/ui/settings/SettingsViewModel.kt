package com.nsoft.notificationhistory.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsoft.notificationhistory.domain.model.AppInfo
import com.nsoft.notificationhistory.domain.usecase.GetInstalledAppsUseCase
import com.nsoft.notificationhistory.domain.usecase.ToggleAppSettingUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getInstalledAppsUseCase: GetInstalledAppsUseCase,
    private val toggleAppSettingUseCase: ToggleAppSettingUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val apps: StateFlow<List<AppInfo>> = getInstalledAppsUseCase()
        .onEach { _isLoading.value = false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleAppSetting(packageName: String, isEnabled: Boolean) {
        viewModelScope.launch {
            toggleAppSettingUseCase(packageName, isEnabled)
        }
    }
}
