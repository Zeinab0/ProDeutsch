package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {

    init {
        Log.d("GameViewModel", "New GameViewModel instance created: ${this.hashCode()}")
    }

    private val db = FirebaseFirestore.getInstance()

    // StateFlows موجود برای سایر بازی‌ها
    private val _selectOption = MutableStateFlow<MultipleChoice?>(null)
    val selectOption: StateFlow<MultipleChoice?> = _selectOption.asStateFlow()

    private val _wordPairs = MutableStateFlow<List<WordPair>>(emptyList())
    val wordPairs: StateFlow<List<WordPair>> = _wordPairs.asStateFlow()

    private val _displayPairs = MutableStateFlow<List<WordPairDisplay>>(emptyList())
    val displayPairs: StateFlow<List<WordPairDisplay>> = _displayPairs.asStateFlow()

    private val _sentenceData = MutableStateFlow<SentenceGameData?>(null)
    val sentenceData: StateFlow<SentenceGameData?> = _sentenceData.asStateFlow()

    // StateFlows موجود برای امتیازات کلی
    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers.asStateFlow()

    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions: StateFlow<Int> = _totalQuestions.asStateFlow()

    private val _totalGames = MutableStateFlow(0)
    val totalGames: StateFlow<Int> = _totalGames.asStateFlow()

    private val _gameIds = MutableStateFlow<List<String>>(emptyList())
    val gameIds: StateFlow<List<String>> = _gameIds.asStateFlow()

    // StateFlows جدید برای TextPicPage
    private val _textPicData = MutableStateFlow<TextPicData?>(null)
    val textPicData: StateFlow<TextPicData?> = _textPicData.asStateFlow()

    private val _textPicSelectedWords = MutableStateFlow<Set<String>>(emptySet())
    val textPicSelectedWords: StateFlow<Set<String>> = _textPicSelectedWords.asStateFlow()

    private val _textPicCorrectCount = MutableStateFlow(0)
    val textPicCorrectCount: StateFlow<Int> = _textPicCorrectCount.asStateFlow()

    private val _textPicWrongCount = MutableStateFlow(0)
    val textPicWrongCount: StateFlow<Int> = _textPicWrongCount.asStateFlow()

    // StateFlow برای زمان کل
    private val _totalTimeInSeconds = MutableStateFlow(0)
    val totalTimeInSeconds: StateFlow<Int> = _totalTimeInSeconds.asStateFlow()

    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) {
            _correctAnswers.value += 1
            Log.d("GameViewModel", "Recorded correct answer. New correct count: ${_correctAnswers.value}")
        } else {
            _wrongAnswers.value += 1
            Log.d("GameViewModel", "Recorded wrong answer. New wrong count: ${_wrongAnswers.value}")
        }
    }

    fun incrementCorrect(count: Int) {
        _correctAnswers.value += count
        Log.d("GameViewModel", "Added $count to correct answers. Total now: ${_correctAnswers.value}")
    }

    fun incrementWrong(count: Int) {
        _wrongAnswers.value += count
        Log.d("GameViewModel", "Added $count to wrong answers. Total now: ${_wrongAnswers.value}")
    }

    fun resetScores() {
        _correctAnswers.value = 0
        _wrongAnswers.value = 0
        Log.d("GameViewModel", "Scores reset. Correct: ${_correctAnswers.value}, Wrong: ${_wrongAnswers.value}")
    }

    // توابع مدیریت وضعیت برای TextPicPage
    fun toggleTextPicWordSelection(word: String) {
        _textPicSelectedWords.value = if (_textPicSelectedWords.value.contains(word)) {
            _textPicSelectedWords.value - word
        } else {
            _textPicSelectedWords.value + word
        }
        Log.d("GameViewModel", "TextPic word toggled: $word, current selection: ${_textPicSelectedWords.value}")
    }

    fun checkTextPicAnswers() {
        val data = _textPicData.value ?: return
        val currentSelectedWords = _textPicSelectedWords.value

        var correct = 0
        var wrong = 0

        data.words.forEach { word ->
            val isWordSelected = currentSelectedWords.contains(word.word)
            if (word.isCorrect && isWordSelected) {
                correct++
            } else if (!word.isCorrect && isWordSelected) {
                wrong++
            }
        }
        _textPicCorrectCount.value = correct
        _textPicWrongCount.value = wrong
        Log.d("GameViewModel", "TextPic answers checked. Correct: $correct, Wrong: $wrong")
    }

    fun resetTextPicGame() {
        _textPicSelectedWords.value = emptySet()
        _textPicCorrectCount.value = 0
        _textPicWrongCount.value = 0
        Log.d("GameViewModel", "TextPic game reset.")
    }

    // متد جدید برای ثبت نتیجه بازی حافظه (با اضافه کردن زمان)
    fun recordMemoryGameResult(correct: Int, wrong: Int, timeInSeconds: Int) {
        incrementCorrect(correct)
        incrementWrong(wrong)
        _totalTimeInSeconds.value += timeInSeconds
        Log.d("GameViewModel", "Recorded Memory Game result. Correct: $correct, Wrong: $wrong, Time: $timeInSeconds, Total Time: ${_totalTimeInSeconds.value}")
    }

    private var loadedTopicId: String? = null
    private var loadedGameId: String? = null
    private var loadedCourseId: String? = null
    private var loadedLessonId: String? = null
    private var loadedContentId: String? = null
    private var currentGameIndex = 0

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

    suspend fun initializeGames(courseId: String, lessonId: String, contentId: String) {
        if (loadedCourseId == courseId && loadedLessonId == lessonId && loadedContentId == contentId && _totalGames.value > 0) {
            Log.d("GameViewModel", "Using cached games: ${_totalGames.value}, IDs: ${_gameIds.value}")
            return
        }

        try {
            Log.d("GameViewModel", "Fetching games for courseId=$courseId, lessonId=$lessonId, contentId=$contentId")
            val documents = db.collection("Courses")
                .document(courseId)
                .collection("Lessons")
                .document(lessonId)
                .collection("Contents")
                .document(contentId)
                .collection("games")
                .get().await()
            val gameIds = documents.documents.map { it.id }
            Log.d("GameViewModel", "Raw documents: ${documents.documents.map { it.id to it.data }}")
            _gameIds.value = gameIds
            _totalGames.value = gameIds.size
            loadedCourseId = courseId
            loadedLessonId = lessonId
            loadedContentId = contentId
            Log.d("GameViewModel", "Total games loaded: ${gameIds.size}, IDs: $gameIds")
        } catch (e: Exception) {
            _totalGames.value = 0
            _gameIds.value = emptyList()
            Log.e("GameViewModel", "Error loading games: ${e.message}")
            // برای تست موقت، لیست gameIds رو دستی پر می‌کنیم
            _gameIds.value = listOf("memory_game_2", "sentence_builder_1", "text_pic_3")
            _totalGames.value = _gameIds.value.size
            Log.d("GameViewModel", "Using fallback gameIds: ${_gameIds.value}")
        }
    }

    fun getNextGameId(currentIndex: Int): String? {
        return if (currentIndex < _gameIds.value.size) _gameIds.value[currentIndex] else null
    }

    fun loadNextGame(courseId: String, lessonId: String, contentId: String) {
        val games = _gameIds.value
        if (games.isEmpty()) {
            Log.d("GameViewModel", "لیست بازی‌ها خالی است")
            return
        }
        if (currentGameIndex >= games.size) {
            Log.d("GameViewModel", "تمام بازی‌ها اجرا شدند")
            currentGameIndex = 0 // ریست برای تکرار (اختیاری)
            return
        }

        val gameId = games[currentGameIndex]
        currentGameIndex++

        when (gameId) {
            "memory_game_2" -> loadMemoryGame(courseId, lessonId, contentId, gameId)
            "sentence_builder_1" -> loadSentenceGame(courseId, lessonId, contentId, gameId)
            "text_pic_3" -> loadTextPicGame(courseId, lessonId, contentId, gameId)
            else -> Log.w("GameViewModel", "Unknown gameId: $gameId")
        }
    }

    fun getNextGameRouteAndId(currentIndex: Int): Pair<String?, String?> {
        val nextGameId = getNextGameId(currentIndex)
        return if (nextGameId != null) {
            val nextRoute = when (nextGameId) {
                "memory_game_2" -> "memoryGame"
                "sentence_builder_1" -> "sentenceBuilder"
                "text_pic_3" -> "textPic"
                else -> null
            }
            Pair(nextRoute, nextGameId)
        } else {
            Pair(null, null)
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

    fun loadTextPicGame(courseId: String, lessonId: String, contentId: String, gameId: String) {
        resetTextPicGame()
        viewModelScope.launch {
            try {
                val document = db.collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .document(contentId)
                    .collection("games")
                    .document(gameId)
                    .get().await()

                if (document != null && document.exists()) {
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val title = document.getString("title") ?: "کلمات درست رو پیدا کن"
                    val order = document.getLong("order")?.toInt() ?: 0
                    val type = document.getString("type") ?: "TEXT_PIC"
                    val wordsData = document.get("words") as? List<Map<String, Any>> ?: emptyList()
                    val wordList = wordsData.map { word ->
                        TextPicWord(
                            word = word["word"] as? String ?: "",
                            isCorrect = word["isCorrect"] as? Boolean ?: false
                        )
                    }
                    _textPicData.value = TextPicData(imageUrl, title, order, type, wordList)
                    Log.d("GameViewModel", "Loaded textPic: imageUrl=$imageUrl, title=$title, words=$wordList")
                } else {
                    _textPicData.value = null
                    Log.e("GameViewModel", "Document does not exist for gameId: $gameId")
                }
            } catch (e: Exception) {
                _textPicData.value = null
                Log.e("GameViewModel", "Failed to load textPic for gameId: $gameId, error: ${e.message}")
            }
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