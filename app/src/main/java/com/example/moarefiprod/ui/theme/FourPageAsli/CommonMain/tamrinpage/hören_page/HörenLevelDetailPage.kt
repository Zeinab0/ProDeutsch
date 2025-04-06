package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören

import FilterChips
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.foundation.layout.*


@Composable
fun HörenLevelDetailPage(navController: NavController, level: String) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var selectedFilter by remember { mutableStateOf("همه") }

    val exercises = listOf(
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, null),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, 45),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, 0),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, 60),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, 100),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, 85),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, null),
        HörExercise("مصاحبه در فرودگاه", R.drawable.baner, 1, null),
    )

    val filteredExercises = when (selectedFilter) {
        "مطالعه شده ها" -> exercises.filter { it.score != null }
        "مطالعه نشده ها" -> exercises.filter { it.score == null }
        else -> exercises
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ⬇️ محتوای اصلی
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = screenWidth * 0.05f)
        ) {

            // Header
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(top = screenHeight * 0.05f)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(screenWidth * 0.09f)
                    )
                }
            }

            Text(
                text = ":دوره‌ها",
                fontSize = (screenWidth * 0.035f).value.sp,
                fontWeight = FontWeight.Bold,
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
                val filters = listOf("مطالعه شده ها", "مطالعه نشده ها", "همه")
                filters.forEach { filter ->
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChips(
                        text = filter,
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(filteredExercises) { item ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFCDE8E5))
                            .clickable { }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f)
                                    .padding(10.dp, 10.dp, 10.dp, 0.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = iranSans,
                                    modifier = Modifier.align(Alignment.End)
                                )

                                Text(
                                    text = if (item.score != null) "مطالعه شده" else "مطالعه نشده",
                                    fontSize = 10.sp,
                                    color = Color(0xFF828282),
                                    fontFamily = iranSans,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.align(Alignment.End)
                                )

                                val (statusText, statusColor) = when {
                                    item.score == null -> "شروع" to Color(0xFF7AB2B2)
                                    item.score < 60 -> "دوباره امتحان کن" to Color(0xFFFFA26D)
                                    else -> "تکرار مجدد" to Color(0xFF00BD10)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                            .height(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(statusColor)
                                            .padding(horizontal = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = statusText,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontFamily = iranSans,
                                            maxLines = 1
                                        )
                                    }

                                    Text(
                                        text = if (item.score != null) "نمره شما: ${item.score}/100" else "نمره ندارد",
                                        fontSize = 10.sp,
                                        color = Color.Black,
                                        fontFamily = iranSans,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ✅ دکمه شناور فقط یکبار، روی کل صفحه، پایین سمت راست
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
                .size(70.dp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val frameworkPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.TRANSPARENT
                            setShadowLayer(
                                20f,  // بلور
                                0f, 0f,  // افست
                                android.graphics.Color.BLACK  // رنگ سایه
                            )
                        }

                        this.drawContext.canvas.nativeCanvas.apply {
                            save()
                            drawCircle(
                                size.width / 2,
                                size.height / 2,
                                size.width / 2.2f,
                                frameworkPaint
                            )
                            restore()
                        }
                    }
                }
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF90CECE))
                .clickable {
                    // عملکرد دکمه
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.random),
                contentDescription = "افزودن",
                tint = Color(0xFF4D869C),
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

data class HörExercise(
    val title: String,
    val imageRes: Int,
    val isRead: Int,
    val score: Int?
)
