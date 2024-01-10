package com.google.firebase.codelab.friendlychat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("MarcusFCM recived", "From: ${remoteMessage.from}")
        showTestNotification()

        remoteMessage.notification?.let {
            Log.d("MarcusFCM recived", "Message Notification calld: ")
            showNotification(it.title ?: "Title", it.body ?: "Body")
        }
    }

    override fun onNewToken(token: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let {
            FirebaseDatabase.getInstance().getReference("users")
                .child(it)
                .child("fcmToken")
                .setValue(token)
        }
    }
    fun showNotification(title: String, body: String) {//todo få att fungera för att vissa medelande it notification
        Log.d("MarcusTAG", "showNotification CALLED : $title $body")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "CHAT_CHANNEL_ID"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Chat Messages", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notifications for new chat messages"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.baseline_add_24) // Make sure this icon exists in your drawable folder
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationId = (System.currentTimeMillis() and 0xfffffff).toInt() // Use an unique ID for each notification
        notificationManager.notify(notificationId, notificationBuilder.build())

        Log.d("MarcusTAG", "Notification posted with ID: $notificationId")
    }
    fun showTestNotification() {
        Log.d("MarcusTAG", "showTestNotification CALLED")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "TEST_CHANNEL_ID"

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Notifications"
            val descriptionText = "Test Notification Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification and issue it
        val notificationId = (System.currentTimeMillis() and 0xfffffff).toInt() // Use an unique ID for each notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // Ensure you have this icon in your drawable folder
            .setContentTitle("Campus")
            .setContentText("New message from campus Flemingsberg")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(notificationId, notificationBuilder.build())
        Log.d("MarcusTAG", "Test Notification posted with ID: $notificationId")
    }


}