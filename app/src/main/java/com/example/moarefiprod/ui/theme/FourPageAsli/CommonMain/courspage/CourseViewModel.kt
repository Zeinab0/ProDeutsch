package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moarefiprod.repository.FirestoreRepository
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseLesson
import com.google.firebase.auth.FirebaseAuth
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
    private val auth = FirebaseAuth.getInstance()

    // State برای نگهداری لیست همه دوره‌ها
    private val _allCourses = MutableStateFlow<List<Course>>(emptyList())
    val allCourses: StateFlow<List<Course>> = _allCourses.asStateFlow()

    // State برای نگهداری لیست دوره‌های رایگان
    private val _freeCourses = MutableStateFlow<List<Course>>(emptyList())
    val freeCourses: StateFlow<List<Course>> = _freeCourses.asStateFlow()

    // State برای نگهداری لیست دوره‌های جدید
    private val _newCourses = MutableStateFlow<List<Course>>(emptyList())
    val newCourses: StateFlow<List<Course>> = _newCourses.asStateFlow()

    // State برای نگهداری جزئیات دوره فعلی
    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    // State برای نگهداری لیست دروس دوره انتخاب شده
    private val _selectedCourseLessons = MutableStateFlow<List<CourseLesson>>(emptyList())
    val selectedCourseLessons: StateFlow<List<CourseLesson>> = _selectedCourseLessons.asStateFlow()

    private val _selectedLessonItems = MutableStateFlow<List<CourseItem>>(emptyList())
    val selectedLessonItems: StateFlow<List<CourseItem>> = _selectedLessonItems.asStateFlow()

    // State برای مدیریت وضعیت بارگذاری
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State برای مدیریت پیام‌های خطا
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // دوره‌های شخصی کاربر (اضافه شده/خریداری شده)
    private val _myCourses = MutableStateFlow<List<Course>>(emptyList())
    val myCourses: StateFlow<List<Course>> = _myCourses.asStateFlow()

    // آی‌دی دوره‌های خریداری‌شده
    private val _purchasedCourseIds = MutableStateFlow<Set<String>>(emptySet())
    val purchasedCourseIds: StateFlow<Set<String>> = _purchasedCourseIds.asStateFlow()

    init {
        loadAllCourses()
        loadFreeCourses()
        loadNewCourses()
        listenMyCourses()
        listenPurchasedCourses()
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

                // 3. بارگذاری زیربخش‌ها
                val contentDocs = lessonDocs.flatMap { lesson ->
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
                _errorMessage.value = "خطا در بارگذاری جزئیات دوره: ${e.localizedMessage}"
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

                _selectedLessonItems.value = items
                Log.d("CourseViewModel", "Contents loaded for lesson $lessonId: $items")
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading Contents: ${e.message}")
                _errorMessage.value = e.message
            }
        }
    }

    // --- متدهای مدیریت دوره‌های شخصی کاربر ---
    private fun listenMyCourses() {
        val uid = auth.currentUser?.uid ?: run {
            _myCourses.value = emptyList()
            return
        }

        db.collection("users").document(uid).collection("purchased_courses")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("CourseViewModel", "Error listening to my courses", e)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Course(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            price = doc.getLong("price")?.toInt() ?: 0, // ✅ اصلاح شد
                            imageUrl = doc.getString("imageUrl") ?: "", // ✅ اصلاح شد
                            isNew = doc.getBoolean("isNew") ?: false,   // ✅ اصلاح شد
                            isPurchased = doc.getBoolean("isPurchased") ?: false // ✅ اصلاح شد
                            // سایر فیلدهای مدل Course رو اینجا اضافه کن
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                _myCourses.value = list
                Log.d("CourseViewModel", "My courses updated: ${list.size} items")
            }
    }

    private fun listenPurchasedCourses() {
        val uid = auth.currentUser?.uid ?: run {
            _purchasedCourseIds.value = emptySet()
            return
        }

        db.collection("users").document(uid).collection("purchased_courses")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("CourseViewModel", "Error listening to purchased courses", e)
                    return@addSnapshotListener
                }

                val ids = snapshot?.documents?.map { it.id }?.toSet() ?: emptySet()
                _purchasedCourseIds.value = ids
                Log.d("CourseViewModel", "Purchased IDs updated: ${ids.size} items")
            }
    }

    fun addCourseToMyList(course: Course, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return

        val data = mapOf(
            "title" to course.title,
            "description" to course.description,
            "price" to course.price,
            "imageUrl" to course.imageUrl, // ✅ اصلاح شد
            "isNew" to course.isNew,
            "isPurchased" to false,
            "addedAt" to System.currentTimeMillis()
        )

        db.collection("users").document(uid)
            .collection("purchased_courses").document(course.id)
            .set(data)
            .addOnSuccessListener {
                Log.d("CourseViewModel", "Course added to my list: ${course.title}")
                onDone?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e("CourseViewModel", "Error adding course to my list", e)
            }
    }

    fun removeCourseFromMyList(courseId: String, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users").document(uid)
            .collection("purchased_courses").document(courseId)
            .delete()
            .addOnSuccessListener {
                Log.d("CourseViewModel", "Course removed from my list: $courseId")
                onDone?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e("CourseViewModel", "Error removing course from my list", e)
            }
    }

    fun markCoursePurchased(courseId: String, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return

        val data = mapOf(
            "isPurchased" to true,
            "purchasedAt" to System.currentTimeMillis()
        )

        db.collection("users").document(uid)
            .collection("purchased_courses").document(courseId)
            .set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Log.d("CourseViewModel", "Course marked as purchased: $courseId")
                onDone?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e("CourseViewModel", "Error marking course as purchased", e)
            }
    }

    // بررسی آیا دوره در لیست کاربر است
    fun isCourseInMyList(courseId: String): Boolean {
        return _myCourses.value.any { it.id == courseId }
    }

    // بررسی آیا دوره خریداری شده است
    fun isCoursePurchased(courseId: String): Boolean {
        return _purchasedCourseIds.value.contains(courseId)
    }

}