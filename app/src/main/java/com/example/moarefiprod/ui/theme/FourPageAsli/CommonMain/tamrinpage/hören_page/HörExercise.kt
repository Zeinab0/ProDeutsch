package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

// ✅ مدل اصلی تمرین شنیداری بدون فیلد id از Firestore
data class HörExercise(
    val title: String = "",
    val imageUrl: String = "",
    val score: Int? = null
)

// ✅ مدل کمکی برای نگهداری id سند جدا از داده‌ها
data class HörExerciseWithId(
    val exercise: HörExercise,
    val id: String
)

class HörenRepository {

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

    private val db = FirebaseFirestore.getInstance()

    fun getExercisesByLevel(levelCode: String): Flow<List<HörExerciseWithId>> = callbackFlow {
        val ref = db.collection("hör_levels")
            .document(levelCode)
            .collection("exercises")

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val list = snapshot?.documents?.mapNotNull { doc ->
                val item = doc.toObject(HörExercise::class.java)
                item?.let { HörExerciseWithId(it, doc.id) }
            } ?: emptyList()

            trySend(list)
        }

        awaitClose { listener.remove() }
    }
}

class HörenViewModel : ViewModel() {
    private val repo = HörenRepository()

    private val _exercises = MutableStateFlow<List<HörExerciseWithId>>(emptyList())
    val exercises: StateFlow<List<HörExerciseWithId>> = _exercises

    fun loadExercises(levelCode: String) {
        viewModelScope.launch {
            repo.getExercisesByLevel(levelCode).collect {
                _exercises.value = it
            }
        }
    }
}


// در صورت نیاز به مدل سوالات:
data class Question(
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0
)