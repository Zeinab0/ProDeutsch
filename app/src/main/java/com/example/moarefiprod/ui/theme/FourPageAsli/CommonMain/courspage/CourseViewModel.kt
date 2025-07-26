package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moarefiprod.data.FirestoreRepository
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseLesson // ایمپورت CourseLesson
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

    // ✅ تابع اصلاح شده برای بارگذاری جزئیات یک دوره خاص و دروس آن
//    fun loadSelectedCourseDetailsAndLessons(courseId: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                // 1. بارگذاری جزئیات دوره
//                val course = repository.getCourseDetails(courseId)
//                _selectedCourse.value = course
//
//                // 2. بارگذاری دروس مربوط به این دوره
//                if (course != null) {
//                    val lessons = repository.getLessonsForCourse(courseId)
//                    _selectedCourseLessons.value = lessons
//                } else {
//                    _selectedCourseLessons.value = emptyList() // اگر دوره پیدا نشد، لیست دروس خالی باشد
//                }
//
//            } catch (e: Exception) {
//                _errorMessage.value = "خطا در بارگذاری جزئیات دوره و دروس آن برای $courseId: ${e.localizedMessage}"
//                _selectedCourse.value = null // در صورت خطا، حالت را ریست کنید
//                _selectedCourseLessons.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
    fun loadSelectedCourseDetailsAndLessons(courseId: String) {
        viewModelScope.launch {
            try {
                val courseDoc = db.collection("Courses").document(courseId).get().await().toObject(Course::class.java)
                _selectedCourse.value = courseDoc
                Log.d("CourseViewModel", "Course loaded: $courseDoc")
                val lessonDocs = db.collection("Courses").document(courseId).collection("Lessons").get().await().toObjects(CourseLesson::class.java)
                _selectedCourseLessons.value = lessonDocs
                Log.d("CourseViewModel", "Lessons loaded: $lessonDocs")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("CourseViewModel", "Error loading data: ${e.message}")
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
                    .collection("Contents") // اسم رو با دیتابیس (Contents) همگام کن
                    .get()
                    .await()
                    .toObjects(CourseItem::class.java)
                // باید یه StateFlow برای lessonItems داشته باشی، مثلاً:
                // _selectedLessonItems.value = itemDocs
                Log.d("CourseViewModel", "Contents loaded: $itemDocs")
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading Contents: ${e.message}")
            }
        }
    }
    // ✅ تابع loadLessonItems را در اینجا، خارج از تابع loadSelectedCourseDetailsAndLessons قرار دهید
//    fun loadLessonItems(courseId: String, lessonId: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                val items = repository.getCourseItemsForLesson(courseId, lessonId)
//                _selectedLessonItems.value = items
//            } catch (e: Exception) {
//                _errorMessage.value = "خطا در بارگذاری آیتم‌های درس $lessonId در دوره $courseId: ${e.localizedMessage}"
//                _selectedLessonItems.value = emptyList() // در صورت خطا، لیست را خالی کنید
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//    fun loadLessonItems(courseId: String, lessonId: String) {
//        viewModelScope.launch {
//            try {
//                val itemDocs = db.collection("Courses")
//                    .document(courseId)
//                    .collection("Lessons")
//                    .document(lessonId)
//                    .collection("Contents")
//                    .get()
//                    .await()
//                    .toObjects(CourseItem::class.java)
//                // اینجا باید یه StateFlow برای lessonItems داشته باشی
//                Log.d("CourseViewModel", "Lesson items loaded: $itemDocs")
//            } catch (e: Exception) {
//                Log.e("CourseViewModel", "Error loading items: ${e.message}")
//            }
//        }
//    }
}