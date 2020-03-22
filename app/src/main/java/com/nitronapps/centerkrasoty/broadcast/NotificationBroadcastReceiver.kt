package com.nitronapps.centerkrasoty.broadcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import androidx.core.app.NotificationManagerCompat
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.utils.NOTIFICATION_CHANNEL_ID
import com.nitronapps.centerkrasoty.utils.withFirstUpperLetter
import kotlin.jvm.internal.Intrinsics


class NotificationBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getBooleanExtra("isOrder", false)) {
            createNotificationChannel(context)
            val stringExtra = intent.getStringExtra("serviceName")
            val serviceName: String = stringExtra!!.withFirstUpperLetter()

            val builder = if (VERSION.SDK_INT >= 26) {
                Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            } else {
                Notification.Builder(context)
            }

            builder.setContentTitle(context.getString(R.string.app_name_ru))
            val string: String = context.getString(R.string.youHaveAnOrder)

            val sb = StringBuilder()
            sb.append(string)
            sb.append(' ')
            sb.append(serviceName)

            builder
                .setContentText(sb.toString())
                .setSmallIcon(R.mipmap.ic_launcher)



            NotificationManagerCompat.from(context).notify(0, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (VERSION.SDK_INT >= 26) {
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = ""

            val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
        }
    }
}