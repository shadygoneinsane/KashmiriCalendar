package koushur.kashmirievents.firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import koushur.kashmirievents.presentation.ui.main.ActivityMain
import koushur.kashmirievents.utility.Constants
import koushur.kashmirievents.utility.NotificationCreator

/**
 * For receiving firebase messages
 * Created by: Vikesh Dass
 * Created on: 23-01-2021
 * Email : vikeshdass@gmail.com
 * Company : Adventovate
 */
class KCFirebaseService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        remoteMessage.notification?.let { remoteMessageNotification ->
            // Check if message contains a notification payload.
            sendNotification(remoteMessageNotification.title, remoteMessageNotification.body)
            return
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     *
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {
        FirebaseCrashlytics.getInstance().setUserId(token)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageTitle: String?, messageBody: String?) {
        val intent = Intent(this, ActivityMain::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 1210, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationCreator = NotificationCreator(
            context = this, channelId = Constants.FIREBASE_CHANNEL_ID,
            pendingIntent = pendingIntent, notificationId = Constants.FIREBASE_NOTIFICATION_ID
        )
        val notification = notificationCreator.createNotification(
            messageBody = messageBody,
            messageTitle = messageTitle
        )
        notificationCreator.notify(
            notification, importance = NotificationManager.IMPORTANCE_DEFAULT,
            channelName = Constants.FIREBASE_CHANNEL_NAME,
            enableVibration = true
        )
    }
}