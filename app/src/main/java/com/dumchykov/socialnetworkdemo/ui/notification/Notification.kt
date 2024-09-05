package com.dumchykov.socialnetworkdemo.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.dumchykov.socialnetworkdemo.R

private fun createNotification(
    context: Context,
    title: String,
    contextText: String,
): Notification {
    val args = bundleOf(
        "isFromDeepLink" to true
    )
    val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.detailsFragment)
        .setArguments(args)
        .createPendingIntent()

    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(contextText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
}

fun createOnAddNotification(context: Context, userName: String): Notification {
    return createNotification(
        context,
        userName,
        "Contact was added"
    )
}

fun createOnRemoveNotification(context: Context, userName: String): Notification {
    return createNotification(
        context,
        userName,
        "Contact was removed"
    )
}

fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Channel name"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

const val CHANNEL_ID = "Miscellaneous"
const val NOTIFICATION_ID = 10001