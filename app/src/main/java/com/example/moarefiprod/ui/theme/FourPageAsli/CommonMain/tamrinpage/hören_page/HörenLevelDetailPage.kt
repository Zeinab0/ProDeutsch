package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören

import FilterChips
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.HörExercise
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.HörExerciseWithId
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.HörenViewModel

@Composable
fun HörenLevelDetailPage(navController: NavController, level: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var selectedFilter by remember { mutableStateOf("همه") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedExerciseByClick by remember { mutableStateOf<HörExerciseWithId?>(null) }
    var selectedExerciseRandom by remember { mutableStateOf<HörExerciseWithId?>(null) }
    var dialogSource by remember { mutableStateOf("click") } // or "random"

    val viewModel: HörenViewModel = viewModel()
    val exercises by viewModel.exercises.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExercises(level)
    }

    val filteredExercises = when (selectedFilter) {
        "مطالعه شده ها" -> exercises.filter { it.exercise.score != null }
        "مطالعه نشده ها" -> exercises.filter { it.exercise.score == null }
        else -> exercises
    }
    val exerciseToShow = if (dialogSource == "random") selectedExerciseRandom else selectedExerciseByClick


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = screenWidth * 0.05f)
        ) {
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
                            AsyncImage(
                                model = item.exercise.imageUrl,
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
                                    text = item.exercise.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = iranSans,
                                    modifier = Modifier.align(Alignment.End)
                                )

                                val (statusText, statusColor) = when {
                                    item.exercise.score == null -> "شروع" to Color(0xFF7AB2B2)
                                    item.exercise.score < 60 -> "دوباره امتحان کن" to Color(0xFFFFA26D)
                                    else -> "تکرار مجدد" to Color(0xFF00BD10)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .padding(bottom = 6.dp, top = 6.dp)
                                                .height(20.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(statusColor)
                                                .padding(horizontal = 6.dp)
                                                .clickable {
                                                    selectedExerciseByClick = item
                                                    dialogSource = "click"
                                                    showDialog = true
                                                },
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
                                    }

                                    Column(
                                        modifier = Modifier.padding(bottom = 6.dp),
                                        horizontalAlignment = Alignment.End,
                                        verticalArrangement = Arrangement.spacedBy(0.dp)
                                    ) {
                                        Text(
                                            text = if (item.exercise.score != null) "مطالعه شده" else "مطالعه نشده",
                                            fontSize = 10.sp,
                                            color = Color(0xFF828282),
                                            fontFamily = iranSans,
                                            fontWeight = FontWeight.Light
                                        )
                                        Text(
                                            text = if (item.exercise.score != null) "نمره شما: ${item.exercise.score}/100" else "نمره ندارد",
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
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
                .size(70.dp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val frameworkPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.TRANSPARENT
                            setShadowLayer(20f, 0f, 0f, android.graphics.Color.BLACK)
                        }
                        this.drawContext.canvas.nativeCanvas.apply {
                            save()
                            drawCircle(size.width / 2, size.height / 2, size.width / 2.2f, frameworkPaint)
                            restore()
                        }
                    }
                }
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF90CECE))
                .clickable {
                    if (filteredExercises.isNotEmpty()) {
                        selectedExerciseRandom = filteredExercises.random()
                        dialogSource = "random"
                        showDialog = true
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.random),
                contentDescription = "رندم",
                tint = Color(0xFF4D869C),
                modifier = Modifier.size(24.dp)
            )
        }
    }

// فایل کامل با دیالوگ تفکیک شده برای رندم و کلیک جدا
// دیالوگ برای تمرین کلیک‌شده و دیالوگ برای تمرین تصادفی جداگانه نوشته شده‌اند

// دیالوگ برای تمرین رندم:
    if (showDialog && dialogSource == "random" && selectedExerciseRandom != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "تمرین انتخابی: ${selectedExerciseRandom?.exercise?.title}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )


                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "می‌خوای همین تمرین رو شروع کنی؟",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .clickable { showDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("خروج", color = Color.Red, fontFamily = iranSans)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF7AB2B2))
                                .height(45.dp)
                                .clickable {
                                    showDialog = false
                                    navController.navigate("audio_test/$level/${selectedExerciseRandom?.id}")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("شروع", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }

// دیالوگ برای تمرین کلیک‌شده:
    if (showDialog && dialogSource == "click" && selectedExerciseByClick != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "شروع کنیم؟",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "هر آزمون شنیداری شامل یک صدا می باشد که شما میتوانید فقط سه دفعه آن را بشنوید\nسپس با توجه به آن باید به سوالات پاسخ دهید",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .clickable { showDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("خروج", color = Color.Red, fontFamily = iranSans)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF7AB2B2))
                                .height(45.dp)
                                .clickable {
                                    showDialog = false
                                    navController.navigate("audio_test/$level/${selectedExerciseByClick?.id}")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("شروع", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }
}



fun Modifier.evenShadow(
    color: Color = Color.Black,
    radius: Float = 20f,
    cornerRadius: Float = 20f
): Modifier = this.then(
    Modifier.drawBehind {
        val shadowColor = color.copy(alpha = 0.3f).toArgb()
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.color = android.graphics.Color.TRANSPARENT
            setShadowLayer(radius, 0f, 0f, shadowColor)
        }
        drawIntoCanvas {
            it.nativeCanvas.drawRoundRect(
                0f,
                0f,
                size.width,
                size.height,
                cornerRadius,
                cornerRadius,
                paint
            )
        }
    }
)