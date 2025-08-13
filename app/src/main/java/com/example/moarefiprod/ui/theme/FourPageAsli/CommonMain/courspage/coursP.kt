package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import FilterChips
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun tamrinpage(
    navController: NavController,
    query: String = "",
    viewModel: CourseViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // فیلتر تب‌ها
    var selectedFilter by remember { mutableStateOf("همه") }

    // داده‌ها از ویومدل
    val allCourses by viewModel.allCourses.collectAsState()
    val isLoading  by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // لاگ کمکی (اختیاری)
    LaunchedEffect(allCourses) {
        allCourses.forEach { c ->
            Log.d("TamrinPage", "Course: ${c.title}, isNew=${c.isNew}, price=${c.price}")
        }
    }

    // کمک‌تابع: نرمال‌سازی قیمت برای فیلتر «رایگان»
    fun isFree(price: Any?): Boolean {
        val s = price?.toString()?.trim()?.lowercase() ?: return false
        return s == "0" || s == "free" || s == "frei" || s == "رایگان" || s == "مجانی"
    }

    // ---------- فیلتر + سرچ ----------
    val filteredByTab = remember(allCourses, selectedFilter) {
        when (selectedFilter) {
            "رایگان" -> allCourses.filter { isFree(it.price) }
            "جدید"   -> allCourses.filter { it.isNew }
            else     -> allCourses
        }
    }

    val q = remember(query) { query.trim().lowercase() }
    val filteredByQuery = remember(filteredByTab, q) {
        if (q.isEmpty()) filteredByTab
        else {
            filteredByTab.filter { c ->
                // title/description ممکنه نال باشن؟ با ?.
                (c.title?.lowercase()?.contains(q) == true) ||
                        (c.description?.lowercase()?.contains(q) == true)
            }
        }
    }

    // مرتب‌سازی امن بر اساس order (اگر نبود، ته لیست)
    val orderedCourses = remember(filteredByQuery) {
        filteredByQuery.sortedBy { it.order ?: Int.MAX_VALUE }
    }
    // ----------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f)
    ) {
        BannerSection(navController = navController)

        Text(
            text = " : دوره ها ",
            fontSize = (screenWidth * 0.035f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .padding(top = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            listOf("جدید", "رایگان", "همه").forEach { filter ->
                Spacer(modifier = Modifier.width(8.dp))
                FilterChips(
                    text = filter,
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter }
                )
            }
        }

        if (isLoading) {
            Text(
                "در حال بارگذاری...",
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(orderedCourses, key = { it.id }) { course ->
                Box {
                    CourseCard(course = course, navController = navController)

                    if (course.isNew) {
                        NewLabel(
                            modifier = Modifier
                                .align(AbsoluteAlignment.TopLeft)
                                .zIndex(1f)
                                .offset(x = (-12).dp, y = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
