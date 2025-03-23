package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage

data class Cards(
    val title: String,
    val description: String,
    val zaman: String,
    val teadad: String,

    val price: Int,
    val image: Int,
    val isNew: Boolean = false // 🔥 اضافه کن برای تشخیص جدید بودن

)
