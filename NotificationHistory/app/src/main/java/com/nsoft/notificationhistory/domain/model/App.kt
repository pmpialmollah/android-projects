package com.nsoft.notificationhistory.domain.model

import android.graphics.drawable.Drawable

data class App(
    val packageName: String,
    val appName: String,
    val icon: Drawable?
)
