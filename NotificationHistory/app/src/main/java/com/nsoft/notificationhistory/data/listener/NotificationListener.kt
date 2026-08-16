package com.nsoft.notificationhistory.data.listener

import android.app.Notification
import android.content.ComponentName
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.nsoft.notificationhistory.MyApplication
import com.nsoft.notificationhistory.domain.model.AppNotification
import com.nsoft.notificationhistory.domain.usecase.SaveNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {

    lateinit var saveNotificationUseCase: SaveNotificationUseCase
    lateinit var applicationScope: CoroutineScope

    private val TAG = "PialNotification"

    override fun onCreate() {
        super.onCreate()
        val appContainer = (application as MyApplication).appContainer
        saveNotificationUseCase = appContainer.saveNotificationUseCase
        applicationScope = appContainer.applicationScope
        Log.d(TAG, "NotificationListener Service Created")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "NotificationListener Connected successfully!")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "NotificationListener Disconnected! Attempting to rebind...")
        rebindService()
    }


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        Log.d(TAG, "onNotificationPosted: ($sbn)")
        
        if (sbn == null) return

        if (!::saveNotificationUseCase.isInitialized) {
            Log.e(TAG, "saveNotificationUseCase not yet initialized, skipping notification")
            return
        }

        val packageName = sbn.packageName
        if (packageName.isNullOrEmpty()) return

        applicationScope.launch(Dispatchers.IO) {
            try {
                val notificationObject = sbn.notification ?: return@launch

                val extras = try {
                    notificationObject.extras
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to unbundle notification extras from $packageName: ${e.message}")
                    null
                }

                val title = extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString()
                val text = extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString()
                val postTime = sbn.postTime

                Log.d(TAG, "onNotificationPosted: $title")

                if (title.isNullOrEmpty() && text.isNullOrEmpty()) {
                    return@launch
                }

                val appName = try {
                    packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(packageName, 0)
                    ).toString()
                } catch (e: Exception) {
                    packageName
                }

                val notification = AppNotification(
                    packageName = packageName,
                    appName = appName,
                    title = title,
                    text = text,
                    postTime = postTime
                )

                saveNotificationUseCase(notification)
                Log.d(TAG, "Notification processed for $packageName: $title")
            } catch (e: Exception) {
                Log.e(TAG, "Error processing notification from $packageName", e)
            }
        }
    }

    private fun rebindService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestRebind(ComponentName(this, NotificationListener::class.java))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "NotificationListener Service Destroyed")
    }
}
