package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moarefiprod.repository.FirestoreRepository
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseLesson
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourseViewModel(
    private val repository: FirestoreRepository = FirestoreRepository()
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    // State برای نگهداری لیست همه دوره‌ها
    private val _allCourses = MutableStateFlow<List<Course>>(emptyList())
    val allCourses: StateFlow<List<Course>> = _allCourses.asStateFlow()

    // State برای نگهداری لیست دوره‌های رایگان
    private val _freeCourses = MutableStateFlow<List<Course>>(emptyList())
    val freeCourses: StateFlow<List<Course>> = _freeCourses.asStateFlow()

    // State برای نگهداری لیست دوره‌های جدید
    private val _newCourses = MutableStateFlow<List<Course>>(emptyList())
    val newCourses: StateFlow<List<Course>> = _newCourses.asStateFlow()

    // ✅ State جدید: برای نگهداری جزئیات دوره فعلی که کاربر روی آن کلیک کرده است
    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    // ✅ State جدید: برای نگهداری لیست دروس دوره انتخاب شده
    private val _selectedCourseLessons = MutableStateFlow<List<CourseLesson>>(emptyList())
    val selectedCourseLessons: StateFlow<List<CourseLesson>> = _selectedCourseLessons.asStateFlow()

    private val _selectedLessonItems = MutableStateFlow<List<CourseItem>>(emptyList())
    val selectedLessonItems: StateFlow<List<CourseItem>> = _selectedLessonItems.asStateFlow()

    // State برای مدیریت وضعیت بارگذاری (مثلاً نمایش Progress Bar)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State برای مدیریت پیام‌های خطا
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        loadAllCourses()
        loadFreeCourses()
        loadNewCourses()
    }

    fun loadAllCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val courses = repository.getAllCourses()
                _allCourses.value = courses
            } catch (e: Exception) {
                _errorMessage.value = "خطا در بارگذاری همه دوره‌ها: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadFreeCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val courses = repository.getFreeCourses()
                _freeCourses.value = courses
            } catch (e: Exception) {
                _errorMessage.value = "خطا در بارگذاری دوره‌های رایگان: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNewCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val courses = repository.getNewCourses()
                _newCourses.value = courses
            } catch (e: Exception) {
                _errorMessage.value = "خطا در بارگذاری دوره‌های جدید: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ تابع اصلاح شده برای بارگذاری جزئیات یک دوره خاص و دروس آن با زیربخش‌ها
    fun loadSelectedCourseDetailsAndLessons(courseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // 1. بارگذاری جزئیات دوره
                val courseDoc = db.collection("Courses").document(courseId).get().await().toObject(Course::class.java)
                _selectedCourse.value = courseDoc
                Log.d("CourseViewModel", "Course loaded: $courseDoc")

                // 2. بارگذاری دروس مربوط به این دوره
                val lessonDocs = db.collection("Courses").document(courseId).collection("Lessons").get().await().toObjects(CourseLesson::class.java)
                Log.d("CourseViewModel", "Lessons loaded: $lessonDocs")

                // 3. بارگذاری زیربخش‌ها از Contents و مپ کردن به درس‌ها
                val contentDocs = db.collection("Courses").document(courseId).collection("Lessons").get().await().toObjects(CourseLesson::class.java)
                    .flatMap { lesson ->
                        db.collection("Courses").document(courseId).collection("Lessons").document(lesson.id).collection("Contents")
                            .get().await().toObjects(CourseItem::class.java).map { it.copy(lessonId = lesson.id) }
                    }
                Log.d("CourseViewModel", "Contents loaded: $contentDocs")

                // 4. مپ کردن زیربخش‌ها به درس‌ها
                val lessonsWithItems = lessonDocs.map { lesson ->
                    lesson.copy(items = contentDocs.filter { it.lessonId == lesson.id })
                }
                _selectedCourseLessons.value = lessonsWithItems
                Log.d("CourseViewModel", "Lessons with items: $lessonsWithItems")

            } catch (e: Exception) {
                _errorMessage.value = "خطا در بارگذاری جزئیات دوره و دروس آن برای $courseId: ${e.localizedMessage}"
                _selectedCourse.value = null
                _selectedCourseLessons.value = emptyList()
                Log.e("CourseViewModel", "Error loading data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLessonItems(courseId: String, lessonId: String) {
        viewModelScope.launch {
            try {
                val itemDocs = db.collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .get()
                    .await()
                val items = itemDocs.map { doc ->
                    doc.toObject(CourseItem::class.java).copy(
                        id = doc.id,
                        gameId = doc.getString("gameId"),
                        lessonId = lessonId,
                        courseId = courseId
                    )
                }

                _selectedLessonItems.value = items // به‌روزرسانی StateFlow
                Log.d("CourseViewModel", "Contents loaded for lesson $lessonId: $items")
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading Contents for lesson $lessonId: ${e.message}")
                _errorMessage.value = e.message // اگه StateFlow برای خطا داری، اینو فعال کن
            }
        }
    }
}