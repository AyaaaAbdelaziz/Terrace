package com.amuab.pergola

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amuab.pergola.notify.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        val channelId = "com.amuab.pergola"
        val notificationBuilder = NotificationCompat.Builder(context!!, channelId).apply {
            setSmallIcon(R.drawable.can)
            setContentTitle("pergola")
            setContentText("water ur plant")
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT

            val intent = Intent(context, Alarm::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }}
