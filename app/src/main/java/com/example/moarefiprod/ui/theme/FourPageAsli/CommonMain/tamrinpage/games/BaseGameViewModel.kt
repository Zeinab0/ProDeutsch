package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseGameViewModel : ViewModel() {

    private val _gameDurations = mutableListOf<Int>()
    private val _totalTimeInSeconds = MutableStateFlow(0)
    val totalTimeInSeconds: StateFlow<Int> = _totalTimeInSeconds

    // فقط ثبت نتیجه هر بازی
    fun recordMemoryGameResult(correct: Int, wrong: Int, timeInSeconds: Int) {
        _gameDurations.add(timeInSeconds)
        _totalTimeInSeconds.value = _gameDurations.sum()

        Log.d("ViewModel", "⏱ لیست زمان‌ها: $_gameDurations")
        Log.d("ViewModel", "🎯 مجموع زمان کل: ${_totalTimeInSeconds.value}")
    }

    // اگر بخوای مستقیم فقط زمان اضافه کنی
    fun resetTotalTime() {
        _gameDurations.clear()
        _totalTimeInSeconds.value = 0
    }
}

