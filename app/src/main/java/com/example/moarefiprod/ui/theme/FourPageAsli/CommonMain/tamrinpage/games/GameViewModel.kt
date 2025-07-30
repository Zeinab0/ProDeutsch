package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moarefiprod.data.MultipleChoice
import com.example.moarefiprod.data.SentenceGameData
import com.example.moarefiprod.data.TextPicData
import com.example.moarefiprod.data.TextPicWord
import com.example.moarefiprod.data.WordPair
import com.example.moarefiprod.data.WordPairDisplay
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {

    init {
        Log.d("GameViewModel", "New GameViewModel instance created: ${this.hashCode()}")
    }

    private val db = FirebaseFirestore.getInstance()

    private val _selectOption = MutableStateFlow<MultipleChoice?>(null)
    val selectOption: StateFlow<MultipleChoice?> = _selectOption.asStateFlow()

    private val _wordPairs = MutableStateFlow<List<WordPair>>(emptyList())
    val wordPairs: StateFlow<List<WordPair>> = _wordPairs.asStateFlow()

    private val _displayPairs = MutableStateFlow<List<WordPairDisplay>>(emptyList())
    val displayPairs: StateFlow<List<WordPairDisplay>> = _displayPairs.asStateFlow()

    private val _sentenceData = MutableStateFlow<SentenceGameData?>(null)
    val sentenceData: StateFlow<SentenceGameData?> = _sentenceData.asStateFlow()

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()

    private val _textPicData = MutableStateFlow<TextPicData?>(null)
    val textPicData: StateFlow<TextPicData?> = _textPicData.asStateFlow()

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers.asStateFlow()

    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions: StateFlow<Int> = _totalQuestions.asStateFlow()

    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) {
            _correctAnswers.value = _correctAnswers.value + 1
            Log.d("GameViewModel", "Recorded correct answer. New correct count: ${_correctAnswers.value}")
        } else {
            _wrongAnswers.value = _wrongAnswers.value + 1
            Log.d("GameViewModel", "Recorded wrong answer. New wrong count: ${_wrongAnswers.value}")
        }
    }

    fun resetScores() {
        _correctAnswers.value = 0
        _wrongAnswers.value = 0
        Log.d("GameViewModel", "Scores reset. Correct: ${_correctAnswers.value}, Wrong: ${_wrongAnswers.value}")
    }

    private var loadedTopicId: String? = null
    private var loadedGameId: String? = null

    suspend fun initializeTotalQuestions(topicId: String, gameId: String) {
        if (loadedTopicId == topicId && loadedGameId == gameId && _totalQuestions.value > 0) {
            Log.d("GameViewModel", "Using cached total questions: ${_totalQuestions.value}")
            return
        }

        try {
            val document = db.collection("grammar_topics")
                .document(topicId)
                .collection("games")
                .document(gameId)
                .get().await()
            val questions = document.get("questions") as? List<Map<String, Any>> ?: emptyList()
            _totalQuestions.value = questions.size
            loadedTopicId = topicId
            loadedGameId = gameId
            Log.d("GameViewModel", "Total questions loaded: ${questions.size}")
        } catch (e: Exception) {
            _totalQuestions.value = 0
            Log.e("GameViewModel", "Error loading total questions: ${e.message}")
        }
    }

    fun loadMultipleChoiceGame(topicId: String, gameId: String, questionIndex: Int) {
        db.collection("grammar_topics")
            .document(topicId)
            .collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener { document ->
                val questions = document.get("questions") as? List<Map<String, Any>> ?: emptyList()
                Log.d("GameViewModel", "Loaded questions for gameId: $gameId, questionIndex: $questionIndex, total questions: ${questions.size}")
                if (questionIndex in questions.indices) {
                    val question = questions[questionIndex]["questionText"] as? String ?: ""
                    val options = (questions[questionIndex]["options"] as? List<String>) ?: emptyList()
                    val correctIndex = (questions[questionIndex]["correctAnswerIndex"] as? Long)?.toInt() ?: 0
                    val translation = questions[questionIndex]["translation"] as? String ?: ""

                    _selectOption.value = MultipleChoice(
                        question = question,
                        options = options,
                        correctIndex = correctIndex,
                        translation = translation
                    )
                    Log.d("GameViewModel", "Loaded question: $question, options: $options, correctIndex: $correctIndex")
                } else {
                    _selectOption.value = null
                    Log.d("GameViewModel", "Invalid questionIndex: $questionIndex, total questions: ${questions.size}")
                }
            }
            .addOnFailureListener {
                _selectOption.value = null
                Log.e("GameViewModel", "Failed to load question for gameId: $gameId, error: ${it.message}")
            }
    }

    fun loadMemoryGame(courseId: String, lessonId: String, contentId: String, gameId: String) {
        db.collection("Courses")
            .document(courseId)
            .collection("Lessons")
            .document(lessonId)
            .collection("Contents")
            .document(contentId)
            .collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener { document ->
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
            .addOnFailureListener {
                _wordPairs.value = emptyList()
                _displayPairs.value = emptyList()
            }
    }

    fun loadSentenceGame(courseId: String, lessonId: String, contentId: String, gameId: String) {
        db.collection("Courses")
            .document(courseId)
            .collection("Lessons")
            .document(lessonId)
            .collection("Contents")
            .document(contentId)
            .collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener { document ->
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
            }
            .addOnFailureListener {
                _sentenceData.value = null
            }
    }
}