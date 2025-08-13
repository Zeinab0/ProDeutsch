package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel.FlashcardViewModel

@Composable
fun flashCard(
    cards: Cards,
    navController: NavController,
    manageMode: Boolean = false,
    viewModel: FlashcardViewModel = viewModel()
) {
    val myCardIds by viewModel.myCardIds.collectAsState()
    val inMyList = myCardIds.contains(cards.id)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardHeight = screenWidth * 0.3f


    Box { // ← برای قرار دادن لیبل روی کارت
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = rememberAsyncImagePainter(cards.image),
                    contentDescription = "Course Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(cardHeight)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                // ستون دکمه‌ها و قیمت
                Column(
                    modifier = Modifier
                        .width(95.dp)
                        .align(Alignment.Bottom)
                        .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = cards.price,
                        fontSize = 10.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                        color = if (cards.price == "رایگان") Color(0xFF2E7D32) else Color(0xFF000000),
                        textAlign = TextAlign.Right,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )

                    if (manageMode && inMyList) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("card_id", cards.id)
                                    navController.navigate("word_progress_page")
                                },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C))
                            ) {
                                Text("ادامه", color = Color.White, fontSize = 9.sp,
                                    fontFamily = iranSans, fontWeight = FontWeight.Medium)
                            }

                            OutlinedButton(
                                onClick = { viewModel.removeCardFromMyList(cards.id) },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("لغو", fontSize = 9.sp, fontFamily = iranSans,
                                    fontWeight = FontWeight.Medium)
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                println("start clicked")  // برای تست لاگ

                                // 1) آیدی کارت را بده به صفحه مقصد
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("card_id", cards.id)

                                // 2) برو به صفحه
                                navController.navigate("word_progress_page")

                                // 3) (اختیاری) بعد از ناوبری کارت را به «لیست من» اضافه کن
                                viewModel.addCardToMyList(cards)
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(22.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C))
                        ) {
                            Text(
                                text = "شروع دوره",
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White,
                                fontSize = 8.sp,
                                fontFamily = iranSans,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // ستون عنوان/توضیح/تعداد
                Column(
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 10.dp, 10.dp)
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = cards.title,
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Right,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = cards.description,
                        fontFamily = iranSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontFamily = iranSans)) {
                                append("تعداد کلمات:   ")
                            }
                            withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontFamily = iranSans)) {
                                append("${cards.count}")
                            }
                        },
                        fontSize = 10.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
    }
}

@Composable
fun NewLabel(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .offset(x = (-10).dp, y = 6.dp)
            .rotate(-40f)
            .background(Color.Red, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "جدید",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans
        )
    }
}
