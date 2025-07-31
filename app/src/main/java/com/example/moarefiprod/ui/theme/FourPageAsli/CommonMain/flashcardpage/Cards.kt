package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage
import com.google.firebase.firestore.FirebaseFirestore

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
