package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ---------- مدل‌ها ----------
data class HörExercise(
    val title: String = "",
    val imageUrl: String = "",
    val score: Int? = null
)

data class HörExerciseWithId(
    val exercise: HörExercise,
    val id: String
)

data class Question(
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val id: Int? = null
)

// ---------- Repository ----------
class HörenRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getExercisesByLevel(levelCode: String): Flow<List<HörExerciseWithId>> = callbackFlow {
        val ref = db.collection("hör_levels")
            .document(levelCode)
            .collection("exercises")

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error); return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(HörExercise::class.java)?.let { HörExerciseWithId(it, doc.id) }
            } ?: emptyList()
            trySend(list)
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
                close(error ?: Exception("No document")); return@addSnapshotListener
            }
            trySend(snapshot.getString("audioUrl") ?: "")
        }
        awaitClose { listener.remove() }
    }

    fun getQuestions(level: String, exerciseId: String): Flow<List<Question>> = callbackFlow {
        val ref = db.collection("hör_levels")
            .document(level)
            .collection("exercises")
            .document(exerciseId)
            .collection("questions")

        val listener = ref.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            val list = snap?.documents?.mapNotNull { it.toObject(Question::class.java) }.orEmpty()
            trySend(list)
        }
        awaitClose { listener.remove() }
    }
}

// ---------- ViewModel ----------
class HörenViewModel(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val scoreRepo: UserHorenScoreRepository = UserHorenScoreRepository(),
    private val repo: HörenRepository = HörenRepository()
) : ViewModel() {

    private val _base = MutableStateFlow<List<HörExerciseWithId>>(emptyList())
    private val _userScores = MutableStateFlow<Map<String, Int>>(emptyMap())

    val exercises: StateFlow<List<HörExerciseWithId>> =
        combine(_base, _userScores) { base, scores ->
            base.map { item ->
                val s = scores[item.id]
                item.copy(exercise = item.exercise.copy(score = s))
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun loadExercises(levelCode: String) {
        // 1) تمرین‌ها
        viewModelScope.launch {
            repo.getExercisesByLevel(levelCode).collect { _base.value = it }
        }

        // 2) نمره‌های کاربر
        viewModelScope.launch {
            scoreRepo.listenScores().collect { _userScores.value = it }
        }
    }

    fun submitScore(exerciseId: String, score: Int) {
        viewModelScope.launch { scoreRepo.setScore(exerciseId, score) }
    }
}

// ---------- ViewModel آزمون ----------
class AudioTestViewModel(
    private val repo: HörenRepository = HörenRepository()
) : ViewModel() {

    // وضعیت UI
    data class UiState(
        val audioUrl: String = "",
        val questions: List<Question> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    /**
     * بارگذاری صدا و سوالات برای یک تمرین خاص
     */
    fun load(level: String, exerciseId: String) {
        _uiState.value = UiState(isLoading = true)

        viewModelScope.launch {
            combine(
                repo.getAudioUrl(level, exerciseId),
                repo.getQuestions(level, exerciseId)
            ) { url, qs -> url to qs }
                .catch { e ->
                    _uiState.value = UiState(
                        isLoading = false,
                        error = e.message ?: "خطا در بارگذاری"
                    )
                }
                .collect { (url, qs) ->
                    _uiState.value = UiState(
                        audioUrl = url,
                        questions = qs,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}

