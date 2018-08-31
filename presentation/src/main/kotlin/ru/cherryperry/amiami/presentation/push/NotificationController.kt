package ru.cherryperry.amiami.presentation.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.notifications.IncreaseNotificationItemCounterUseCase
import ru.cherryperry.amiami.domain.notifications.ResetNotificationItemCounterUseCase
import ru.cherryperry.amiami.presentation.activity.SingleActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationController @Inject constructor(
    private val context: Context,
    private val increaseNotificationItemCounterUseCase: IncreaseNotificationItemCounterUseCase,
    private val resetNotificationItemCounterUseCase: ResetNotificationItemCounterUseCase
) {
    companion object {
        private const val notificationId = 0
    }

    private val notificationChannelId: String = context.getString(R.string.notification_channel_id)
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createOrUpdateNotificationChannel()
    }

    fun reset() {
        notificationManager.cancel(notificationId)
        resetNotificationItemCounterUseCase.run(Unit).subscribe()
    }

    fun show(value: Int, context: Context) {
        increaseNotificationItemCounterUseCase.run(value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val notifyIntent = Intent(context, SingleActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                createOrUpdateNotificationChannel()
                val builder = NotificationCompat.Builder(context, notificationChannelId)
                    .setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.resources.getQuantityString(R.plurals.notification_counter, value, value))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                notificationManager.notify(notificationId, builder.build())
            }
    }

    private fun createOrUpdateNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val channel = NotificationChannel(
            notificationChannelId,
            context.getString(R.string.notification_channel_title),
            NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }
}
