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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class FlashcardViewModel : ViewModel() {

    // ---------- فقط اگر در جای دیگری نیاز داری ----------
    var wordList = mutableStateListOf<Word>()
        private set

    fun loadWords(words: List<Word>) { wordList.clear(); wordList.addAll(words) }
    fun updateWordStatus(index: Int, status: WordStatus) {
        if (index in wordList.indices) wordList[index] = wordList[index].copy(status = status)
    }
    fun getWordsByStatus(statuses: Set<WordStatus>) =
        if (statuses.isEmpty()) wordList else wordList.filter { it.status in statuses }
    // -----------------------------------------------------

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // «کلمات من»
    private val _myCards = MutableStateFlow<List<Cards>>(emptyList())
    val myCards: StateFlow<List<Cards>> = _myCards.asStateFlow()

    private val _myCardIds = MutableStateFlow<Set<String>>(emptySet())
    val myCardIds: StateFlow<Set<String>> = _myCardIds.asStateFlow()

    private var myCardsListener: ListenerRegistration? = null

    // همه‌ی کارت‌ها (مرجع)
    private val _allCards = MutableStateFlow<List<Cards>>(emptyList())
    val allCards: StateFlow<List<Cards>> = _allCards.asStateFlow()
    private var allCardsListener: ListenerRegistration? = null

    // خریدها
    private val _purchasedIds = MutableStateFlow<Set<String>>(emptySet())
    val purchasedIds: StateFlow<Set<String>> = _purchasedIds.asStateFlow()
    private var purchasesListener: ListenerRegistration? = null

    // کارت‌های خریداری‌شده (برای UI)
    val purchasedCards: StateFlow<List<Cards>> =
        combine(purchasedIds, allCards) { ids, all ->
            if (ids.isEmpty()) emptyList() else all.filter { it.id in ids }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        listenAllCards()
        listenMyCards()
        listenPurchased()
    }

    private fun listenMyCards() {
        val uid = auth.currentUser?.uid ?: run {
            _myCards.value = emptyList()
            _myCardIds.value = emptySet()
            return
        }
        myCardsListener?.remove()
        myCardsListener = db.collection("users").document(uid)
            .collection("purchases_flashcards")
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

    fun listenAllCards() {
        allCardsListener?.remove()
        allCardsListener = db.collection("flashcards")
            .addSnapshotListener { snap, e ->
                if (e != null) return@addSnapshotListener
                val list = snap?.documents?.mapNotNull { d ->
                    try {
                        Cards(
                            id = d.id,
                            title = d.getString("title") ?: "",
                            description = d.getString("description") ?: "",
                            count = (d.getLong("count") ?: 0L).toInt(),
                            price = d.getString("price") ?: "نامشخص",
                            image = d.getString("image") ?: "",
                            isNew = d.getBoolean("isNew") ?: false
                        )
                    } catch (_: Exception) { null }
                } ?: emptyList()
                _allCards.value = list
            }
    }

    private fun listenPurchased() {
        val uid = auth.currentUser?.uid ?: run {
            _purchasedIds.value = emptySet()
            return
        }
        purchasesListener?.remove()
        purchasesListener = db.collection("users")
            .document(uid)
            .collection("purchases_flashcards")
            .addSnapshotListener { qs, _ ->
                _purchasedIds.value = qs?.documents?.map { it.id }?.toSet() ?: emptySet()
            }
    }

    fun markPurchased(cardId: String, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(uid)
            .collection("purchases_flashcards")
            .document(cardId)
            .set(mapOf("purchasedAt" to System.currentTimeMillis()))
            .addOnSuccessListener { onDone?.invoke() }
    }

    fun addCardToMyList(card: Cards, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf(
            "title" to card.title,
            "description" to card.description,
            "count" to card.count,
            "price" to card.price,
            "image" to card.image,
            "isNew" to card.isNew,
            "addedAt" to System.currentTimeMillis()
        )
        db.collection("users").document(uid)
            .collection("purchases_flashcards").document(card.id)
            .set(data).addOnSuccessListener { onDone?.invoke() }
    }

    fun removeCardFromMyList(cardId: String, onDone: (() -> Unit)? = null) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .collection("purchases_flashcards").document(cardId)
            .delete().addOnSuccessListener { onDone?.invoke() }
    }

    override fun onCleared() {
        myCardsListener?.remove()
        allCardsListener?.remove()
        purchasesListener?.remove()
        super.onCleared()
    }
}
