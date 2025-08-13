package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentSnapshot


data class Cards(
    val id: String = "", // 🆕 اضافه شده
    val title: String,
    val description: String,
    val count: Int,
    val price: String,
    val image: String,
    val isNew: Boolean = false // 🔥 اضافه کن برای تشخیص جدید بودن
)


enum class WordStatus {
    NEW, CORRECT, WRONG, IDK
}

data class Word(
    val id: String = "",
    val text: String = "",
    val translation: String = "",
    val status: WordStatus = WordStatus.NEW
)

fun observeFlashcardsRealtime(
    onlyNew: Boolean = false,
    orderByField: String? = null,
    onUpdate: (List<Cards>) -> Unit,
    onError: (Exception) -> Unit = {}
): ListenerRegistration {
    val db = FirebaseFirestore.getInstance()
    var query: Query = db.collection("flashcards")

    if (onlyNew) {
        query = query.whereEqualTo("isNew", true)
    }
    if (!orderByField.isNullOrBlank()) {
        query = query.orderBy(orderByField)
    }

    return query.addSnapshotListener { snapshot, e ->
        if (e != null) {
            onError(e)
            return@addSnapshotListener
        }

        val items = snapshot?.documents?.mapNotNull { it.toCardsOrNull() } ?: emptyList()
        onUpdate(items)
    }
}

/** مپ امنِ داکیومنت به مدل Cards */
private fun DocumentSnapshot.toCardsOrNull(): Cards? = try {
    Cards(
        id = id,
        title = getString("title") ?: "",
        description = getString("description") ?: "",
        count = getLong("count")?.toInt() ?: 0,
        price = getString("price") ?: "نامشخص",
        image = getString("image") ?: "",
        isNew = getBoolean("isNew") ?: false
    )
} catch (_: Exception) {
    null
}
