package koushur.kashmirievents.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Video Data class
 *
 * Created by: Vikesh Dass
 * Created on: 22-11-2020
 */
@Parcelize
data class VideoData(
    val title: String,
    val videoId: String,
) : Parcelable