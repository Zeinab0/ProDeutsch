package com.example.moarefiprod.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.example.moarefiprod.data.WordPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _wordPairs = MutableStateFlow<List<WordPair>>(emptyList())
    val wordPairs = _wordPairs.asStateFlow()
//    init {
//        _wordPairs.value = listOf(
//            WordPair("سلام", "wasser"),
//            WordPair("آب", "Hallo"),
//            WordPair("میل", "Buch"),
//            WordPair("جایگاه", "Handy Nummer"),
//            WordPair("کتاب", "Sofa"),
//            WordPair("شماره تلفن", "platz")
//        )
//    }

    fun loadMemoryGame(gameId: String) {
        db.collection("games").document(gameId)
            .get()
            .addOnSuccessListener { document ->
                Log.d("FirebaseGame", "Raw document: ${document.data}")
                if (document.exists()) {
                    val list = document["pairs"] as? List<Map<String, String>>
                    val pairs = list?.map {
                        WordPair(
                            farsiWord = it["farsiWord"] ?: "",
                            germanWord = it["germanWord"] ?: ""
                        )
                    } ?: emptyList()
                    Log.d("FirebaseGame", "Loaded pairs: $pairs")
                    _wordPairs.value = pairs
                } else {
                    Log.e("FirebaseGame", "Document doesn't exist")
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseGame", "Failed to fetch: ${it.message}")
            }
    }
}
