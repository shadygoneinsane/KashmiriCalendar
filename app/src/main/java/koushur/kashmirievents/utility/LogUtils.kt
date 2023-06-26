package koushur.kashmirievents.utility

import android.content.Context
import android.os.Environment
import koushir.kashmirievents.BuildConfig
import koushur.kashmirievents.KashmiriEventsApplication
import koushur.kashmirievents.utility.Constants.LOGGER
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

/**
 * For Logging
 *
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 */
fun log(text: String, writeToText: Boolean = true) {
    Timber.tag(LOGGER).d(text.formatLogText(LOGGER))
    if (writeToText) text.formatLogText().writeLogsToTextFile()
}

fun log(tag: String, text: String, writeToText: Boolean = true) {
    Timber.tag(tag).d(text.formatLogText())
    if (writeToText) text.formatLogText(tag).writeLogsToTextFile()
}

const val logFormatter: String = "%s | %s"

fun logException(tag: String, msg: String, writeToText: Boolean = true) {
    Timber.tag(tag).e(logFormatter, tag, msg.formatLogText())
    if (writeToText) msg.formatLogText(tag).writeLogsToTextFile()
}

fun logException(tag: String, ex: Exception, writeToText: Boolean = true) {
    Timber.tag(tag).e(logFormatter, tag, ex.localizedMessage?.formatLogText())
    if (writeToText) (ex.localizedMessage ?: "exception").formatLogText(tag).writeLogsToTextFile()
}

fun logException(msg: String, writeToText: Boolean = true) {
    Timber.tag(Constants.EXCEPTION).e(msg.formatLogText())
    if (writeToText) msg.formatLogText().writeLogsToTextFile()
}

fun String.writeLogsToTextFile() {
    writeStringToTextFile(this, KashmiriEventsApplication.applicationContext())
}

private fun writeStringToTextFile(text: String, mContext: Context) {
    //write logs only if user enables logs
    //if (SharedPrefUtils.getBooleanPreference(mContext, Constants.IS_LOGS_ENABLED)) {
    try {
        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(sdCard.absolutePath + Constants.LOGS_FOLDER)
        dir.mkdirs()
        val file = File(
            dir,
            if (BuildConfig.DEBUG)
                Constants.DEBUG_LOG_FILE
            else Constants.LOG_FILE
        )
        if (!file.exists()) {
            file.parentFile?.mkdirs()

            file.createNewFile()
        }
        val f1 = FileOutputStream(file, true)
        val p = PrintStream(f1)
        p.print(text + System.getProperty("line.separator"))
        p.close()
        f1.close()
    } catch (e: Exception) {
        //Timber.tag("LogUtils").e(e)
    }
    //}
}

const val dash =
    "============================================================================================="


fun String.formatLogText(tag: String = ""): String {
    return ":: $tag :: at ${getCurrentDebugTime()} :: \n$dash\n$this\n$dash\n"
}