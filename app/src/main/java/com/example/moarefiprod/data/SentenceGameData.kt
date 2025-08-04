package com.example.moarefiprod.data

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.BaseGameViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SentenceGameData(
    val gameType: String = "",
    val question: String = "",
    val correctSentence: List<String> = emptyList(),
    val wordPool: List<String> = emptyList()
)



class SentenceGameViewModel : BaseGameViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _sentenceData = MutableStateFlow<SentenceGameData?>(null)
    val sentenceData: StateFlow<SentenceGameData?> = _sentenceData

    fun loadSentenceGame(courseId: String, gameId: String) {
        viewModelScope.launch {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection("grammar_topics")
                    .document(courseId)
                    .collection("games")
                    .document(gameId)
                    .get().await()

                val question = doc.getString("question") ?: ""
                val correct = doc.get("correctSentence") as? List<String> ?: emptyList()
                val words = doc.get("wordPool") as? List<String> ?: emptyList()

                _sentenceData.value = SentenceGameData(
                    question = question,
                    correctSentence = correct,
                    wordPool = words
                )

                Log.d("SentenceGameViewModel", "✅ Loaded words: $words")
            } catch (e: Exception) {
                Log.e("SentenceGameViewModel", "❌ Error loading: ${e.message}")
                _sentenceData.value = null
            }
        }
    }


    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers

    fun incrementCorrect(count: Int) {
        _correctAnswers.value += count
    }

    fun incrementWrong(count: Int) {
        _wrongAnswers.value += count
    }

}
