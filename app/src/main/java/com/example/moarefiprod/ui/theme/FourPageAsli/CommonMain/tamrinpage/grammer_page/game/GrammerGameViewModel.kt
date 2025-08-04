package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moarefiprod.data.TextPicData
import com.example.moarefiprod.data.TextPicWord
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.BaseGameViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GrammerGameViewModel : BaseGameViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // ---------- MEMORY ----------
    private val _memoryCardPairs = MutableStateFlow<List<MemoryCardPair>>(emptyList())
    val memoryCardPairs: StateFlow<List<MemoryCardPair>> = _memoryCardPairs

    private val _memoryGameTitle = MutableStateFlow("بازی حافظه")

    fun resetMemoryGame() {
        _memoryCardPairs.value = emptyList()
        _memoryGameTitle.value = "بازی حافظه"
    }
    fun gameListSize(): Int = _gameList.size

    fun loadMemoryGameFromGrammar(topicId: String, gameId: String) {

        resetMemoryGame()
        viewModelScope.launch {
            try {
                val doc = withContext(Dispatchers.IO) {
                    db.collection("grammar_topics")
                        .document(topicId)
                        .collection("games")
                        .document(gameId)
                        .get()
                        .await()
                }

//                Log.d("GameViewModel", "✅ Firestore document retrieved for $gameId")

                val title = doc.getString("title") ?: ""
//                Log.d("GameViewModel", "📘 Title: $title")

                val rawPairs = doc["pairs"] as? List<*> ?: run {
//                    Log.w("GameViewModel", "⚠️ No 'pairs' field or wrong format in document $gameId")
                    emptyList<Any>()
                }

//                Log.d("GameViewModel", "🔍 Found ${rawPairs.size} raw pairs")

                val cardPairs = rawPairs.mapNotNull { item ->
                    val map = item as? Map<*, *>
                    if (map == null) {
//                        Log.w("GameViewModel", "⚠️ Item is not a map: $item")
                        return@mapNotNull null
                    }

                    val farsi = map["farsiWord"] as? String
                    val german = map["germanWord"] as? String

                    if (farsi == null || german == null) {
//                        Log.w("GameViewModel", "⚠️ Invalid pair entry. farsi: $farsi, german: $german")
                        return@mapNotNull null
                    }

                    MemoryCardPair(farsiWord = farsi, germanWord = german)
                }

//                Log.d("GameViewModel", "✅ Parsed ${cardPairs.size} valid pairs")

                _memoryCardPairs.value = cardPairs
                _memoryGameTitle.value = title

            } catch (e: Exception) {
//                Log.e("GameViewModel", "❌ loadMemoryGameFromGrammar error: ${e.message}", e)
            }
        }
    }
    private val _gameList = mutableListOf<String>()

    fun setGameList(gameIds: List<String>) {
        _gameList.clear()
        _gameList.addAll(gameIds)
    }

    fun getNextGameId(currentIndex: Int): String? {
        return if (currentIndex + 1 < _gameList.size) _gameList[currentIndex + 1] else null
    }

    // ---------- TEXT PIC ----------
    private val _textPicData = MutableStateFlow<TextPicData?>(null)
    val textPicData: StateFlow<TextPicData?> = _textPicData

    fun resetTextPicGame() {
        _textPicData.value = null
    }

    private val loadedTextPicGames = mutableSetOf<String>()

    fun loadTextPicGameFromGrammar(topicId: String, gameId: String) {
        Log.d("GameViewModel", "Trying to load $gameId")
        Log.d("GameViewModel", "Loaded $gameId successfully")

        if (loadedTextPicGames.contains(gameId)) {
            Log.d("GameViewModel", "Already loaded $gameId, skipping.")
            return
        }

        resetTextPicGame()
        viewModelScope.launch {
            try {
                val doc = withContext(Dispatchers.IO) {
                    db.collection("grammar_topics")
                        .document(topicId)
                        .collection("games")
                        .document(gameId)
                        .get()
                        .await()
                }

                val imageUrl = doc.getString("imageUrl") ?: ""
                val title = doc.getString("title") ?: ""
                val order = doc.getLong("order")?.toInt() ?: 0
                val type = doc.getString("type") ?: "TEXT_PIC"

                val rawWords = doc["words"] as? List<*> ?: emptyList<Any>()
                val wordList = rawWords.mapNotNull { item ->
                    val map = item as? Map<*, *> ?: return@mapNotNull null
                    val word = map["word"] as? String ?: return@mapNotNull null
                    val isCorrect = map["isCorrect"] as? Boolean ?: false
                    TextPicWord(word, isCorrect)
                }

                _textPicData.value = TextPicData(imageUrl, title, order, type, wordList)
                loadedTextPicGames.add(gameId)

            } catch (e: Exception) {
//                Log.e("GameViewModel", "loadTextPicGameFromGrammar error: ${e.message}")
                _textPicData.value = null
            }
        }
    }



    // ---------- SENTENCE BUILDER ----------
    private val _sentenceWords = MutableStateFlow<List<String>>(emptyList())
    val sentenceWords: StateFlow<List<String>> = _sentenceWords

    private val _sentenceAnswer = MutableStateFlow("")
    val sentenceAnswer: StateFlow<String> = _sentenceAnswer

    private val _sentenceTitle = MutableStateFlow("جمله‌سازی")
    val sentenceTitle: StateFlow<String> = _sentenceTitle


    fun loadSentenceGameFromGrammar(topicId: String, gameId: String) {
        Log.d("GameViewModel", "🟡 Loading sentence game: $gameId (topic: $topicId)")
        viewModelScope.launch {
            try {
                val doc = withContext(Dispatchers.IO) {
                    db.collection("grammar_topics")
                        .document(topicId)
                        .collection("games")
                        .document(gameId)
                        .get()
                        .await()
                }

                val correctSentenceList = doc["correctSentence"] as? List<*> ?: emptyList<Any>()
                val wordPoolList = doc["wordPool"] as? List<*> ?: emptyList<Any>()
                val title = doc.getString("title") ?: ""

                val cleanWords = wordPoolList.mapNotNull { it as? String }
                val cleanSentence = correctSentenceList.mapNotNull { it as? String }

                Log.d("GameViewModel", "✅ Sentence: ${cleanSentence.joinToString(" ")}")
                Log.d("GameViewModel", "✅ Words count: ${cleanWords.size}")
                Log.d("GameViewModel", "✅ Title: $title")

                _sentenceAnswer.value = cleanSentence.joinToString(" ")
                _sentenceWords.value = cleanWords.shuffled()
                _sentenceTitle.value = title

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ Failed to load sentence game: ${e.message}", e)
            }
        }
    }



    // ---------- MULTIPLE CHOICE ----------
    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions: StateFlow<Int> = _totalQuestions

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers

    private val _selectOption = MutableStateFlow<MultipleChoiceData?>(null)
    val selectOption: StateFlow<MultipleChoiceData?> = _selectOption

    fun initializeTotalQuestions(topicId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("grammar_topics")
                    .document(topicId)
                    .collection("games")
                    .get()
                    .await()

                val total = snapshot.documents.count { it.getString("type") == "MULTIPLE_CHOICE" }
                _totalQuestions.value = total
            } catch (e: Exception) {
                Log.e("GameViewModel", "initializeTotalQuestions error: ${e.message}")
            }
        }
    }

    fun resetScores() {
        _correctAnswers.value = 0
        _wrongAnswers.value = 0
    }

    fun loadMultipleChoiceGame(topicId: String, gameId: String, index: Int) {
        viewModelScope.launch {
            try {
                val doc = db.collection("grammar_topics")
                    .document(topicId)
                    .collection("games")
                    .document(gameId)
                    .get()
                    .await()

                val questionText = doc.getString("questionText") ?: ""
                val options = doc.get("options") as? List<String> ?: emptyList()
                val correctAnswerIndex = doc.getLong("correctAnswerIndex")?.toInt() ?: -1
                val translation = doc.getString("translation") ?: ""
                val title = doc.getString("title") ?: ""
                val type = doc.getString("type") ?: ""

                _selectOption.value = MultipleChoiceData(
                    questionText = questionText,
                    correctAnswerIndex = correctAnswerIndex,
                    options = options,
                    title = title,
                    type = type,
                    translation = translation
                )
            } catch (e: Exception) {
                Log.e("GameViewModel", "loadMultipleChoiceGame error: ${e.message}")
            }
        }
    }


    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) _correctAnswers.value += 1
        else _wrongAnswers.value += 1
    }
}

data class MemoryCardPair(val farsiWord: String, val germanWord: String)


data class MultipleChoiceData(
    val questionText: String = "",
    val correctAnswerIndex: Int = -1,
    val options: List<String> = emptyList(),
    val title: String = "",
    val type: String = "",
    val translation: String
)
