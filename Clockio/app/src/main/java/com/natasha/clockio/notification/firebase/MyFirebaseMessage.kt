package com.natasha.clockio.notification.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.natasha.clockio.R

//https://stackoverflow.com/questions/51123197/firebaseinstanceidservice-is-deprecated
//https://www.codementor.io/@flame3/send-push-notifications-to-android-with-firebase-du10860kb
class MyFirebaseMessage: FirebaseMessagingService() {
    private val TAG: String = MyFirebaseMessage::class.java.simpleName

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "new Token: $p0")
    }

    override fun onMessageReceived(remote: RemoteMessage) {
        Log.d(TAG, "From: ${remote.from}")
        val notifBody = remote.notification?.body
        Log.d(TAG, "Notif Msg Body: $notifBody")

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "notif_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "Description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(notifBody)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)

        notificationManager.notify(1000, notificationBuilder.build())
    }
}