package com.nsoft.notificationhistory.data.mapper

import com.nsoft.notificationhistory.data.local.entity.NotificationEntity
import com.nsoft.notificationhistory.domain.model.AppNotification

fun NotificationEntity.toDomain(): AppNotification {
    return AppNotification(
        id = id,
        packageName = packageName,
        appName = appName,
        title = title,
        text = text,
        postTime = postTime,
        isRemoved = isRemoved
    )
}

fun AppNotification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        packageName = packageName,
        appName = appName,
        title = title,
        text = text,
        postTime = postTime,
        isRemoved = isRemoved
    )
}