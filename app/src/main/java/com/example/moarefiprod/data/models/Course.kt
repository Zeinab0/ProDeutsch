// app/src/main/java/com/example/moarefiprod/data/models/Course.kt
package com.example.moarefiprod.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.Date

@Parcelize
data class Course(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val sath: String = "",
    val zaman: String = "",
    val teadad: Int = 0,
    val price: Int = 0,
    val imageUrl: String = "",
    val isNew: Boolean = false,
    val isFree: Boolean = false,
    val publishedAt: Date = Date(),
    // ✅ این فیلد را برای همگام‌سازی با CourseDetailPage اضافه می‌کنیم
    val isPurchased: Boolean = false, // یا هر منطق پیش‌فرض دیگری
    // ✅ لیست دروس باید اینجا باشد اگر به صورت Nested Object در Firestore ذخیره می‌شوند
    val lessons: List<CourseLesson> = emptyList() // این خط قبلاً بود، فقط مطمئن شوید که هست
) : Parcelable

@Parcelize
data class CourseLesson(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val items: List<CourseItem> = emptyList(),
    // ✅ این فیلدها را برای همگام‌سازی با CourseLessonItem و darspage اضافه می‌کنیم
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false
) : Parcelable

//@Parcelize
//data class CourseItem(
//    val type: String = "",
//    val title: String = "",
//    // ✅ این فیلدها را برای همگام‌سازی با darspage اضافه می‌کنیم (اگرچه از CourseLesson می‌آیند)
//    val isCompleted: Boolean = false,
//    val isInProgress: Boolean = false
//) : Parcelable

@Parcelize
data class CourseItem(
    val type: CourseItemType = CourseItemType.VIDEO, // تغییر به CourseItemType
    val title: String = "",
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false
) : Parcelable

enum class CourseItemType {
    VIDEO, DOCUMENT, QUIZ1, QUIZ2, QUIZ3, FINAL_EXAM, WORDS;

    companion object {
        fun fromString(type: String): CourseItemType? {
            return values().find { it.name.equals(type, ignoreCase = true) }
        }
    }
}