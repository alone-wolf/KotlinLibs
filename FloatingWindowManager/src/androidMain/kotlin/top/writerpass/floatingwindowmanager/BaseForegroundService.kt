package top.writerpass.floatingwindowmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleService

//abstract class BaseForegroundService : LifecycleService() {
//    abstract val foregroundNotificationChannelId: String
//    abstract val foregroundNotificationChannel: () -> NotificationChannel
//    abstract val foregroundNotification: () -> Notification
//    abstract val foregroundNotificationId: Int
//
//    fun startForegroundService() {
//        val notificationChannel = foregroundNotificationChannel()
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(notificationChannel)
//
//        startForeground(
//            foregroundNotificationId,
//            foregroundNotification()
//        )
//    }
//
//
//    fun String.toast(context: Context) {
//        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
//    }
//}