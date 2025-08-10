package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class HandoutModel(
    val fileName: String = "",
    val title: String = "",
    val size: String = "",
    val url: String = ""
)

class  DarsViewModel : ViewModel() {

    private val _handout = MutableStateFlow<HandoutModel?>(null)
    val handout: StateFlow<HandoutModel?> = _handout

    fun loadHandout(courseId: String, lessonId: String, contentId: String) {
        viewModelScope.launch {
            try {
                val doc = Firebase.firestore
                    .collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .document(contentId)
                    .get()
                    .await()

                val data = doc.toObject(HandoutModel::class.java)
                _handout.value = data

                Log.d("LessonContentVM", "✅ Handout Loaded: $data")

            } catch (e: Exception) {
                Log.e("LessonContentVM", "❌ Failed to load handout: ${e.message}")
            }
        }
    }
}