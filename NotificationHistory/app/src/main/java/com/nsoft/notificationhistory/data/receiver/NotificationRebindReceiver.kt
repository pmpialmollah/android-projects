package com.nsoft.notificationhistory.data.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.nsoft.notificationhistory.data.listener.NotificationListener

class NotificationRebindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("PialNotification", "RebindReceiver triggered by: ${intent?.action}")
        rebindNotificationListener(context)
    }

    companion object {
        fun rebindNotificationListener(context: Context) {
            val componentName = ComponentName(context, NotificationListener::class.java)
            val pm = context.packageManager

            pm.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            pm.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            Log.d("PialNotification", "NotificationListener rebind toggled")
        }
    }
}
