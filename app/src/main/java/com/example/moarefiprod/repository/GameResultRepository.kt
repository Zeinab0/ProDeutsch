package com.example.moarefiprod.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

fun saveGameResultToFirestore(
    userId: String,
    gameId: String,
    correct: Int,
    wrong: Int
) {
    val db = FirebaseFirestore.getInstance()
    val resultData = hashMapOf(
        "gameId" to gameId,
        "correct" to correct,
        "wrong" to wrong,
        "timestamp" to Timestamp.now()
    )

    db.collection("users")
        .document(userId)
        .collection("game_results")
        .add(resultData)
}
