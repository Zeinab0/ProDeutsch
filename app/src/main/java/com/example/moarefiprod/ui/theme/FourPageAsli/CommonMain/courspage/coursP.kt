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
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun tamrinpage(
    navController: NavController,
    viewModel: CourseViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // مدیریت فیلتر
    var selectedFilter by remember { mutableStateOf("همه") }

    // جمع‌آوری داده‌ها از ViewModel
    val allCourses by viewModel.allCourses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(allCourses) {
        allCourses.forEach { course ->
            Log.d("TamrinPage", "Course: ${course.title}, isNew: ${course.isNew}")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f)
    ) {
        BannerSection(navController = navController)

        // متن دوره‌ها
        Text(
            text = ":دوره‌ها",
            fontSize = (screenWidth * 0.035f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .padding(top = 10.dp)
        )

        // فیلترها
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            val filters = listOf("جدید", "رایگان", "همه")
            filters.forEach { filter ->
                Spacer(modifier = Modifier.width(8.dp))
                FilterChips(
                    text = filter,
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter }
                )
            }
        }

        // نمایش دوره‌ها با فیلتر
        val filteredCourses = when (selectedFilter) {
            "رایگان" -> allCourses.filter { it.price == 0 }
            "جدید" -> allCourses.filter { it.isNew }
            else -> allCourses
        }

        if (isLoading) {
            Text("در حال بارگذاری...", color = Color.Gray, modifier = Modifier.fillMaxWidth().padding(16.dp))
        }
        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredCourses) { course ->
                Box {
                    CourseCard(course = course, navController = navController)
                    if (course.isNew) {
                        NewLabel()
                    }
                }
            }
        }
    }
}