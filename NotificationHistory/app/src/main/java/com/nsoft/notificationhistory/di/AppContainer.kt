package com.nsoft.notificationhistory.di

import android.content.Context
import androidx.room.Room
import com.nsoft.notificationhistory.data.local.AppDatabase
import com.nsoft.notificationhistory.data.repository.NotificationRepositoryImpl
import com.nsoft.notificationhistory.domain.repository.NotificationRepository
import com.nsoft.notificationhistory.domain.usecase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AppContainer(private val context: Context) {

    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "notification_history_db"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    private val notificationDao by lazy { database.notificationDao() }

    val notificationRepository: NotificationRepository by lazy {
        NotificationRepositoryImpl(notificationDao)
    }

    val saveNotificationUseCase by lazy { SaveNotificationUseCase(notificationRepository) }
    val getNotificationsUseCase by lazy { GetNotificationsUseCase(notificationRepository) }
    val deleteNotificationUseCase by lazy { DeleteNotificationUseCase(notificationRepository) }
    val clearAllNotificationsUseCase by lazy { ClearAllNotificationsUseCase(notificationRepository) }
    val getInstalledAppsUseCase by lazy { GetInstalledAppsUseCase(context.packageManager, notificationRepository, ioDispatcher) }
    val toggleAppSettingUseCase by lazy { ToggleAppSettingUseCase(notificationRepository) }
}
