package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class Story(
    val id: String = "",
    val title: String = "",
    val level: String = "",
    val price: String = "",
    val duration: String = "", // تعداد صفحه
    val author: String = "",
    val summary: String = "",
    val content: String = "",
    val imageUrl: String = ""
)
fun getStoriesFromFirestore(callback: (List<Story>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("stories")
        .get()
        .addOnSuccessListener { result ->
            val storyList = result.documents.mapNotNull { doc ->
                val story = doc.toObject(Story::class.java)
                story?.copy(id = doc.id)
            }
            callback(storyList)
        }
        .addOnFailureListener {
            callback(emptyList())
        }
}
// import لازم: com.google.firebase.firestore.*  و  com.google.firebase.firestore.ktx.firestore (در صورت نیاز)
 fun listenStoriesLive(onChange: (List<Story>) -> Unit): ListenerRegistration {
    return FirebaseFirestore.getInstance()
        .collection("stories")
        .addSnapshotListener { snap, e ->
            if (e != null) return@addSnapshotListener
            val list = snap?.documents?.mapNotNull { d ->
                d.toObject(Story::class.java)?.copy(id = d.id)
            } ?: emptyList()
            onChange(list)
        }
}