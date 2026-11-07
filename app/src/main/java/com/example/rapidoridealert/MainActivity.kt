package com.example.rapidoridealert

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createChannel()
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
        }
        layout.addView(TextView(this).apply {
            text = "Rapido Ride Alert Assistant"
            textSize = 24f
            gravity = Gravity.CENTER
        })
        layout.addView(TextView(this).apply {
            text = "\nRapido की नई राइड का नोटिफिकेशन मिलने पर यह ऐप अलर्ट देगा।\n\nपहले Notification Access चालू करें और Rapido Captain के नोटिफिकेशन ऑन रखें।"
            textSize = 16f
            gravity = Gravity.CENTER
        })
        layout.addView(Button(this).apply {
            text = "1. Notification Access चालू करें"
            setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        })
        layout.addView(Button(this).apply {
            text = "2. Rapido Captain खोलें"
            setOnClickListener { openRapido() }
        })
        layout.addView(Button(this).apply {
            text = "टेस्ट अलर्ट"
            setOnClickListener { RideNotificationListener.showTestAlert(this@MainActivity) }
        })
        setContentView(layout)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                RideNotificationListener.CHANNEL_ID, "Ride alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Rapido ride notification alerts"; enableVibration(true) }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun openRapido() {
        val pm = packageManager
        val app = pm.getInstalledApplications(0).firstOrNull {
            pm.getApplicationLabel(it).toString().contains("Rapido", ignoreCase = true)
        }
        val launch = app?.let { pm.getLaunchIntentForPackage(it.packageName) }
        if (launch != null) startActivity(launch)
        else startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        })
    }
}
