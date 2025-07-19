// com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage/tamrinpage.kt
package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import FilterChips
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
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.data.allAppCourses // ⬅️ **این خط جدید اضافه شده است**

@Composable
fun tamrinpage(navController: NavController){

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var selectedFilter by remember { mutableStateOf("همه") } // ✅ مقدار اولیه

    // 🔴 **این خط جایگزین لیست sampleCourses قبلی شما شده است**
    val coursesToDisplay = allAppCourses // حالا از لیست جامع و کامل استفاده می‌کنیم

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f) // ✅ پدینگ کلی برای هماهنگی بهتر
    ) {
        // ✅ بنر تبلیغاتی
        BannerSection()

        // ✅ متن راست‌چین
        Text(
            text = ":دوره‌ها",
            fontSize = (screenWidth * 0.035f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
        )

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
        val filteredCourses = when (selectedFilter) {
            "رایگان" -> coursesToDisplay.filter { it.price == 0 } // استفاده از coursesToDisplay
            "جدید" -> coursesToDisplay.filter { it.isNew }     // استفاده از coursesToDisplay
            else -> coursesToDisplay                           // استفاده از coursesToDisplay
        }

        // ✅ فقط لیست دوره‌ها اسکرول میشه
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