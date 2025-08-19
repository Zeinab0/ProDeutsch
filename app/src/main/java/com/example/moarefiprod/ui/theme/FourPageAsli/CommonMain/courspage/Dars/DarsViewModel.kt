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

class DarsViewModel : ViewModel() {

    // -------- Handout --------
    private val _handout = MutableStateFlow<HandoutModel?>(null)
    val handout: StateFlow<HandoutModel?> = _handout

    // -------- Video --------
    private val _videoUrl = MutableStateFlow<String?>(null)
    val videoUrl: StateFlow<String?> = _videoUrl

    // -------- Words (NEW) --------
    private val _wordsTitle = MutableStateFlow("")
    val wordsTitle: StateFlow<String> = _wordsTitle

    private val _words = MutableStateFlow<List<WordItem>>(emptyList())
    val words: StateFlow<List<WordItem>> = _words

    private val _isWordsLoading = MutableStateFlow(false)
    val isWordsLoading: StateFlow<Boolean> = _isWordsLoading

    private val _wordsError = MutableStateFlow<String?>(null)
    val wordsError: StateFlow<String?> = _wordsError

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

    fun loadVideoUrl(courseId: String, lessonId: String, contentId: String) {
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

                val url = doc.getString("url")
                _videoUrl.value = url

                Log.d("VideoVM", "✅ Video URL Loaded: $url")
            } catch (e: Exception) {
                Log.e("VideoVM", "❌ Failed to load video URL: ${e.message}")
            }
        }
    }

    // NEW: Words loader مطابق ساختار شما (subcollection = "Words" با W بزرگ)
    fun loadWords(courseId: String, lessonId: String, contentId: String) {
        viewModelScope.launch {
            _isWordsLoading.value = true
            _wordsError.value = null
            try {
                val contentRef = Firebase.firestore
                    .collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .document(contentId)

                // عنوان محتوا
                val metaSnap = contentRef.get().await()
                _wordsTitle.value = metaSnap.getString("title") ?: "کلمات"

                // لیست کلمات از subcollection: "Words"
                val wordsSnap = contentRef
                    .collection("Words")
                    .orderBy("order")
                    .get()
                    .await()

                val items = wordsSnap.documents.mapNotNull { it.toObject(WordItem::class.java) }
                _words.value = items

                Log.d("WordsVM", "✅ Loaded ${items.size} words for $contentId")
            } catch (e: Exception) {
                _wordsError.value = e.message
                Log.e("WordsVM", "❌ Failed to load words: ${e.message}")
            } finally {
                _isWordsLoading.value = false
            }
        }
    }
}

data class HandoutModel(
    val fileName: String = "",
    val title: String = "",
    val size: String = "",
    val url: String = ""
)

data class VideoModel(
    val url: String = ""
)

data class WordItem(
    val text: String = "",
    val translation: String = "",
    val order: Int = 0
)