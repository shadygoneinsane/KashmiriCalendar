package vikesh.dass.lockmeout.utility.powermanager

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import koushur.kashmirievents.utility.log

/**
 * Utility class for PowerManager
 * Email : vikeshdass@gmail.com
 */
class PowerManager(val context: Context, private val printLogs: Boolean? = true) {
    private var wakeLock: WakeLock? = null
    private var powerManager: PowerManager? = null
    private val logTag = "PowerManager"

    init {
        try {
            powerManager =
                context.applicationContext.getSystemService(Context.POWER_SERVICE) as? PowerManager
        } catch (ex: Exception) {
            log(logTag, "Exception while creating power manager : ${ex.localizedMessage}")
        }
    }

    fun acquire(timeout: Long, tag: String) {
        try {
            wakeLock = powerManager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag)
            wakeLock?.acquire(timeout)

        } catch (ex: Exception) {
            log(logTag, "Exception while getting wake lock : ${ex.localizedMessage}")
        }
    }

    fun release() {
        log(
            logTag,
            "While Releasing wake lock, the held status was ${wakeLock?.isHeld}",
            printLogs ?: true
        )
        if (wakeLock?.isHeld == true)
            wakeLock?.release()
    }
}