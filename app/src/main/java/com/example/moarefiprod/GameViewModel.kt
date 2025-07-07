package com.example.moarefiprod.viewmodel

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

                    _wordPairs.value = originalPairs // برای مقایسه‌ی درست

                    // فقط آلمانی‌ها رو بهم بزن و نمایش بساز
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

    // ⬇️ بازی جمله‌سازی (Sentence)
    // In your GameViewModel, in loadSentenceGame function
    fun loadSentenceGame(docId: String) {
        Log.d("FirebaseDebug", "📄 Trying to load document: $docId")
        db.collection("games").document(docId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("FirebaseDebug", "📥 Loaded document data: ${document.data}")

                    // 🔴 تغییر اینجا 🔴
                    val question = document.getString("quesstion") ?: "" // به 'quesstion' (دو s) تغییر یافت

                    val wordPool = document.get("wordPool") as? List<String> ?: emptyList()
                    val correctSentence = document.get("correctSentence") as? List<String> ?: emptyList()
                    val gameType = document.getString("gameType") ?: ""

                    // ✅ تغییر برای ثابت نگه داشتن ترتیب کلمات
                    val finalWordPool = wordPool // از wordPool اصلی استفاده کنید

                    _sentenceData.value = SentenceGameData(
                        question = question,
                        wordPool = finalWordPool,
                        correctSentence = correctSentence,
                        gameType = gameType
                    )

                    Log.d("FirebaseDebug", "✅ WordPool (not shuffled): $finalWordPool")
                    Log.d("FirebaseDebug", "✅ Question loaded: $question") // لاگ برای تایید
                } else {
                    Log.e("FirebaseDebug", "Document does not exist")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseDebug", "Failed to load sentence: ", e)
            }
    }

}

//fun loadSentenceGame(docId: String) {
//        Log.d("FirebaseDebug", "📄 Trying to load document: $docId")
//        db.collection("games").document(docId).get()
//            .addOnSuccessListener { document ->
//                if (document != null && document.exists()) {
//                    Log.d("FirebaseDebug", "📥 Loaded document data: ${document.data}")
//
//                    val question = document.getString("question") ?: ""
//                    val wordPool = document.get("wordPool") as? List<String> ?: emptyList()
//                    val correctSentence = document.get("correctSentence") as? List<String> ?: emptyList()
//                    val gameType = document.getString("gameType") ?: ""
//
//                    // ✅ تغییر برای ثابت نگه داشتن ترتیب کلمات
//                    // val shuffledWordPool = wordPool.shuffled() // این خط را حذف یا کامنت کنید
//                    // به جای آن:
//                    val finalWordPool = wordPool // از wordPool اصلی استفاده کنید
//
//                    _sentenceData.value = SentenceGameData(
//                        question = question,
//                        wordPool = finalWordPool, // اینجا از finalWordPool استفاده کنید
//                        correctSentence = correctSentence,
//                        gameType = gameType
//                    )
//
//                    Log.d("FirebaseDebug", "✅ WordPool (not shuffled): $finalWordPool") // لاگ را هم تغییر دهید
//                } else {
//                    Log.e("FirebaseDebug", "Document does not exist")
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("FirebaseDebug", "Failed to load sentence: ", e)
//            }
//    }
