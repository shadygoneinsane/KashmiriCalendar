package koushur.kashmirievents

import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.utility.Importance
import koushur.kashmirievents.utility.getDayEventImportance
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ViewUtilsTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSetTVFontColor() {
        val a = getDayEventImportance(DayEvent(-1, LocalDate.now(), "Bhimsen Ekadashi", Importance.none))
        assertEquals(Importance.high, a)
        val b = getDayEventImportance(DayEvent(-1, LocalDate.now(), "Bhimsen ", Importance.none))
        assertEquals(Importance.none, b)
        val c = getDayEventImportance(DayEvent(1, LocalDate.now(), "Bhimsen ", Importance.low))
        assertEquals(Importance.low, c)
        val d = getDayEventImportance(DayEvent(1, LocalDate.now(), "Ekadashi", Importance.low))
        assertEquals(Importance.med, d)
    }
}
