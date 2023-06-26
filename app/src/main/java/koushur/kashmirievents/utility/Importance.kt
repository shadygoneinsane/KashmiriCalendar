package koushur.kashmirievents.utility

import androidx.annotation.IntDef

@IntDef(Importance.high, Importance.med, Importance.low, Importance.none)
annotation class Importance {
    companion object {
        //red bold
        const val high: Int = 2

        //black bold
        const val med: Int = 1

        //blue
        const val low: Int = 0

        //brown
        const val none: Int = -1
    }
}