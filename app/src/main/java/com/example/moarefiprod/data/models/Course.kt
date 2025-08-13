package com.example.moarefiprod.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import com.google.firebase.firestore.PropertyName
import java.util.Date

@Keep
@Parcelize
data class Course(
    val id: String = "",
    val headerTitle: String = "",
    val title: String = "",
    val description: String = "",
    val sath: String = "",
    val zaman: String = "",
    val teadad: Int = 0,
    val price: Int = 0,
    val imageUrl: String = "",

    @get:PropertyName("isNew")
    val isNew: Boolean = false,

    @get:PropertyName("isFree")
    val isFree: Boolean = false,

    @get:PropertyName("isInProgress")
    val isInProgress: Boolean = false,

    @get:PropertyName("isCompleted")
    val isCompleted: Boolean = false,

    @get:PropertyName("isUnlocked")
    val isUnlocked: Boolean = false,

    val publishedAt: Date = Date(),

    @get:PropertyName("isPurchased")
    val isPurchased: Boolean = false,

    @PropertyName("order") val order: Int = 0,

    val lessons: List<CourseLesson> = emptyList()
) : Parcelable


@Parcelize
data class CourseLesson(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val items: List<CourseItem> = emptyList(),
    @PropertyName("isUnlocked") val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false,
    @PropertyName("order") val order: Int = 0,
    val imageUrl: String = ""
) : Parcelable

@Parcelize
data class CourseItem(
    val id: String = "",
    val type: CourseItemType = CourseItemType.VIDEO,
    val title: String = "",
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false, // تغییر به false
    val isInProgress: Boolean = false,
    val duration: Int = 0,
    val url: String = "",
    @PropertyName("order") val order: Int = 0,
    val lessonId: String? = null, // فیلد جدید برای ربط به درس
    val courseId: String? = null,
    val gameId: String? = null
) : Parcelable

enum class CourseItemType {
    VIDEO, DOCUMENT, QUIZ1, QUIZ2, QUIZ3,QUIZ_SET, FINAL_EXAM, WORDS;

    companion object {
        fun fromString(type: String): CourseItemType? {
            return values().find { it.name.equals(type, ignoreCase = true) }
        }
    }
}