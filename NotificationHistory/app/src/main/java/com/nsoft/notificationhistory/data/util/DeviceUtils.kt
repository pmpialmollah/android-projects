package com.nsoft.notificationhistory.data.util

import android.os.Build

object DeviceUtils {

    fun isMiui(): Boolean {
        return !getSystemProperty("ro.miui.ui.version.name").isNullOrEmpty()
    }

    fun manufacturer(): String = Build.MANUFACTURER.lowercase()

    private fun getSystemProperty(key: String): String? {
        return try {
            val process = Runtime.getRuntime().exec("getprop $key")
            process.inputStream.bufferedReader().readLine()
        } catch (e: Exception) {
            null
        }
    }
}
