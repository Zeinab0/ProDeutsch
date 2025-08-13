package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie

import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@Keep
data class Movie(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val level: String = "",
    val duration: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val videoUrl: String = ""

)

fun getMoviesFromFirestore(
    onResult: (List<Movie>) -> Unit
) {
    FirebaseFirestore.getInstance()
        .collection("movies")
        .get()

        .addOnSuccessListener { snapshot ->
            val movies = snapshot.documents.mapNotNull {
                val movie = it.toObject(Movie::class.java)
                Log.d("FirebaseData", "Fetched: ${movie?.title}")
                movie?.copy(id = it.id)
            }
            Log.d("FirebaseCount", "Movies count: ${movies.size}")
            onResult(movies)
        }
        .addOnFailureListener {
            Log.e("FirebaseError", "Failed to fetch: ${it.message}")
            onResult(emptyList())
        }

}

 fun listenMoviesLive(onChange: (List<Movie>) -> Unit): ListenerRegistration {
    return FirebaseFirestore.getInstance()
        .collection("movies")
        .addSnapshotListener { snap, e ->
            if (e != null) return@addSnapshotListener
            val list = snap?.documents?.mapNotNull { d ->
                d.toObject(Movie::class.java)?.copy(id = d.id)
            } ?: emptyList()
            onChange(list)
        }
}

