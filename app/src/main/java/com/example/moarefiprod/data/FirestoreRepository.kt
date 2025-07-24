// app/src/main/java/com/example/moarefiprod/data/FirestoreRepository.kt
package com.example.moarefiprod.data

import android.util.Log
import com.example.moarefiprod.data.models.Course
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllCourses(): List<Course> {
        return try {
            val snapshot = db.collection("Courses") // ✅ تغییر اینجا: "courses" به "Courses"
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Course::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting all courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getFreeCourses(): List<Course> {
        return try {
            val snapshot = db.collection("Courses") // ✅ تغییر اینجا: "courses" به "Courses"
                .whereEqualTo("isFree", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Course::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting free courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getNewCourses(): List<Course> {
        return try {
            val snapshot = db.collection("Courses") // ✅ تغییر اینجا: "courses" به "Courses"
                .whereEqualTo("isNew", true)
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Course::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting new courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun getCourseDetails(courseId: String): Course? {
        return try {
            val document = db.collection("Courses").document(courseId).get().await() // ✅ تغییر اینجا: "courses" به "Courses"
            document.toObject(Course::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting course details for $courseId: ${e.localizedMessage}", e)
            null
        }
    }
}