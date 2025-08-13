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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel.FlashcardViewModel
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🔼 هدر و تصویر بالا
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp)
                .background(Color(0xFF64DEF0))
        ) {
            Image(
                painter = painterResource(id = R.drawable.pic),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 16.dp)
        )


        Spacer(modifier = Modifier.height(12.dp))

        // 🟢 وضعیت و درصد
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(20.dp)
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
                    .padding(horizontal = 16.dp)
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
                    .padding(horizontal = 16.dp)
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
                    .width(350.dp)
                    .padding(vertical = 10.dp)
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

        Spacer(modifier = Modifier.height(22.dp))

        // 🃏 تعداد کارت‌ها
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(20.dp)
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

        Spacer(modifier = Modifier.height(5.dp))

        WordCards(filteredWords)

        Spacer(modifier = Modifier.height(32.dp))

        // 🔘 دکمه مرور
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
                .align(Alignment.CenterHorizontally)
                .size(180.dp, 90.dp)
                .padding(10.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.review), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("مرور (${filteredWords.size} کلمه)", fontFamily = iranSans)
        }
    }
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

