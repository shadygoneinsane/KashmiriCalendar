package koushur.kashmirievents.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.presentation.ui.main.ActivityMain

/**
 * Utility for Alarms
 * Created by: Vikesh Dass
 * Email: vikeshdass@gmail.com
 */
object AlarmManagerUtils {

    const val alarmManagerTag = "AlarmManager"

    @Throws(SecurityException::class)
    fun startAlarm(
        context: Context, startTimeMillis: Long, savedEvent: SavedEventEntity
    ) {
        val alarmManager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = returnPIForSettingScheduledAlarm(context, returnReqCode(savedEvent))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, startTimeMillis, pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTimeMillis, pendingIntent)
        }
    }

    fun cancelAlarm(context: Context, savedEvent: SavedEventEntity) {
        val alarmManager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = returnPIForCancellingAlarm(context, returnReqCode(savedEvent))
        alarmManager.cancel(pendingIntent)
        log(
            alarmManagerTag,
            "Cancelling scheduled Alarm Manager with tag as ${returnReqCode(savedEvent)}"
        )
        pendingIntent.cancel()
    }

    private fun returnPIForSettingScheduledAlarm(context: Context, reqCode: Int): PendingIntent {
        val intent = appIntent(context)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            reqCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun returnPIForCancellingAlarm(context: Context, profileId: Int): PendingIntent {
        val intent = appIntent(context)
        return PendingIntent.getBroadcast(
            context.applicationContext, profileId, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Check for the condition if pending intent exists without periodic lock status as true and vice-versa
     *
     * @param context from activity
     */
    fun checkForPeriodicLockAlarm(context: Context, savedEvent: SavedEventEntity): Boolean {
        val intent = appIntent(context)

        return PendingIntent.getBroadcast(
            context.applicationContext,
            returnReqCode(savedEvent),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    private fun appIntent(context: Context): Intent {
        return Intent(context, ActivityMain::class.java).apply {
            action = Constants.ACTION_ALARM_RECEIVER
        }
    }

    private fun returnReqCode(savedEvent: SavedEventEntity): Int {
        return "${savedEvent.selectedDate.dayOfMonth}${savedEvent.selectedDate.month}${savedEvent.selectedDate.year}".toInt()
    }
}