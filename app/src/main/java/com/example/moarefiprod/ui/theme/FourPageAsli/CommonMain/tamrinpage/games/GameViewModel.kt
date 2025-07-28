package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moarefiprod.data.SentenceGameData
import com.example.moarefiprod.data.WordPair
import com.example.moarefiprod.data.WordPairDisplay
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // برای بازی حافظه
    private val _wordPairs = MutableStateFlow<List<WordPair>>(emptyList())
    val wordPairs: StateFlow<List<WordPair>> = _wordPairs.asStateFlow()

    private val _displayPairs = MutableStateFlow<List<WordPairDisplay>>(emptyList())
    val displayPairs: StateFlow<List<WordPairDisplay>> = _displayPairs.asStateFlow()

    // برای بازی جمله‌سازی
    private val _sentenceData = MutableStateFlow<SentenceGameData?>(null)
    val sentenceData: StateFlow<SentenceGameData?> = _sentenceData.asStateFlow()

    // ⬇️ بازی حافظه (Match)
    fun loadMemoryGame(gameId: String) {
        db.collection("games").document(gameId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val list = document["pairs"] as? List<Map<String, String>>
                    val originalPairs = list?.map {
                        WordPair(
                            farsiWord = it["farsiWord"] ?: "",
                            germanWord = it["germanWord"] ?: ""
                        )
                    } ?: emptyList()

                    _wordPairs.value = originalPairs

                    val shuffledGerman = originalPairs.map { it.germanWord }.shuffled()
                    val displayList = originalPairs.mapIndexed { index, pair ->
                        WordPairDisplay(
                            farsiWord = pair.farsiWord,
                            germanWord = shuffledGerman.getOrNull(index) ?: ""
                        )
                    }
                    _displayPairs.value = displayList
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseGame", "Failed to fetch: ${it.message}")
            }
    }

    fun loadSentenceGame(courseId: String, lessonId: String, contentId: String, gameId: String) {
        Log.d("FirebaseDebug", "📄 Attempting to load sentence game with path: Courses/$courseId/Lessons/$lessonId/Contents/$contentId/games/$gameId")
        db.collection("Courses")
            .document(courseId)
            .collection("Lessons") // تغییر به "Lessons" (با "s" بزرگ)
            .document(lessonId)
            .collection("Contents") // تغییر به "Contents" (با "s" بزرگ)
            .document(contentId)
            .collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("FirebaseDebug", "📥 Loaded document data: ${document.data}")

                    val question = document.getString("question") ?: ""
                    val wordPool = document.get("wordPool") as? List<String> ?: emptyList()
                    val correctSentence = document.get("correctSentence") as? List<String> ?: emptyList()
                    val gameType = document.getString("gameType") ?: ""

                    _sentenceData.value = SentenceGameData(
                        gameType = gameType,
                        question = question,
                        wordPool = wordPool,
                        correctSentence = correctSentence
                    )

                    Log.d("FirebaseDebug", "✅ Loaded: question=$question, wordPool=$wordPool, correctSentence=$correctSentence")
                } else {
                    Log.e("FirebaseDebug", "Document does not exist for gameId: $gameId at path Courses/$courseId/Lessons/$lessonId/Contents/$contentId/games/$gameId")
                    _sentenceData.value = null
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseDebug", "Failed to load sentence for gameId: $gameId, error: ${e.message}")
                _sentenceData.value = null
            }
    }
}
