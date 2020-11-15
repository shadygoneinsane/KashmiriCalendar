package koushur.kashmirievents.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Aarti Data class
 * Created by: Vikesh Dass
 * Created on: 15-11-2020
 */
@Parcelize
data class Aarti(
    val titleEnglish: String,
    val titleHindi: String,
    val aartiString: String
) : Parcelable