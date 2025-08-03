package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseGameViewModel : ViewModel() {

    // مثلاً تایمر یا امتیاز کلی یا داده‌های مشترک
    protected val _totalTimeInSeconds = MutableStateFlow(0)
    val totalTimeInSeconds: StateFlow<Int> = _totalTimeInSeconds


    fun recordMemoryGameResult(correct: Int, wrong: Int, timeInSeconds: Int) {
        Log.d("BaseGameViewModel", "MemoryGame Result -> Correct: $correct, Wrong: $wrong, Time: $timeInSeconds")
        _totalTimeInSeconds.value += timeInSeconds
        // اینجا می‌تونی ارسال نتیجه به Firestore یا سرور هم اضافه کنی اگر خواستی
    }

    // در صورت نیاز، متدهای مشترک مثل logResult(), trackProgress(), etc
}
