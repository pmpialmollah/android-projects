package com.nsoft.notificationhistory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingEntity(
    @PrimaryKey val packageName: String,
    val isEnabled: Boolean
)
