package koushur.kashmirievents.utility


/**
 * File Description
 * Created by: Vikesh Dass
 * Created on: 23-01-2021
 * Email : vikeshdass@gmail.com
 */
object Constants {

    //Firebase Service
    const val FIREBASE_NOTIFICATION_ID = 1121
    const val FIREBASE_CHANNEL_ID = "KOUSHUR_CALENDAR_FIREBASE"
    const val FIREBASE_CHANNEL_NAME = "Calendar Notifications"

    const val LOGS_FOLDER = "/CalendarLogs"
    const val LOG_FILE = "CALENDAR_LOGS.txt"
    const val DEBUG_LOG_FILE = "DEBUG_CALENDAR_LOGS.txt"
    const val EXCEPTION = "KOUSHUR_EXCEPTION"
    const val LOGGER = "KOUSHUR_LOG"

    //Extras
    const val EXTRA_DATE = "DATE"
    const val EXTRA_MONTH_INDEX = "MONTH_INDEX"
    const val EXTRA_MONTH_NAME = "MONTH_NAME"
    const val EXTRA_DAY_INDEX = "DAY_INDEX"
    const val EXTRA_DAY_NAME = "DAY_NAME"

    const val WAKE_LOCK_BOOT_TAG = "keepmeout:WakeLockBootTag"
    const val wakeLockTimeout = 20L * 1000L
    const val thresholdBroadcastTimeout = 9L * 1000L + 500L
    const val ACTION_ALARM_RECEIVER = "ACTION_ALARM_RECEIVER"
}