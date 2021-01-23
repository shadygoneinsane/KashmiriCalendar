package koushur.kashmirievents.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import koushir.kashmirievents.R

/**
 * File Description
 * Created by: Vikesh Dass
 * Created on: 18-01-2021
 * Email : vikeshdass@gmail.com
 * Company : Adventovate
 */
class NotificationCreator(
    private val context: Context, private val pendingIntent: PendingIntent? = null,
    private val notificationId: Int, private val channelId: String
) {

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    fun createNotification(
        ongoing: Boolean = false, autoCancel: Boolean = true, enableSound: Boolean = true,
        messageTitle: String? = null, messageBody: String? = null
    ): Notification {
        val defaultSoundUri =
            if (enableSound) RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) else null
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(ongoing)
            .setAutoCancel(autoCancel)
            .setSound(defaultSoundUri)
            .setShowWhen(false)

        messageBody?.let {
            notificationBuilder.setContentText(messageBody)
        }

        messageTitle?.let {
            notificationBuilder.setContentTitle(it)
        }

        pendingIntent?.let {
            notificationBuilder.setContentIntent(it)
        }

        return notificationBuilder.build()
    }

    fun notify(
        notification: Notification, importance: Int = 0, channelName: String,
        enableVibration: Boolean = true
    ) {
        val notificationManager = createChannel(importance, channelName)
        notificationManager.notify(notificationId, notification)
    }

    private fun createChannel(
        importance: Int = 0, channelName: String, enableVibration: Boolean = true
    ): NotificationManager {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        return notificationManager
    }
}