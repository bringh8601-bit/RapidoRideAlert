package com.example.rapidoridealert

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class RideNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!sbn.packageName.contains("rapido", ignoreCase = true)) return
        val extras = sbn.notification.extras
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
        val body = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()
        val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString().orEmpty()
        val message = listOf(body, bigText).filter { it.isNotBlank() }.distinct().joinToString("\n")
        if (title.isBlank() && message.isBlank()) return
        showRideAlert(this, title.ifBlank { "नई Rapido सूचना" },
            message.ifBlank { "Rapido Captain ऐप में नई सूचना आई है।" }, sbn.packageName)
    }

    companion object {
        const val CHANNEL_ID = "rapido_ride_alerts"
        fun showTestAlert(context: Context) {
            showRideAlert(context, "टेस्ट अलर्ट", "आपके फोन का अलर्ट काम कर रहा है।", null)
        }
        private fun showRideAlert(context: Context, title: String, message: String, packageName: String?) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= 26 && manager.getNotificationChannel(CHANNEL_ID) == null) {
                manager.createNotificationChannel(NotificationChannel(
                    CHANNEL_ID, "Ride alerts", NotificationManager.IMPORTANCE_HIGH
                ).apply { enableVibration(true) })
            }
            val intent = packageName?.let { context.packageManager.getLaunchIntentForPackage(it) }
                ?: Intent(context, MainActivity::class.java)
            val pending = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0))
            val builder = if (Build.VERSION.SDK_INT >= 26) Notification.Builder(context, CHANNEL_ID)
                else Notification.Builder(context)
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Rapido Ride Alert: $title")
                .setContentText(message)
                .setStyle(Notification.BigTextStyle().bigText(message))
                .setContentIntent(pending).setAutoCancel(true)
                .setVibrate(longArrayOf(0, 500, 250, 500))
                .setPriority(Notification.PRIORITY_MAX)
            manager.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), builder.build())
        }
    }
}
