package com.sungbin.hyunnieserver.tool.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.sungbin.hyunnieserver.R

@Suppress("DEPRECATION")
object NotificationUtil {

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val group = NotificationChannelGroup(
                context.getString(R.string.app_name),
                context.getString(R.string.app_name)
            )
            getManager(context).createNotificationChannelGroup(group)

            val channelMessage = NotificationChannel(
                context.getString(R.string.app_name),
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_description)
                setGroup(context.getString(R.string.app_name))
                enableVibration(false)
            }
            getManager(context).createNotificationChannel(channelMessage)
        }
    }

    fun getManager(context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getDownloadNotification(
        context: Context, id: Int, title: String, content: String
    ): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, context.getString(R.string.app_name)).apply {
                    setContentTitle(title)
                    setContentText(content)
                    setSmallIcon(R.mipmap.ic_launcher)
                    setAutoCancel(false)
                    setOngoing(true)
                    setProgress(100, 0, false)
                }
            //getManager(context).notify(id, builder.build())
        } else {
            Notification.Builder(context).apply {
                setContentTitle(title)
                setContentText(content)
                setSmallIcon(R.mipmap.ic_launcher)
                setAutoCancel(false)
                setOngoing(true)
                setProgress(100, 0, false)
            }
            //getManager(context).notify(id, builder.build())
        }
    }

    fun remove(context: Context, id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }

}
