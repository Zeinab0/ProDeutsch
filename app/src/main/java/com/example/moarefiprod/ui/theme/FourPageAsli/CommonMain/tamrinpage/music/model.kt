package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Singer(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val songs: List<String> = emptyList()
)

data class Song(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val imageUrl: String = "",
    val songUrl: String = "",
    val isFavorite: Boolean = false,
    val lyrics: String = ""

)

class MusicViewModel : ViewModel() {

    private var songsReg: ListenerRegistration? = null
    private var singersReg: ListenerRegistration? = null

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    init {
        loadSingers()
        loadSongs()
    }

    private fun loadSingers() {
        singersReg?.remove()
        singersReg = FirebaseFirestore.getInstance()
            .collection("singers")
            .addSnapshotListener { snap, e ->
                if (e != null) return@addSnapshotListener
                val data = snap?.documents?.mapNotNull { d ->
                    d.toObject(Singer::class.java)?.copy(id = d.id)
                } ?: emptyList()
                _singers.value = data
            }
    }

    private fun loadSongs() {
        songsReg?.remove()
        songsReg = FirebaseFirestore.getInstance()
            .collection("songs")
            .addSnapshotListener { snap, e ->
                if (e != null) return@addSnapshotListener
                val data = snap?.documents?.mapNotNull { d ->
                    d.toObject(Song::class.java)?.copy(id = d.id)
                } ?: emptyList()
                _songs.value = data
            }
    }

    fun toggleSongLike(
        userId: String,
        song: Song,
        liked: Boolean,
        onComplete: () -> Unit = {}
    ) {
        val docRef = Firebase.firestore
            .collection("users")
            .document(userId)
            .collection("likedSongs")
            .document(song.id)

        docRef.set(song)
            .addOnSuccessListener {
                Log.d("Firestore", "آهنگ لایک شد و ذخیره شد.")
                _songs.update { currentList ->
                    currentList.map {
                        if (it.id == song.id) it.copy(isFavorite = liked) else it
                    }
                }
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ خطا در ذخیره آهنگ: ${e.message}")
            }

        val task = if (liked) docRef.set(song) else docRef.delete()

        task.addOnSuccessListener {
            _songs.update { currentList ->
                currentList.map {
                    if (it.id == song.id) it.copy(isFavorite = liked) else it
                }
            }
            Log.d("Firestore", "Trying to ${if (liked) "like" else "unlike"} song: ${song.title} by user $userId")
            onComplete()
        }.addOnFailureListener { e ->
            Log.e("LikeError", "خطا در ذخیره/حذف آهنگ: ${e.message}")
        }
    }
    fun getLikedSongIdsForUser(userId: String, onComplete: (Set<String>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users").document(userId)
            .collection("likedSongs")
            .addSnapshotListener { snap, _ ->
                val ids = snap?.documents?.map { it.id }?.toSet() ?: emptySet()
                onComplete(ids)
            }
    }

    fun getLikedSongsForUser(userId: String, onResult: (List<Song>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users").document(userId)
            .collection("likedSongs")
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull { it.toObject(Song::class.java) } ?: emptyList()
                onResult(list)
            }
    }

    private val _singers = MutableStateFlow<List<Singer>>(emptyList())
    val singers: StateFlow<List<Singer>> = _singers

    init {
        fetchSingers()
    }

    fun fetchSingers() {
        FirebaseFirestore.getInstance()
            .collection("singers")
            .get()
            .addOnSuccessListener { result ->
                val singerList = result.documents.mapNotNull { doc ->
                    doc.toObject(Singer::class.java)
                }
                _singers.value = singerList
            }
    }

    override fun onCleared() {
        songsReg?.remove()
        singersReg?.remove()
        super.onCleared()
    }


}

object FirebaseRepository {
    private val firestore = Firebase.firestore

    fun getSingers(onResult: (List<Singer>) -> Unit) {
        firestore.collection("singers")
            .get()
            .addOnSuccessListener { snapshot ->
                val result = snapshot.documents.mapNotNull { it.toObject(Singer::class.java) }
                onResult(result)
            }
    }

    fun getSongs(onResult: (List<Song>) -> Unit) {
        firestore.collection("songs")
            .get()
            .addOnSuccessListener { snapshot ->
                val result = snapshot.documents.mapNotNull { it.toObject(Song::class.java) }
                onResult(result)
            }
    }

}
