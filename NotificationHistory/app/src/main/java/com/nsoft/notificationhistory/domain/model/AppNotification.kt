package com.nsoft.notificationhistory.domain.model

data class AppNotification(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val title: String?,
    val text: String?,
    val postTime: Long,
    val isRemoved: Boolean = false
)