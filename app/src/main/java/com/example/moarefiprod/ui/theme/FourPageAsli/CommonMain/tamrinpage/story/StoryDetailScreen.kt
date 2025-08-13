package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun StoryDetailScreen(
    id: String,
    title: String,
    author: String,
    duration: String,
    price: String,
    summary: String,
    imageUrl: String,
    onBack: () -> Unit,
    navController: NavController // اضافه شده
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp


    var isPurchased by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB4E0E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFD4F0F0), Color(0xFF52D3D3))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔙 دکمه برگشت و تصویر و عنوان
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(top = screenHeight * 0.05f, start = 16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(screenWidth * 0.09f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = screenHeight * 0.1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = screenWidth * 0.56f)
                            .heightIn(max = screenHeight * 0.32f)
                            .shadow(34.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = title,
                        fontFamily = iranSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 🧾 جزییات داستان
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .shadow(
                        22.dp,
                        shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                    .background(Color.White, shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(45.dp))

                    // 🔤 عنوان‌های بالا
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("زمان مطالعه", "نویسنده", "قیمت").forEach {
                            Text(
                                text = it,
                                fontFamily = iranSans,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 🔢 مقادیر بالا
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            duration,
                            author,
                            if (isPurchased) "خریداری شده" else price
                        ).forEach {
                            Text(
                                text = it,
                                fontFamily = iranSans,
                                fontSize = 14.sp,
                                color = Color(0xFF7AB2B2),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = TextStyle(textDirection = TextDirection.Rtl)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "خلاصه داستان:",
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = summary,
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF7AB2B2),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        maxLines = 7,
                        minLines = 7,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // دکمه خرید
                    if (!isPurchased && price != "Frei") {
                        Box(
                            modifier = Modifier
                                .width(screenWidth * 0.33f)
                                .height(screenHeight * 0.07f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF7AB2B2))
                                .clickable { isPurchased = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "خرید",
                                color = Color.White,
                                fontFamily = iranSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                }
            }
        }

        // 🟦 دکمه شروع مطالعه که وسط دو بخش بیاد
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 26.dp)
                .zIndex(2f)
                .clickable {
                    if (id.isNotBlank()) {
                        navController.navigate("reading_screen/$id")
                    } else {
                        Log.e("StoryDetailScreen", "⛔ ID داستان خالی است و نمی‌توان ادامه داد.")
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .width(screenWidth * 0.45f)
                    .height(screenHeight * 0.07f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF7AB2B2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "شروع مطالعه",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium
                )
            }
        }
     }
}