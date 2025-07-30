package com.example.moarefiprod.data

data class MultipleChoice(
    val question: String,
    val translation: String,
    val options: List<String>,
    val correctIndex: Int
)
