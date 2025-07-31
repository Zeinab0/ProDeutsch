package com.example.moarefiprod.data

data class TextPicData(
    val imageUrl: String,
    val words: List<TextPicWord>
)

data class TextPicWord(
    val word: String,
    val isCorrect: Boolean
)