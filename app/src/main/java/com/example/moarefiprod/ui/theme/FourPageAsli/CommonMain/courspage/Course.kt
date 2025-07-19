package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// مدل اطلاعات کلی دوره
@Parcelize
data class Course(
    val title: String,
    val description: String,
    val sath: String,
    val zaman: String,
    val teadad: String,
    val price: Int,
    val image: Int,
    val isNew: Boolean = false,
    val isPurchased: Boolean = false, // ✅ این خط اضافه شده
    val lessons: List<CourseLesson> = emptyList()
) : Parcelable


// مدل هر آیتم داخل یک درس (مثلاً فیلم، جزوه، آزمون و...)
@Parcelize
data class CourseItem(
    val type: CourseItemType,
    val title: String,
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false
) : Parcelable


// ✅ مهمترین تغییر: اینجا @Parcelize و : Parcelable را اضافه کنید.
@Parcelize
enum class CourseItemType : Parcelable {
    VIDEO, DOCUMENT, QUIZ1, QUIZ2, QUIZ3, FINAL_EXAM, WORDS
}

// مدل هر درس (درس شماره ۱، ۲، ...)
@Parcelize
data class CourseLesson(
    val id: String,
    val title: String,
    val duration: String,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val items: List<CourseItem>
) : Parcelable