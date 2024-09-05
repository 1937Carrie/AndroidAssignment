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
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.MyContactsFragment.Companion.INDICATOR_CONTACT_ID
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.MyContactsFragment.Companion.INDICATOR_CONTACT_NAME

const val CHANNEL_ID = "channel_miscellaneous"
const val IS_FROM_DEEP_LINK = "isFromDeepLink"
const val NOTIFICATION_ID = 10001

private fun createNotification(
    context: Context,
    contextText: String,
    indicatorContactId: Int,
    indicatorContactName: String,
): Notification {
    val args = bundleOf(
        INDICATOR_CONTACT_ID to indicatorContactId,
        INDICATOR_CONTACT_NAME to indicatorContactName,
        IS_FROM_DEEP_LINK to true
    )
    val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.detailsFragment)
        .setArguments(args)
        .createPendingIntent()

    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(indicatorContactName)
        .setContentText(contextText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
}

fun createOnAddNotification(
    context: Context,
    indicatorContactId: Int,
    indicatorContactName: String,
): Notification {
    return createNotification(
        context,
        context.getString(R.string.contact_was_added),
        indicatorContactId,
        indicatorContactName
    )
}

fun createOnRemoveNotification(
    context: Context,
    indicatorContactId: Int,
    indicatorContactName: String,
): Notification {
    return createNotification(
        context,
        context.getString(R.string.contact_was_removed),
        indicatorContactId,
        indicatorContactName
    )
}

fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.miscellaneous)
        val descriptionText = context.getString(R.string.channel_for_miscellaneous_notifications)
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