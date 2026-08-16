package com.nsoft.notificationhistory.domain.usecase

import android.content.pm.PackageManager
import com.nsoft.notificationhistory.data.util.InstalledAppsProvider
import com.nsoft.notificationhistory.domain.model.AppInfo
import com.nsoft.notificationhistory.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class GetInstalledAppsUseCase(
    private val packageManager: PackageManager,
    private val repository: NotificationRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<List<AppInfo>> = repository.getAllAppSettings().map { settings ->
        val settingsMap = settings.associateBy({ it.packageName }, { it.isEnabled })
        
        val installedApps = InstalledAppsProvider.getInstalledUserApps(packageManager)

        installedApps.map { app ->
            app.copy(isEnabled = settingsMap[app.packageName] ?: false)
        }
    }.flowOn(ioDispatcher)
}
