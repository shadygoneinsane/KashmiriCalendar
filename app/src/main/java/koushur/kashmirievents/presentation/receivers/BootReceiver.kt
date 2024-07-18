package koushur.kashmirievents.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import koushur.kashmirievents.repository.CalendarRepository
import koushur.kashmirievents.utility.Constants
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import vikesh.dass.lockmeout.utility.powermanager.PowerManager

/**
 * This class runs after device restart and checks if any lock is currently running
 */
class BootReceiver : BroadcastReceiver(), KoinComponent {
    private val tag = "BootReceiver"
    private lateinit var powerManager: PowerManager

    private val repository: CalendarRepository by inject()


    override fun onReceive(context: Context, intent: Intent) {
        acquireWakeLock(
            context,
            Constants.WAKE_LOCK_BOOT_TAG,
            Constants.thresholdBroadcastTimeout
        )

        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
            GlobalScope.launch {
                repository.fetchAllEvents().collect {

                }
            }
        }
    }


    private fun onFinish() {
        releaseResources()
    }

    /**
     * Release resources
     */
    private fun releaseResources() {
        powerManager.release()
    }

    private fun acquireWakeLock(
        context: Context,
        tag: String,
        wakeLockTimeout: Long = Constants.wakeLockTimeout
    ) {
        powerManager = PowerManager(context)
        powerManager.acquire(wakeLockTimeout, tag)
    }
}