package com.example.moarefiprod.repository

import android.util.Log
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.data.models.CourseItem
import com.example.moarefiprod.data.models.CourseLesson
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllCourses(): List<Course> {
        return try {
            val snapshot = db.collection("Courses")
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                // ✅ اضافه کردن document.id به فیلد id در مدل Course
                document.toObject(Course::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting all courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getFreeCourses(): List<Course> {
        return try {
            val snapshot = db.collection("Courses")
                .whereEqualTo("isFree", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                // ✅ اضافه کردن document.id به فیلد id در مدل Course
                document.toObject(Course::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting free courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getNewCourses(): List<Course> {
        return try {
            val snapshot = db.collection("Courses")
                .whereEqualTo("isNew", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                // ✅ اضافه کردن document.id به فیلد id در مدل Course
                document.toObject(Course::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting new courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getCourseDetails(courseId: String): Course? {
        return try {
            Log.d("FirestoreRepository", "Attempting to get course details for ID: $courseId")
            val document = db.collection("Courses").document(courseId).get().await()
            val course = document.toObject(Course::class.java)
            Log.d("FirestoreRepository", "Course document data: ${document.data}") // لاگ کردن کل داده‌های داکیومنت
            Log.d("FirestoreRepository", "Course object converted: ${course?.title} (is null: ${course == null})")

            // ✅ اضافه کردن document.id به فیلد id در مدل Course
            course?.copy(id = document.id)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting course details for $courseId: ${e.localizedMessage}", e)
            null
        }
    }

    // تابع جدید برای گرفتن لیست دروس یک دوره خاص
    suspend fun getLessonsForCourse(courseId: String): List<CourseLesson> {
        return try {
            val snapshot = db.collection("Courses")
                .document(courseId)
                .collection("Lessons")
                .get()
                .await()
            // ✅ اضافه کردن document.id به فیلد id در مدل CourseLesson
            snapshot.documents.mapNotNull { document ->
                document.toObject(CourseLesson::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting lessons for course $courseId: ${e.localizedMessage}", e)
            emptyList()
        }
    }
    // داخل FirestoreRepository.kt -> suspend fun getCourseItemsForLesson(courseId: String, lessonId: String): List<CourseItem>
    suspend fun getCourseItemsForLesson(courseId: String, lessonId: String): List<CourseItem> {
        return try {
            Log.d("FirestoreRepoItems", "Attempting to get items for course: $courseId, lesson: $lessonId") // ✅ جدید
            val snapshot = db.collection("Courses")
                .document(courseId)
                .collection("Lessons")
                .document(lessonId)
                .collection("Contents") // ✅ مطمئن شوید "Contents" با C بزرگ است، همانطور که در Firestore شما هست
                .get()
                .await()

            Log.d("FirestoreRepoItems", "Contents snapshot size for lesson $lessonId in course $courseId: ${snapshot.documents.size}") // ✅ جدید: این لاگ بسیار مهم است!

            snapshot.documents.mapNotNull { document ->
                val item = document.toObject(CourseItem::class.java)?.copy(id = document.id)
                if (item == null) {
                    Log.w("FirestoreRepoItems", "Failed to map content item document ${document.id} for lesson $lessonId. Raw Data: ${document.data}") // ✅ جدید: جزئیات بیشتر در صورت شکست مپینگ
                } else {
                    Log.d("FirestoreRepoItems", "Mapped content item: ${item.title} (ID: ${item.id}), Type: ${item.type}, Duration: ${item.duration}") // ✅ جدید: جزئیات آیتم مپ شده
                }
                item
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepoItems", "Error getting course items for lesson $lessonId in course $courseId: ${e.localizedMessage}", e) // ✅ جدید: لاگ خطا
            emptyList()
        }
    }
//    suspend fun getCourseItemsForLesson(courseId: String, lessonId: String): List<CourseItem> {
//        return try {
//            val snapshot = db.collection("Courses")
//                .document(courseId)
//                .collection("Lessons")
//                .document(lessonId)
//                .collection("Contents") // نام کالکشن برای آیتم‌های محتوایی
//                // .orderBy("order", Query.Direction.ASCENDING) // اگر فیلد order دارید، می‌توانید بر اساس آن مرتب کنید
//                .get()
//                .await()
//            // ✅ اضافه کردن document.id به فیلد id در مدل CourseItem
//            snapshot.documents.mapNotNull { document ->
//                document.toObject(CourseItem::class.java)?.copy(id = document.id)
//            }
//        } catch (e: Exception) {
//            Log.e("FirestoreRepository", "Error getting course items for lesson $lessonId in course $courseId: ${e.localizedMessage}", e)
//            emptyList()
//        }
//    }
}