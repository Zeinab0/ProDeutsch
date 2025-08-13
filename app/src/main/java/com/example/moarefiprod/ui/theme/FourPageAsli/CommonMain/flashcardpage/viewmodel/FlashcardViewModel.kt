package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlashcardViewModel : ViewModel() {

    // -------------------- کلمات (همون قبلیِ خودت) --------------------
    var wordList = mutableStateListOf<Word>()
        private set

    fun loadWords(words: List<Word>) {
        wordList.clear()
        wordList.addAll(words)
    }

    fun updateWordStatus(index: Int, status: WordStatus) {
        if (index in wordList.indices) {
            val w = wordList[index]
            wordList[index] = w.copy(status = status)
        }
    }

    fun getWordsByStatus(statuses: Set<WordStatus>): List<Word> {
        return if (statuses.isEmpty()) wordList else wordList.filter { it.status in statuses }
    }

    // -------------------- «کلمات من» (لیست کارت‌های شروع‌شده‌ی کاربر) --------------------
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _myCards = MutableStateFlow<List<Cards>>(emptyList())
    val myCards: StateFlow<List<Cards>> = _myCards.asStateFlow()

    private val _myCardIds = MutableStateFlow<Set<String>>(emptySet())
    val myCardIds: StateFlow<Set<String>> = _myCardIds.asStateFlow()

    private var myCardsListener: ListenerRegistration? = null

    private var allCardsListener: ListenerRegistration? = null

    private val _allCards = MutableStateFlow<List<Cards>>(emptyList())
    val allCards: StateFlow<List<Cards>> = _allCards.asStateFlow()

    init {
        listenMyCards()
    }

    private fun listenMyCards() {
        val uid = auth.currentUser?.uid ?: run {
            // اگر لاگین نیست: استیت را خالی نگه دار
            _myCards.value = emptyList()
            _myCardIds.value = emptySet()
            return
        }

        // اگر قبلاً لیسنر داشتیم، حذفش کن
        myCardsListener?.remove()

        myCardsListener = db.collection("users")
            .document(uid)
            .collection("my_flashcards")
            .addSnapshotListener { qs, _ ->
                val list = qs?.documents?.map { d ->
                    Cards(
                        id = d.id,
                        title = d.getString("title") ?: "",
                        description = d.getString("description") ?: "",
                        count = (d.getLong("count") ?: 0).toInt(),
                        price = d.getString("price") ?: "",
                        image = d.getString("image") ?: "",
                        isNew = d.getBoolean("isNew") ?: false
                    )
                } ?: emptyList()

                _myCards.value = list
                _myCardIds.value = list.map { it.id }.toSet()
            }
    }

    fun addCardToMyList(card: Cards, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return
        val ref = db.collection("users").document(uid)
            .collection("my_flashcards").document(card.id)

        // برای رندر سریع‌تر، چند فیلد اصلی را نگه می‌داریم
        val data = mapOf(
            "title" to card.title,
            "description" to card.description,
            "count" to card.count,
            "price" to card.price,
            "image" to card.image,
            "isNew" to card.isNew
        )
        ref.set(data).addOnSuccessListener { onDone?.invoke() }
    }

    fun removeCardFromMyList(cardId: String, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .collection("my_flashcards").document(cardId)
            .delete()
            .addOnSuccessListener { onDone?.invoke() }
    }

    fun isCardEnrolled(cardId: String): Boolean = myCardIds.value.contains(cardId)

    fun listenAllCards() {
        allCardsListener?.remove()

        allCardsListener = FirebaseFirestore.getInstance()
            .collection("flashcards")
            .addSnapshotListener { snap, e ->
                if (e != null) return@addSnapshotListener
                val list = snap?.documents?.mapNotNull { d ->
                    try {
                        Cards(
                            id = d.id,
                            title = d.getString("title") ?: "",
                            description = d.getString("description") ?: "",
                            count = (d.getLong("count") ?: 0).toInt(),
                            price = d.getString("price") ?: "نامشخص",
                            image = d.getString("image") ?: "",
                            isNew = d.getBoolean("isNew") ?: false
                        )
                    } catch (_: Exception) { null }
                } ?: emptyList()
                _allCards.value = list
            }
    }

    override fun onCleared() {
        myCardsListener?.remove()
        allCardsListener?.remove() // اینم اضافه بشه
        super.onCleared()
    }
}
