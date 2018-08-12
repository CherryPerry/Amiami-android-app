package ru.cherryperry.amiami.presentation.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.presentation.main.MainFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationController @Inject constructor(
    // TODO Inject repository!
    private val context: Context,
    private val prefs: AppPrefs
) {
    companion object {
        private const val notificationId = 0
    }

    private val notificationChannelId: String = context.getString(R.string.notification_channel_id)
    private val notificationManager: NotificationManager? =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    init {
        createOrUpdateNotificationChannel()
    }

    fun reset() {
        notificationManager?.cancel(notificationId)
        prefs.pushCounter.value = 0
    }

    fun show(value: Int, context: Context) {
        notificationManager?.apply {
            if (!prefs.push.value) {
                return
            }
            prefs.pushCounter.value += value
            val notifyIntent = Intent(context, MainFragment::class.java)
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (hasNotificationChannel()) {
                val builder = NotificationCompat.Builder(context, notificationChannelId)
                    .setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.resources.getQuantityString(R.plurals.notification_counter, value, value))
                    .setContentIntent(pendingIntent).setAutoCancel(true)
                notify(notificationId, builder.build())
            }
        }
    }

    private fun createOrUpdateNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        notificationManager?.apply {
            val channel = NotificationChannel(notificationChannelId,
                context.getString(R.string.notification_channel_title),
                NotificationManager.IMPORTANCE_LOW)
            createNotificationChannel(channel)
        }
    }

    private fun hasNotificationChannel(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
            notificationManager?.getNotificationChannel(notificationChannelId) != null
    }
}
