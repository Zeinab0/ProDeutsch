package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.BaseGameViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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
    private val _gameIds = MutableStateFlow<List<String>>(emptyList())
    val gameIds: StateFlow<List<String>> = _gameIds


    fun loadMemoryGame(
        pathType: GamePathType,
        courseId: String,
        gameId: String,
        lessonId: String = "",
        contentId: String = ""
    ) {
        if (gameId.isBlank()) {
            Log.e("GameViewModel", "❌ gameId is blank.")
            return
        }

        if (pathType == GamePathType.COURSE && (lessonId.isBlank() || contentId.isBlank())) {
            Log.e("GameHost", "❌ Missing lessonId or contentId for COURSE path")
            return
        }

        resetMemoryGame()

        viewModelScope.launch {
            try {

                val collectionRef = when (pathType) {

                    GamePathType.GRAMMAR_TOPIC -> {
                        if (courseId.isBlank()) {
                            Log.e("GameViewModel", "❌ courseId is blank for GRAMMAR_TOPIC path.")
                            return@launch
                        }
                        db.collection("grammar_topics")
                            .document(courseId)
                            .collection("games")
                    }

                    GamePathType.COURSE -> {
                        if (courseId.isBlank() || lessonId.isBlank() || contentId.isBlank()) {
                            Log.e("GameViewModel", "❌ Invalid courseId, lessonId or contentId for COURSE path.")
                            return@launch
                        }

                        db.collection("Courses")
                            .document(courseId)
                            .collection("Lessons")
                            .document(lessonId)
                            .collection("Contents")
                            .document(contentId)
                            .collection("games")
                    }

                    else -> {
                        Log.e("GameViewModel", "❌ Unsupported GamePathType: $pathType")
                        return@launch
                    }
                }

                val docSnapshot = withContext(Dispatchers.IO) {
                    collectionRef.document(gameId).get().await()
                }

                if (!docSnapshot.exists()) {
                    Log.e("GameViewModel", "❌ Document with gameId=$gameId not found.")
                    return@launch
                }

                val title = docSnapshot.getString("title") ?: ""
                val rawPairs = docSnapshot["pairs"] as? List<*> ?: emptyList<Any>()

                val cardPairs = rawPairs.mapNotNull { item ->
                    val map = item as? Map<*, *> ?: return@mapNotNull null
                    val farsi = map["farsiWord"] as? String
                    val german = map["germanWord"] as? String
                    if (farsi == null || german == null) return@mapNotNull null
                    MemoryCardPair(farsiWord = farsi, germanWord = german)
                }

                _memoryCardPairs.value = cardPairs
                _memoryGameTitle.value = title

                Log.d("GameViewModel", "✅ Memory game loaded successfully: $title with ${cardPairs.size} pairs.")

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ loadMemoryGame error: ${e.message}", e)
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

    fun loadTextPicGame(
        pathType: GamePathType,
        courseId: String,
        gameId: String,
        lessonId: String = "",
        contentId: String = ""
    ) {
        if (gameId.isBlank()) {
            Log.e("GameViewModel", "❌ gameId is blank.")
            return
        }

        if (loadedTextPicGames.contains(gameId)) {
            Log.d("GameViewModel", "Already loaded $gameId, skipping.")
            return
        }

        resetTextPicGame()
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                if (collectionRef == null) {
                    Log.e("GameViewModel", "❌ Could not build Firestore path.")
                    return@launch
                }
                val doc = collectionRef.document(gameId).get().await()

//                val doc = withContext(Dispatchers.IO) {
//                    db.collection(path)
//                        .document(gameId)
//                        .get()
//                        .await()
//                }

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

                Log.d("GameViewModel", "✅ Loaded TextPic game $gameId successfully")

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ loadTextPicGame error: ${e.message}")
                _textPicData.value = null
            }
        }
    }


    // ---------- SENTENCE BUILDER ----------
    private val _sentenceData = MutableStateFlow<SentenceGameData?>(null)
    val sentenceData: StateFlow<SentenceGameData?> = _sentenceData

    fun loadSentenceGame(
        pathType: GamePathType,
        courseId: String,
        gameId: String,
        lessonId: String = "",
        contentId: String = ""
    ) {
        if (gameId.isBlank()) {
            Log.e("GameViewModel", "❌ gameId is blank.")
            return
        }

        viewModelScope.launch {
            try {

                // دریافت مسیر امن به collection بازی‌ها
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                if (collectionRef == null) {
                    Log.e("SentenceGameViewModel", "❌ Invalid Firestore path (null collectionRef).")
                    _sentenceData.value = null
                    return@launch
                }

                // گرفتن داکیومنت بازی
                val docSnapshot = collectionRef
                    .document(gameId)
                    .get()
                    .await()

                if (!docSnapshot.exists()) {
                    Log.e("SentenceGameViewModel", "❌ Game document not found: $gameId")
                    _sentenceData.value = null
                    return@launch
                }

                val question = docSnapshot.getString("question") ?: ""
                val correct = docSnapshot["correctSentence"] as? List<String> ?: emptyList()
                val words = docSnapshot["wordPool"] as? List<String> ?: emptyList()

                _sentenceData.value = SentenceGameData(
                    question = question,
                    correctSentence = correct,
                    wordPool = words
                )

                Log.d("SentenceGameViewModel", "✅ Loaded sentence game: question='$question', words=$words")

            } catch (e: Exception) {
                Log.e("SentenceGameViewModel", "❌ Error loading sentence game: ${e.message}", e)
                _sentenceData.value = null
            }
        }
    }



    private val _correctAnswerss = MutableStateFlow(0)

    private val _wrongAnswerss = MutableStateFlow(0)

    fun incrementCorrect(count: Int) {
        _correctAnswerss.value += count
    }

    fun incrementWrong(count: Int) {
        _wrongAnswerss.value += count
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

    fun initializeTotalQuestions(
        pathType: GamePathType,
        courseId: String,
        lessonId: String = "",
        contentId: String = ""
    ) {
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)

                if (collectionRef == null) {
                    Log.e("GameViewModel", "❌ Could not build Firestore path.")
                    return@launch
                }

                val snapshot = collectionRef.get().await()

                val total = snapshot.documents.count { it.getString("type") == "MULTIPLE_CHOICE" }
                _totalQuestions.value = total

                Log.d("GameViewModel", "✅ Total MULTIPLE_CHOICE questions: $total")

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ initializeTotalQuestions error: ${e.message}", e)
            }
        }
    }


    fun resetScores() {
        _correctAnswers.value = 0
        _wrongAnswers.value = 0
    }

    fun loadMultipleChoiceGame(
        pathType: GamePathType,
        courseId: String,
        gameId: String,
        index: Int,
        lessonId: String = "",
        contentId: String = ""
    ) {
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                if (collectionRef == null) {
                    Log.e("GameViewModel", "❌ Could not build Firestore path.")
                    return@launch
                }
                val doc = collectionRef.document(gameId).get().await()

//                val doc = db.collection(path)
//                    .document(gameId)
//                    .get()
//                    .await()

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

                Log.d("GameViewModel", "✅ MultipleChoice loaded: $gameId")

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ loadMultipleChoiceGame error: ${e.message}")
            }
        }
    }


    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) _correctAnswers.value += 1
        else _wrongAnswers.value += 1
    }

    //کدهای مربوط به مسیر کورس
    enum class GamePathType {
        GRAMMAR_TOPIC,
        COURSE
    }

    fun getGameCollectionPath(
        pathType: GamePathType,
        courseId: String,
        lessonId: String = "",
        contentId: String = ""
    ): String {
        return when (pathType) {
            GamePathType.GRAMMAR_TOPIC -> "grammar_topics/$courseId/games"
            GamePathType.COURSE -> "Courses/$courseId/Lessons/$lessonId/Contents/$contentId/games"
        }
    }

    fun initializeGames(courseId: String, lessonId: String, contentId: String) {
        val db = Firebase.firestore
        db.collection("Courses")
            .document(courseId)
            .collection("Lessons")
            .document(lessonId)
            .collection("Contents")
            .document(contentId)
            .collection("games")
            .orderBy("order")
            .get()
            .addOnSuccessListener { documents ->
                val ids = documents.map { it.id }
                val games = documents.mapNotNull { it.toObject(GameModel::class.java) }
                _gameIds.value = ids
                _allGames.value = games
                Log.d("GameViewModel", "✅ Loaded game IDs: $ids")
            }
            .addOnFailureListener { exception ->
                Log.e("GameViewModel", "❌ Error loading games: ", exception)
            }
    }


    private val _allGames = MutableStateFlow<List<GameModel>>(emptyList())
    val allGames: StateFlow<List<GameModel>> = _allGames

    fun getGameAt(index: Int): GameModel? {
        return allGames.value.getOrNull(index)
    }
    private val _games = MutableStateFlow<List<GameModel>>(emptyList())
    val games: StateFlow<List<GameModel>> = _games

    fun loadGamesForCourse(courseId: String, lessonId: String, contentId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .document(contentId)
                    .collection("games")
                    .get()
                    .await()

                val games = snapshot.documents.mapNotNull {
                    it.toObject(GameModel::class.java)?.copy(id = it.id)
                }.sortedBy { it.order }

                _games.value = games
                Log.d("GameViewModel", "✅ Loaded game IDs: ${games.map { it.id }}")
            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ Failed to load course games: ${e.message}")
            }
        }
    }


    fun loadGamesForTopic(topicId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("grammar_topics")
                    .document(topicId)
                    .collection("games")
                    .get()
                    .await()

                val games = snapshot.documents.mapNotNull { it.toObject(GameModel::class.java)?.copy(id = it.id) }
                    .sortedBy { it.order }

                _games.value = games
                Log.d("GrammerGameViewModel", "📚 Loaded topic games: ${games.map { it.id }}")
            } catch (e: Exception) {
                Log.e("GrammerGameViewModel", "❌ Failed to load topic games: ${e.message}")
            }
        }
    }



    private fun getGameCollectionReference(
        pathType: GrammerGameViewModel.GamePathType,
        courseId: String,
        lessonId: String = "",
        contentId: String = ""
    ): CollectionReference? {
        return when (pathType) {
            GrammerGameViewModel.GamePathType.COURSE -> {
                if (lessonId.isEmpty() || contentId.isEmpty()) {
                    Log.e("GameViewModel", "❌ Invalid lessonId or contentId for COURSE path.")
                    return null
                }
                db.collection("Courses")
                    .document(courseId)
                    .collection("Lessons")
                    .document(lessonId)
                    .collection("Contents")
                    .document(contentId)
                    .collection("games")
            }

            GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC -> {
                db.collection("grammar_topics")
                    .document(courseId)
                    .collection("games")
            }
        }
    }

    // بازی questionStoryGame
    private val _questionStoryGameState = MutableStateFlow<QuestionStoryData?>(null)
    val questionStoryGameState: StateFlow<QuestionStoryData?> = _questionStoryGameState

    fun loadQuestionStoryGame(
        pathType: GamePathType,
        courseId: String,
        lessonId: String,
        contentId: String,
        gameId: String
    ) {
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                if (collectionRef == null) {
                    Log.e("GameViewModel", "❌ Invalid Firestore path.")
                    _questionStoryGameState.value = null
                    return@launch
                }

                val docSnapshot = collectionRef.document(gameId).get().await()
                if (!docSnapshot.exists()) {
                    Log.e("GameViewModel", "❌ Document not found for ID: $gameId")
                    _questionStoryGameState.value = null
                    return@launch
                }

                val questionText = docSnapshot.getString("questionText") ?: ""
                val correctAnswer = docSnapshot.getString("correctAnswer") ?: ""
                val translation = docSnapshot.getString("translation") ?: ""

                _questionStoryGameState.value = QuestionStoryData(
                    questionText = questionText,
                    correctAnswer = correctAnswer,
                    translation = translation
                )


                Log.d("GameViewModel", "✅ Loaded QuestionStoryGame: $questionText")

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ Error loading QuestionStoryGame: ${e.message}")
                _questionStoryGameState.value = null
            }
        }
    }

    // بازی questionStoryGame

    // بازی loadVacancyGame
    private val _vacancyGameState = MutableStateFlow<VacancyData?>(null)
    val vacancyGameState: StateFlow<VacancyData?> = _vacancyGameState

    fun loadVacancyGame(
        pathType: GamePathType,
        courseId: String,
        lessonId: String,
        contentId: String,
        gameId: String
    ) {
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                if (collectionRef == null) {
                    Log.e("GameViewModel", "❌ Invalid Firestore path.")
                    _vacancyGameState.value = null
                    return@launch
                }

                val docSnapshot = collectionRef.document(gameId).get().await()
                if (!docSnapshot.exists()) {
                    Log.e("GameViewModel", "❌ Document not found for ID: $gameId")
                    _vacancyGameState.value = null
                    return@launch
                }

                val sentence = docSnapshot.getString("sentence") ?: ""
                val correctAnswer = docSnapshot.getString("correctAnswer") ?: ""
                val translation = docSnapshot.getString("translation") ?: ""

                _vacancyGameState.value = VacancyData(sentence, correctAnswer, translation)

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ Error loading VacancyGame: ${e.message}")
                _vacancyGameState.value = null
            }
        }
    }
    // بازی loadVacancyGame
    // بازی audio

    private val _audioRecognitionGameState = MutableStateFlow<AudioRecognitionData?>(null)
    val audioRecognitionGameState: StateFlow<AudioRecognitionData?> = _audioRecognitionGameState
    private val _audioUrl = MutableStateFlow("")
    val audioUrl: StateFlow<String> = _audioUrl

    fun loadAudioRecognitionGame(
        pathType: GamePathType,
        courseId: String,
        lessonId: String,
        contentId: String,
        gameId: String
    ) {
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                if (collectionRef == null) {
                    Log.e("GameViewModel", "❌ Invalid Firestore path.")
                    _audioRecognitionGameState.value = null
                    _audioUrl.value = ""
                    return@launch
                }

                val docSnapshot = collectionRef.document(gameId).get().await()
                if (!docSnapshot.exists()) {
                    Log.e("GameViewModel", "❌ AudioRecognition doc not found: $gameId")
                    _audioRecognitionGameState.value = null
                    _audioUrl.value = ""
                    return@launch
                }

                val questionText = docSnapshot.getString("questionText") ?: ""
                val options = docSnapshot["options"] as? List<String> ?: emptyList()
                val correctIndex = docSnapshot.getLong("correctAnswerIndex")?.toInt() ?: -1
                val translation = docSnapshot.getString("translation") ?: ""

                val audioUrl = docSnapshot.getString("audioUrl") ?: "" // ✅ گرفتن آدرس فایل ویس

                _audioRecognitionGameState.value = AudioRecognitionData(
                    questionText = questionText,
                    options = options,
                    correctAnswerIndex = correctIndex,
                    translation = translation
                )

                _audioUrl.value = audioUrl // ✅ ذخیره برای استفاده در UI

                Log.d("GameViewModel", "✅ AudioRecognition loaded. Audio URL: $audioUrl")

            } catch (e: Exception) {
                Log.e("GameViewModel", "❌ Error loading AudioRecognitionGame: ${e.message}")
                _audioRecognitionGameState.value = null
                _audioUrl.value = ""
            }
        }
    }
    // بازی audio
    // بازی WordsGame
    private val _connectWordsGameState = MutableStateFlow<ConnectWordsGameData?>(null)
    val connectWordsGameState: StateFlow<ConnectWordsGameData?> = _connectWordsGameState

    fun loadConnectWordsGame(
        pathType: GamePathType,
        courseId: String,
        lessonId: String,
        contentId: String,
        gameId: String
    ) {
        viewModelScope.launch {
            try {
                val collectionRef = getGameCollectionReference(pathType, courseId, lessonId, contentId)
                    ?: return@launch

                val doc = collectionRef.document(gameId).get().await()

                val words = doc["words"] as? List<String> ?: emptyList()
                val audioUrls = doc["audioUrls"] as? List<String> ?: emptyList()
                val correctPairsRaw = doc["correctPairs"] as? Map<String, String> ?: emptyMap()

                _connectWordsGameState.value = ConnectWordsGameData(
                    words = words,
                    audioUrls = audioUrls,
                    correctPairs = correctPairsRaw
                )
            } catch (e: Exception) {
                Log.e("ViewModel", "❌ Error loading ConnectWordsGame: ${e.message}")
                _connectWordsGameState.value = null
            }
        }
    }

    // بازی WordsGame
}
data class QuestionStoryData(
    val questionText: String,
    val correctAnswer: String,
    val translation: String // ✅ این خط اضافه شود
)
data class VacancyData(
    val sentence: String = "",
    val correctAnswer: String = "",
    val translation: String = ""
)

data class AudioRecognitionData(
    val questionText: String = "",
    val correctAnswerIndex: Int = -1,
    val options: List<String> = emptyList(),
    val translation: String = ""
)

data class ConnectWordsGameData(
    val words: List<String> = emptyList(),
    val audioUrls: List<String> = emptyList(), // از Firestore آدرس mp3 می‌گیریم
    val correctPairs: Map<String, String> = emptyMap() // word → audioUrl
)



data class MemoryCardPair(val farsiWord: String, val germanWord: String)

data class GameModel(
    val id: String = "",
    val title: String? = null,
    val type: String = "",
    val order: Int = 0,
    val question: String? = null,
    val correctSentence: List<String> = emptyList(),
    val wordPool: List<String> = emptyList(),
    val pairs: List<Map<String, String>> = emptyList(),
    val imageUrl: String? = null,
    val words: List<Map<String, Any>> = emptyList(),
    val correctAnswer: String = "",
    val questionText: String = "",
    val translation: String = "",
)


data class MultipleChoiceData(
    val questionText: String = "",
    val correctAnswerIndex: Int = -1,
    val options: List<String> = emptyList(),
    val title: String = "",
    val type: String = "",
    val translation: String
)
data class TextPicData(
    val imageUrl: String,
    val title: String,         // اضافه کردن فیلد title
    val order: Int,           // اضافه کردن فیلد order
    val type: String,         // اضافه کردن فیلد type
    val words: List<TextPicWord>
)

data class TextPicWord(
    val word: String,
    val isCorrect: Boolean
)
data class SentenceGameData(
    val gameType: String = "",
    val question: String = "",
    val correctSentence: List<String> = emptyList(),
    val wordPool: List<String> = emptyList()
)



