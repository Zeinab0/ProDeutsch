package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moarefiprod.data.FirestoreRepository // ایمپورت Repository
import com.example.moarefiprod.data.models.Course // ایمپورت مدل Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: FirestoreRepository = FirestoreRepository() // 💡 وابستگی به Repository
) : ViewModel() {

    // State برای نگهداری لیست همه دوره‌ها
    private val _allCourses = MutableStateFlow<List<Course>>(emptyList())
    val allCourses: StateFlow<List<Course>> = _allCourses.asStateFlow()

    // State برای نگهداری لیست دوره‌های رایگان
    private val _freeCourses = MutableStateFlow<List<Course>>(emptyList())
    val freeCourses: StateFlow<List<Course>> = _freeCourses.asStateFlow()

    // State برای نگهداری لیست دوره‌های جدید
    private val _newCourses = MutableStateFlow<List<Course>>(emptyList())
    val newCourses: StateFlow<List<Course>> = _newCourses.asStateFlow()

    // State برای مدیریت وضعیت بارگذاری (مثلاً نمایش Progress Bar)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State برای مدیریت پیام‌های خطا
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // وقتی ViewModel ساخته می‌شود، بلافاصله شروع به بارگذاری داده‌ها می‌کند
        loadAllCourses()
        loadFreeCourses()
        loadNewCourses()
    }

    // تابع برای بارگذاری همه دوره‌ها
    fun loadAllCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // خطاهای قبلی را پاک کنید
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

    // تابع برای بارگذاری دوره‌های رایگان
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

    // تابع برای بارگذاری دوره‌های جدید
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

    // تابع برای بارگذاری جزئیات یک دوره خاص (اگر لازم باشد)
    fun loadCourseDetails(courseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val course = repository.getCourseDetails(courseId)
                // اینجا می‌توانید یک StateFlow جداگانه برای نگهداری جزئیات دوره فعلی داشته باشید
                // یا اینکه این داده را مستقیماً به UI برگردانید (بستگی به معماری شما دارد)
                // فعلاً فقط لاگ می‌کنیم.
                Log.d("CourseViewModel", "Loaded course details for $courseId: $course")
            } catch (e: Exception) {
                _errorMessage.value = "خطا در بارگذاری جزئیات دوره $courseId: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}