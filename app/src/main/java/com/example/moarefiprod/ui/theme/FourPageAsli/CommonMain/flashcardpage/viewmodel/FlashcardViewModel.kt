package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus

class FlashcardViewModel : ViewModel() {

    // لیست کلمات (برای نمایش و به‌روزرسانی در کل برنامه)
    var wordList = mutableStateListOf<Word>()
        private set

    // مقداردهی اولیه (مثلاً با لیست اولیه یا دیتابیس بعداً)
    fun loadWords(words: List<Word>) {
        wordList.clear()
        wordList.addAll(words)
    }

    // به‌روزرسانی وضعیت یک کلمه
    fun updateWordStatus(index: Int, status: WordStatus) {
        if (index in wordList.indices) {
            val word = wordList[index]
            wordList[index] = word.copy(status = status)
        }
    }

    // گرفتن کلمات فیلترشده برای مرور یا بررسی
    fun getWordsByStatus(statuses: Set<WordStatus>): List<Word> {
        return if (statuses.isEmpty()) wordList else wordList.filter { it.status in statuses }
    }
}