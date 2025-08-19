package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.TextStyle

@Composable
fun WordsScreen(
    courseId: String,
    lessonId: String,
    contentId: String,
    navController: NavController,
    viewModel: DarsViewModel = viewModel(),
    rowHeight: Dp = 44.dp
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val tableWidth = screenWidth * 0.86f
    val headerTopPadding = screenHeight * 0.15f
    val tableMaxHeight = screenHeight * 0.62f

    // لود دیتا بر اساس ساختار شما
    LaunchedEffect(contentId) {
        viewModel.loadWords(courseId, lessonId, contentId)
    }

    val title = viewModel.wordsTitle.collectAsState().value
    val rows = viewModel.words.collectAsState().value
    val isLoading = viewModel.isWordsLoading.collectAsState().value
    val error = viewModel.wordsError.collectAsState().value

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // دکمه برگشت (مثل جزوه)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            error != null -> {
                Text(
                    text = "خطا: $error",
                    color = Color.Red,
                    fontFamily = iranSans,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                // تیتر + جدول
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = headerTopPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // تیتر + آیکن
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                         .padding(horizontal = 30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End // چیدمان به سمت راست
                    ) {
                        Text(
                            text = title,
                            fontSize = (screenWidth * 0.04f).value.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = iranSans,
                            color = Color.Black,
                            textAlign = TextAlign.End, // راست‌چین متن
                            style = TextStyle(textDirection = TextDirection.Rtl),
                           // modifier = Modifier.weight(1f) // متن فضا بگیره و آیکن بچسبه به راست
                        )
                        Spacer(modifier = Modifier.width(10.dp)) // فاصله‌ی مناسب بین تیتر و آیکن
                        Icon(
                            painter = painterResource(id = R.drawable.words),
                            contentDescription = "document",
                            tint = Color(0xFF4D869C),
                            modifier = Modifier.size(screenWidth * 0.08f)
                        )
                    }

                    Spacer(modifier = Modifier.height(35.dp))

                    // جدول اسکرول‌دار
                    Box(
                        modifier = Modifier
                            .width(tableWidth)
                            .heightIn(max = tableMaxHeight)
                            .border(1.dp, Color.Gray, RoundedCornerShape(2.dp))
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = listState
                        ) {
                            itemsIndexed(rows) { index, item ->
                                // ستون چپ: آلمانی (item.text) | ستون راست: فارسی (item.translation)
                                WordsRow(
                                    de = item.text,
                                    fa = item.translation,
                                    height = rowHeight,
                                    dividerColor = Color.Gray
                                )
                                if (index < rows.lastIndex) {
                                    Divider(thickness = 1.dp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// همان WordsRow قبلی‌ات
@Composable
private fun WordsRow(
    de: String,
    fa: String,
    height: Dp,
    dividerColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ستون چپ: آلمانی
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = de, fontSize = 13.sp, color = Color.Black, fontFamily = iranSans)
        }

        // خط عمودی وسط
        Box(
            modifier = Modifier
                .width(0.5.dp)
                .fillMaxHeight()
                .background(dividerColor)
        )

        // ستون راست: فارسی
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 12.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(text = fa, fontSize = 13.sp, color = Color.Black, fontFamily = iranSans)
        }
    }
}