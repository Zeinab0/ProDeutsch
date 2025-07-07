package com.example.moarefiprod.data

data class SentenceGameData(
    val gameType: String = "",
    val question: String = "",
    val correctSentence: List<String> = emptyList(),
    val wordPool: List<String> = emptyList()
)
