package koushur.kashmirievents.data

import androidx.annotation.ColorRes
import org.threeten.bp.LocalDateTime


/**
 * File Description
 * Author: Vikesh Dass
 * Created on: 18-04-2020
 */
data class Event(val time: LocalDateTime, val name: String, @ColorRes val color: Int)