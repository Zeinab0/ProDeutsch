package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

// این فایل مربوط به اتصال به دیتابیسه

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class AudioTestRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getQuestions(level: String, exerciseId: String): Flow<List<Question>> = callbackFlow {
        val ref = db.collection("hör_levels")
            .document(level)
            .collection("exercises")
            .document(exerciseId)
            .collection("questions")

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val questions = snapshot?.toObjects<Question>() ?: emptyList()
            trySend(questions)
        }

        awaitClose { listener.remove() }
    }

    fun getAudioUrl(level: String, exerciseId: String): Flow<String> = callbackFlow {
        val ref = db.collection("hör_levels")
            .document(level)
            .collection("exercises")
            .document(exerciseId)

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null || !snapshot.exists()) {
                close(error ?: Exception("No document"))
                return@addSnapshotListener
            }

            val audioUrl = snapshot.getString("audioUrl") ?: ""
            trySend(audioUrl)
        }

        awaitClose { listener.remove() }
    }
}
class AudioTestViewModel : ViewModel() {
    private val repo = AudioTestRepository()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _audioUrl = MutableStateFlow("")
    val audioUrl: StateFlow<String> = _audioUrl

    fun loadQuestions(level: String, exerciseId: String) {
        viewModelScope.launch {
            repo.getQuestions(level, exerciseId).collect { questions ->
                _questions.value = questions
            }
        }
        viewModelScope.launch {
            repo.getAudioUrl(level, exerciseId).collect { url ->
                _audioUrl.value = url
            }
        }
    }
}
