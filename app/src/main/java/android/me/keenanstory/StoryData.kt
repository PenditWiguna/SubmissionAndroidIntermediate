package android.me.keenanstory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryData(
    val id: String,
    val userName: String,
    val avtUrl: String,
    val desc: String,
): Parcelable
