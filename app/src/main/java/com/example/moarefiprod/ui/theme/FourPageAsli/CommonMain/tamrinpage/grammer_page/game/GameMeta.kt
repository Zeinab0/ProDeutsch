package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game


data class GameMeta(
    val id: String,
    val type: String,
    val routeData: Map<String, String> = emptyMap() // برای courseId, lessonId و...
)


data class GrammarGame(
    val id: String = "",
    val type: String = "",
    val order: Int = 0
)
