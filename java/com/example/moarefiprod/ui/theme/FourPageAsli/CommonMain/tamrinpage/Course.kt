package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

data class Course(
    val title: String,
    val description: String,
    val sath: String,
    val zaman: String,
    val teadad: String,

    val price: Int,
    val image: Int,
    val isNew: Boolean = false // 🔥 اضافه کن برای تشخیص جدید بودن

)
