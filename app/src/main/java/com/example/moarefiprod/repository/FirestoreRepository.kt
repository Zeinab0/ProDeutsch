package com.example.moarefiprod.repository

import android.util.Log
import com.example.moarefiprod.data.models.Course
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

    suspend fun getUserCourses(userId: String): List<Course> {
        return try {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("myCourses")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(Course::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user courses: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun addCourseToUser(userId: String, course: Course) {
        try {
            db.collection("users")
                .document(userId)
                .collection("myCourses")
                .document(course.id)
                .set(course.copy(isPurchased = true, isInMyCourses = true))
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding course: ${e.localizedMessage}", e)
        }
    }

}