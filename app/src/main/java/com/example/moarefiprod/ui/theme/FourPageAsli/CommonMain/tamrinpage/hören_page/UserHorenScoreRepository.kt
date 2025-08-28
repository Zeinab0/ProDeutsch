package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserHorenScoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    fun listenScores(): Flow<Map<String, Int>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {

            trySend(emptyMap())
            awaitClose { }
            return@callbackFlow
        }

        val reg = db.collection("users")
            .document(uid)
            .collection("hör_scores")
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err); return@addSnapshotListener
                }
                val map = snap?.documents?.associate { d ->
                    d.id to (d.getLong("score")?.toInt() ?: -1)
                } ?: emptyMap()
                trySend(map)
            }

        awaitClose { reg.remove() }
    }

    suspend fun setScore(exerciseId: String, score: Int) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf(
            "score" to score,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        db.collection("users")
            .document(uid)
            .collection("hör_scores")
            .document(exerciseId)
            .set(data, SetOptions.merge())
            .await()
    }
}
