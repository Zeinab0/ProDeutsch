package com.example.moarefiprod.ui.theme.logofirst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {
    var currentPage by mutableStateOf(0) // 0 = advertisement1، 1 = advertisement2، 2 = advertisement3
}