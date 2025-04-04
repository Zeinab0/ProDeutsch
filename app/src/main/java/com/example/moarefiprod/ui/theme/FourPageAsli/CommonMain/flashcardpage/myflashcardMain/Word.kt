package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

enum class WordStatus {
    CORRECT, WRONG, IDK, NEW
}

data class Word(
    val german: String,
    val persian: String,
    val status: WordStatus
)
