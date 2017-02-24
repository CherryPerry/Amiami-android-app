package ru.cherryperry.amiami.push

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.screen.main.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationController @Inject constructor(context: Context, private val prefs: AppPrefs) {
    companion object {
        private val notificationId = 0
    }

    private val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun reset() {
        manager.cancel(notificationId)
        prefs.pushCounter = 0
    }

    fun show(value: Int, context: Context) {
        prefs.pushCounter += prefs.pushCounter

        if (prefs.push) {
            val notifyIntent = Intent(context, MainActivity::class.java)
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_notification_icon)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.notification_counter, value))
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent).setAutoCancel(true)

            manager.notify(notificationId, builder.build())
        }
    }
}
