package koushur.kashmirievents.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Aarti Data class
 *
 * Created by: Vikesh Dass
 * Created on: 15-11-2020
 */
@Parcelize
data class AartiData(
    val titleEnglish: String,
    val titleHindi: String,
    val aartiString: String
) : Parcelable