package com.example.moarefiprod.data

data class TextPicData(
    val imageUrl: String,
    val title: String,         // اضافه کردن فیلد title
    val order: Int,           // اضافه کردن فیلد order
    val type: String,         // اضافه کردن فیلد type
    val words: List<TextPicWord>
)

data class TextPicWord(
    val word: String,
    val isCorrect: Boolean
)