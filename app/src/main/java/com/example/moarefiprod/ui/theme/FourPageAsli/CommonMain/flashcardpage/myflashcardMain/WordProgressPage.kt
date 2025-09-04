package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun WordProgressPage(navController: NavController) {
    var selectedStatuses by remember { mutableStateOf(setOf<WordStatus>()) }
    val cardId = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("card_id") // الان این میشه: "sample1"

    var words by remember { mutableStateOf<List<Word>>(emptyList()) }
    var cardTitle by remember { mutableStateOf("بدون عنوان") }

    LaunchedEffect(cardId) {
        cardId?.let { id ->
            // 🟢 گرفتن عنوان کارت از Firestore
            FirebaseFirestore.getInstance()
                .collection("flashcards")
                .document(id)
                .get()
                .addOnSuccessListener { doc ->
                    val title = doc.getString("title")
                    if (title != null) {
                        cardTitle = title
                    }
                }

            // 🟢 گرفتن لیست کلمات همان فلش‌کارت
            fetchWordsForCard(id) { loadedWords ->
                words = loadedWords
            }
        }
    }

    val allWords = words

    val wrongCount = allWords.count { it.status == WordStatus.WRONG }
    val idkCount = allWords.count { it.status == WordStatus.IDK }
    val newCount = allWords.count { it.status == WordStatus.NEW }

    val filteredWords = if (selectedStatuses.isEmpty()) allWords
    else allWords.filter { it.status in selectedStatuses }


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val correctCount = words.count { it.status == WordStatus.CORRECT }
    val total = words.size
    val correctPercentage = if (total > 0) (correctCount * 100 / total) else 0

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        // 🔼 هدر و تصویر بالا
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(screenHeight * 0.25f)
//                .background(Color(0xFF00BCD4))
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.pic),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(),
//                contentScale = ContentScale.Crop // 🔹 کل فضا رو پر میکنه و برش میده
//
//            )
//
//            IconButton(
//                onClick = { navController.popBackStack() },
//                modifier = Modifier
//                    .padding(
//                        start = screenWidth * 0.03f,
//                        top = screenHeight * 0.05f
//                    )
//                    .align(Alignment.TopStart)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.backbtn),
//                    contentDescription = "Back",
//                    tint = Color.Black,
//                    modifier = Modifier.size(screenWidth * 0.09f)
//                )
//            }
//        }
//
//        Text(
//            text = cardTitle,
//            fontSize = (screenWidth * 0.04f).value.sp,
//            fontWeight = FontWeight.Bold,
//            fontFamily = iranSans,
//            color = Color.Black,
//            textAlign = TextAlign.Right,
//            style = TextStyle(
//                textDirection = TextDirection.Rtl
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentWidth(Alignment.End)
//                .padding(horizontal = 16.dp)
//        )
//
//
//        Spacer(modifier = Modifier.height(screenHeight * 0.01f))
//
//        // 🟢 وضعیت و درصد
//        Box(
//            modifier = Modifier
//                .padding(horizontal = screenWidth * 0.03f)
//                .fillMaxWidth()
//                .wrapContentHeight() // 🔹 ارتفاع بر اساس محتوا
//        ) {
//            Text(
//                text = "وضعیت",
//                fontSize = (screenWidth * 0.035f).value.sp,
//                fontWeight = FontWeight.Medium,
//                fontFamily = iranSans,
//                color = Color.Black,
//                textAlign = TextAlign.Right,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentWidth(Alignment.End)
//                    .padding(horizontal = screenWidth * 0.04f)
//            )
//            Text(
//                text = "$correctPercentage%",
//                fontSize = (screenWidth * 0.035f).value.sp,
//                fontWeight = FontWeight.Medium,
//                fontFamily = iranSans,
//                color = Color.Black,
//                textAlign = TextAlign.Right,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentWidth(Alignment.Start)
//                    .padding(horizontal = screenWidth * 0.04f)
//            )
//        }
//
//
//
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ) {
//            Divider(
//                color = Color(0xFFABB7C2),
//                thickness = 1.dp,
//                modifier = Modifier
//                    .width(screenWidth * 0.88f)
//                    .padding(vertical = screenHeight * 0.005f)
//            )
//        }
//
//        ChartPager(
//            correct = correctCount,
//            wrong = wrongCount,
//            idk = idkCount,
//            new = newCount,
//            total = total,
//            selected = selectedStatuses,
//            onStatusToggle = { status ->
//                selectedStatuses = if (selectedStatuses.contains(status)) {
//                    selectedStatuses - status
//                } else {
//                    selectedStatuses + status
//                }
//            },
//            weeklyData = listOf(
//                "جمعه" to 25,
//                "پنج‌شنبه" to 10,
//                "چهارشنبه" to 18,
//                "سه‌شنبه" to 32,
//                "دوشنبه" to 22,
//                "یکشنبه" to 12,
//                "شنبه" to 5
//            )
//        )
//
//        Spacer(modifier = Modifier.height(screenHeight * 0.025f))
//
//        // 🃏 تعداد کارت‌ها
//        Box(
//            modifier = Modifier
//                .padding(horizontal = screenHeight * 0.025f)
//                .fillMaxWidth()
//                .height(screenHeight * 0.02f)
//                .clickable {
//                    navController.currentBackStackEntry
//                        ?.savedStateHandle
//                        ?.set("all_words", allWords)
//
//                    navController.navigate("word_list_page")
//                }
//        ) {
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        painter = painterResource(R.drawable.backbtn),
//                        contentDescription = "Back",
//                        tint = Color.Black,
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(2.dp))
//                    Text(
//                        text = "${filteredWords.size} / $total",
//                        fontSize = (screenWidth * 0.035f).value.sp,
//                        fontWeight = FontWeight.Medium,
//                        fontFamily = iranSans,
//                        color = Color.Black
//                    )
//                }
//
//                Text(
//                    text = "کارت‌ها",
//                    fontSize = (screenWidth * 0.035f).value.sp,
//                    fontWeight = FontWeight.Medium,
//                    fontFamily = iranSans,
//                    color = Color.Black,
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(screenHeight * 0.015f))
//
//        WordCards(filteredWords)
//
////        Spacer(modifier = Modifier.height(screenHeight * 0.035f))
//        Spacer(modifier = Modifier.weight(1f)) // فضای خالی تا پایین
//
//        // 🔘 دکمه مرور
//        // 🔘 دکمه مرور
//        // 🔘 دکمه مرور
//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .size(screenWidth * 0.4f, screenHeight * 0.11f)
//                .padding(bottom = 10.dp)
//                .imePadding()
//                .navigationBarsPadding()
//        ) {
//            val buttonText = "مرور (${filteredWords.size} کلمه)"
//
//            Button(
//                onClick = {
//                    val reviewWords = if (selectedStatuses.isEmpty()) {
//                        allWords
//                    } else {
//                        allWords.filter { it.status in selectedStatuses }
//                    }
//
//                    navController.currentBackStackEntry
//                        ?.savedStateHandle
//                        ?.set("review_words", reviewWords.toList())
//                    navController.currentBackStackEntry?.savedStateHandle?.set("review_words", reviewWords)
//                    navController.currentBackStackEntry?.savedStateHandle?.set("review_card_id", cardId)
//                    navController.navigate("review_page")
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CECE)),
//                shape = RoundedCornerShape(12.dp),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.review),
//                    contentDescription = null
//                )
//                Spacer(Modifier.width(8.dp))
//                Text(
//                    text = buttonText,
//                    fontFamily = iranSans,
//                    fontSize = 12.sp, // 🔹 سایز ثابت
//                    maxLines = 2,     // 🔹 حداکثر دو خط
//                    overflow = TextOverflow.Ellipsis,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//
//
//
//    }
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding(),
                contentAlignment = Alignment.Center // 🔹 وسط‌چین
            ) {
                Button(
                    onClick = {
                        val reviewWords = if (selectedStatuses.isEmpty()) {
                            allWords
                        } else {
                            allWords.filter { it.status in selectedStatuses }
                        }

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("review_words", reviewWords.toList())
                        navController.currentBackStackEntry?.savedStateHandle?.set("review_words", reviewWords)
                        navController.currentBackStackEntry?.savedStateHandle?.set("review_card_id", cardId)
                        navController.navigate("review_page")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CECE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(screenWidth * 0.45f)   // 🔹 دکمه با عرض ثابت
                        .height(screenHeight * 0.07f)   // 🔹 ارتفاع ثابت
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.review),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "مرور (${filteredWords.size} کلمه)",
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )  { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = innerPadding.calculateBottomPadding()) // فقط فاصله پایین رو اعمال کن

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.25f)
                    .background(Color(0xFF00BCD4))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pic),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop // 🔹 کل فضا رو پر میکنه و برش میده

                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(
                            start = screenWidth * 0.03f,
                            top = screenHeight * 0.05f
                        )
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
                text = cardTitle,
                fontSize = (screenWidth * 0.04f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = TextStyle(
                    textDirection = TextDirection.Rtl
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(screenHeight * 0.01f))

            // 🟢 وضعیت و درصد
            Box(
                modifier = Modifier
                    .padding(horizontal = screenWidth * 0.03f)
                    .fillMaxWidth()
                    .wrapContentHeight() // 🔹 ارتفاع بر اساس محتوا
            ) {
                Text(
                    text = "وضعیت",
                    fontSize = (screenWidth * 0.035f).value.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = iranSans,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                        .padding(horizontal = screenWidth * 0.04f)
                )
                Text(
                    text = "$correctPercentage%",
                    fontSize = (screenWidth * 0.035f).value.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = iranSans,
                    color = Color.Black,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                        .padding(horizontal = screenWidth * 0.04f)
                )
            }



            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Divider(
                    color = Color(0xFFABB7C2),
                    thickness = 1.dp,
                    modifier = Modifier
                        .width(screenWidth * 0.88f)
                        .padding(vertical = screenHeight * 0.005f)
                )
            }

            ChartPager(
                correct = correctCount,
                wrong = wrongCount,
                idk = idkCount,
                new = newCount,
                total = total,
                selected = selectedStatuses,
                onStatusToggle = { status ->
                    selectedStatuses = if (selectedStatuses.contains(status)) {
                        selectedStatuses - status
                    } else {
                        selectedStatuses + status
                    }
                },
                weeklyData = listOf(
                    "جمعه" to 25,
                    "پنج‌شنبه" to 10,
                    "چهارشنبه" to 18,
                    "سه‌شنبه" to 32,
                    "دوشنبه" to 22,
                    "یکشنبه" to 12,
                    "شنبه" to 5
                )
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.025f))

            // 🃏 تعداد کارت‌ها
            Box(
                modifier = Modifier
                    .padding(horizontal = screenHeight * 0.025f)
                    .fillMaxWidth()
                    .height(screenHeight * 0.02f)
                    .clickable {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("all_words", allWords)

                        navController.navigate("word_list_page")
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.backbtn),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${filteredWords.size} / $total",
                            fontSize = (screenWidth * 0.035f).value.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = iranSans,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = "کارت‌ها",
                        fontSize = (screenWidth * 0.035f).value.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = iranSans,
                        color = Color.Black,
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))

            // بقیه محتوای صفحه مثل قبل (هدر، چارت، کارت‌ها و ...)
            WordCards(filteredWords)
        }
    }


//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 90.dp) // جا برای دکمه پایین
//        ) {
//            // 🔼 اینجا تمام محتوای قبلی صفحه (هدر، چارت، کارت‌ها و ...)
//            // 🔼 هدر و تصویر بالا
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(screenHeight * 0.25f)
//                    .background(Color(0xFF00BCD4))
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.pic),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    contentScale = ContentScale.Crop // 🔹 کل فضا رو پر میکنه و برش میده
//
//                )
//
//                IconButton(
//                    onClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .padding(
//                            start = screenWidth * 0.03f,
//                            top = screenHeight * 0.05f
//                        )
//                        .align(Alignment.TopStart)
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.backbtn),
//                        contentDescription = "Back",
//                        tint = Color.Black,
//                        modifier = Modifier.size(screenWidth * 0.09f)
//                    )
//                }
//            }
//
//            Text(
//                text = cardTitle,
//                fontSize = (screenWidth * 0.04f).value.sp,
//                fontWeight = FontWeight.Bold,
//                fontFamily = iranSans,
//                color = Color.Black,
//                textAlign = TextAlign.Right,
//                style = TextStyle(
//                    textDirection = TextDirection.Rtl
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentWidth(Alignment.End)
//                    .padding(horizontal = 16.dp)
//            )
//
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.01f))
//
//            // 🟢 وضعیت و درصد
//            Box(
//                modifier = Modifier
//                    .padding(horizontal = screenWidth * 0.03f)
//                    .fillMaxWidth()
//                    .wrapContentHeight() // 🔹 ارتفاع بر اساس محتوا
//            ) {
//                Text(
//                    text = "وضعیت",
//                    fontSize = (screenWidth * 0.035f).value.sp,
//                    fontWeight = FontWeight.Medium,
//                    fontFamily = iranSans,
//                    color = Color.Black,
//                    textAlign = TextAlign.Right,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentWidth(Alignment.End)
//                        .padding(horizontal = screenWidth * 0.04f)
//                )
//                Text(
//                    text = "$correctPercentage%",
//                    fontSize = (screenWidth * 0.035f).value.sp,
//                    fontWeight = FontWeight.Medium,
//                    fontFamily = iranSans,
//                    color = Color.Black,
//                    textAlign = TextAlign.Right,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentWidth(Alignment.Start)
//                        .padding(horizontal = screenWidth * 0.04f)
//                )
//            }
//
//
//
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                Divider(
//                    color = Color(0xFFABB7C2),
//                    thickness = 1.dp,
//                    modifier = Modifier
//                        .width(screenWidth * 0.88f)
//                        .padding(vertical = screenHeight * 0.005f)
//                )
//            }
//
//            ChartPager(
//                correct = correctCount,
//                wrong = wrongCount,
//                idk = idkCount,
//                new = newCount,
//                total = total,
//                selected = selectedStatuses,
//                onStatusToggle = { status ->
//                    selectedStatuses = if (selectedStatuses.contains(status)) {
//                        selectedStatuses - status
//                    } else {
//                        selectedStatuses + status
//                    }
//                },
//                weeklyData = listOf(
//                    "جمعه" to 25,
//                    "پنج‌شنبه" to 10,
//                    "چهارشنبه" to 18,
//                    "سه‌شنبه" to 32,
//                    "دوشنبه" to 22,
//                    "یکشنبه" to 12,
//                    "شنبه" to 5
//                )
//            )
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.025f))
//
//            // 🃏 تعداد کارت‌ها
//            Box(
//                modifier = Modifier
//                    .padding(horizontal = screenHeight * 0.025f)
//                    .fillMaxWidth()
//                    .height(screenHeight * 0.02f)
//                    .clickable {
//                        navController.currentBackStackEntry
//                            ?.savedStateHandle
//                            ?.set("all_words", allWords)
//
//                        navController.navigate("word_list_page")
//                    }
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            painter = painterResource(R.drawable.backbtn),
//                            contentDescription = "Back",
//                            tint = Color.Black,
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(2.dp))
//                        Text(
//                            text = "${filteredWords.size} / $total",
//                            fontSize = (screenWidth * 0.035f).value.sp,
//                            fontWeight = FontWeight.Medium,
//                            fontFamily = iranSans,
//                            color = Color.Black
//                        )
//                    }
//
//                    Text(
//                        text = "کارت‌ها",
//                        fontSize = (screenWidth * 0.035f).value.sp,
//                        fontWeight = FontWeight.Medium,
//                        fontFamily = iranSans,
//                        color = Color.Black,
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.015f))
//
//            // فقط دکمه مرور رو اینجا نذار
//            WordCards(filteredWords)
//        }
//
//        // 🔘 دکمه مرور - همیشه پایین
//        Button(
//            onClick = {
//                val reviewWords = if (selectedStatuses.isEmpty()) {
//                    allWords
//                } else {
//                    allWords.filter { it.status in selectedStatuses }
//                }
//
//                navController.currentBackStackEntry
//                    ?.savedStateHandle
//                    ?.set("review_words", reviewWords.toList())
//                navController.currentBackStackEntry?.savedStateHandle?.set("review_words", reviewWords)
//                navController.currentBackStackEntry?.savedStateHandle?.set("review_card_id", cardId)
//                navController.navigate("review_page")
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CECE)),
//            shape = RoundedCornerShape(12.dp),
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 16.dp)
//                .size(screenWidth * 0.4f, screenHeight * 0.1f)
//                .imePadding()
//                .navigationBarsPadding()
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.review),
//                contentDescription = null
//            )
//            Spacer(Modifier.width(8.dp))
//            Text(
//                text = "مرور (${filteredWords.size} کلمه)",
//                fontFamily = iranSans,
//                fontSize = 12.sp,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                textAlign = TextAlign.Center
//            )
//        }
//    }

}


fun fetchWordsForCard(cardId: String, onResult: (List<Word>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("flashcards")
        .document(cardId)
        .collection("words")
        .get()
        .addOnSuccessListener { result ->
            val words = result.mapNotNull { doc ->
                try {
                    Word(
                        id = doc.id, // ← اینه documentId مثل "Flughafen"
                        text = doc.getString("text") ?: "",
                        translation = doc.getString("translation") ?: "",
                        status = when ((doc.getString("status") ?: "").uppercase()) {
                            "CORRECT" -> WordStatus.CORRECT
                            "WRONG" -> WordStatus.WRONG
                            "IDK" -> WordStatus.IDK
                            else -> WordStatus.NEW
                        }
                    )
                } catch (e: Exception) {
                    null
                }
            }
            onResult(words)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}

