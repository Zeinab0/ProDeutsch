package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GrammarViewModel : ViewModel() {
    private val repo = GrammarRepository()

    private val _topics = MutableStateFlow<List<GrammarTopic>>(emptyList())
    val topics: StateFlow<List<GrammarTopic>> = _topics

    init {
        viewModelScope.launch {
            repo.getGrammarTopics().collect {
                _topics.value = it
            }
        }
    }

//    // برای آینده: گرفتن بازی‌ها برای یک مبحث خاص
//    fun getGames(topicId: String): Flow<List<GrammarGame>> {
//        return repo.getGamesForTopic(topicId)
//    }
}
