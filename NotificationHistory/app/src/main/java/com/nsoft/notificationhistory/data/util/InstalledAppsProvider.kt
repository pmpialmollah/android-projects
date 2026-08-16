package com.nsoft.notificationhistory.data.util

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.nsoft.notificationhistory.domain.model.AppInfo

object InstalledAppsProvider {

    /**
     * Returns installed apps excluding pure system apps.
     * Updated system apps (e.g. Gmail, Play Store) are kept since they can send notifications.
     */
    fun getInstalledUserApps(packageManager: PackageManager): List<AppInfo> {
        val allApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        return allApps
            .filter { appInfo -> isUserFacingApp(appInfo) }
            .map { appInfo ->
                AppInfo(
                    packageName = appInfo.packageName,
                    appName = packageManager.getApplicationLabel(appInfo).toString(),
                    icon = packageManager.getApplicationIcon(appInfo)
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    private fun isUserFacingApp(appInfo: ApplicationInfo): Boolean {
        val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val isUpdatedSystemApp = (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
        // Keep non-system apps AND system apps that have been user-updated
        return !isSystemApp || isUpdatedSystemApp
    }
}
