package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GrammarRepository {

    private val db = FirebaseFirestore.getInstance()
    private val grammarRef = db.collection("grammar_topics")

    fun getGrammarTopics(): Flow<List<GrammarTopic>> = callbackFlow {
        val listener = grammarRef
            .orderBy("order")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    val item = doc.toObject(GrammarTopic::class.java)
                    item?.copy(id = doc.id) // ← ذخیره آیدی برای استفاده بعدی
                } ?: emptyList()

                trySend(list)
            }

        awaitClose { listener.remove() }
    }

    // دریافت بازی‌های مربوط به یک موضوع خاص
//    fun getGamesForTopic(topicId: String): Flow<List<GrammarGame>> = callbackFlow {
//        val listener = db.collection("grammar_topics")
//            .document(topicId)
//            .collection("games")
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    close(error)
//                    return@addSnapshotListener
//                }
//
//                val games = snapshot?.toObjects(GrammarGame::class.java) ?: emptyList()
//                trySend(games)
//            }
//
//        awaitClose { listener.remove() }
//    }
}
