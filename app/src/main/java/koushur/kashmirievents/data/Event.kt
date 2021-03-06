package koushur.kashmirievents.data

import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import java.time.LocalDate


/**
 * Data class to be used for each events
 *
 * Author: Vikesh Dass
 * Created on: 18-04-2020
 * Email : vikeshdass@gmail.com
 */
data class Event(
    val localDate: LocalDate,
    @Importance val eventImp: Int,
    val eventName: String,
    @ColorRes val color: Int
)

@IntDef(Importance.high, Importance.low)
annotation class Importance {
    companion object {
        //red bold
        const val high: Int = 2

        //black bold
        const val med: Int = 1

        //none
        const val low: Int = 0
    }
}