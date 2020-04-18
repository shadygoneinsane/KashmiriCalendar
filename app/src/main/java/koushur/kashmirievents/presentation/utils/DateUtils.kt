package koushur.kashmirievents.presentation.utils

import java.text.SimpleDateFormat
import java.util.*


object DateUtils {
    fun getDateAsString(timestamp: Long): String {
        val date = Date(timestamp * 1000 ) //in millis
        val sdf = SimpleDateFormat("dd-MMM-yyyy HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        return sdf.format(date)
    }
}