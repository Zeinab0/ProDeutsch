package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Review

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus
import kotlinx.coroutines.delay

@Composable
fun ReviewPage(
    words: List<Word>,
    navController: NavController,
    onReviewFinished: (List<Word>) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var showExitDialog by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var tempWordList by remember { mutableStateOf(words.toMutableList()) }
    val updatedStatuses = remember { mutableStateMapOf<Word, WordStatus>() }

    val currentWord = tempWordList.getOrNull(currentIndex)

    // ⏱ تایمر
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds++
        }
    }

    // ✅ پایان مرور
    LaunchedEffect(currentWord) {
        if (currentWord == null && words.isNotEmpty()) {
            val finalWords = words.map { word ->
                updatedStatuses[word]?.let { word.copy(status = it) } ?: word
            }

            onReviewFinished(finalWords)
            navController.popBackStack()
        }
    }

    if (currentWord == null) return

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTime(seconds),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = iranSans
                )
                IconButton(onClick = { showExitDialog = true }) {
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "close",
                        modifier = Modifier.size(screenWidth * 0.08f)
                    )
                }
            }

            // Flashcard
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(width = 300.dp, height = 400.dp)
                            .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isFlipped) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(currentWord.german, fontSize = 20.sp, fontFamily = iranSans)
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color.Black, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(currentWord.persian, fontSize = 20.sp, fontFamily = iranSans)
                            }
                        } else {
                            Text(currentWord.german, fontSize = 20.sp, fontFamily = iranSans)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text("${currentIndex + 1}/${words.size}", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(32.dp))

                    if (isFlipped) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ActionButton("نمیدانم", Color(0xFFF7A400)) {
                                updatedStatuses[currentWord] = WordStatus.IDK
                                isFlipped = false
                                currentIndex++
                            }
                            ActionButton("غلط", Color(0xFFE53935)) {
                                updatedStatuses[currentWord] = WordStatus.WRONG
                                isFlipped = false
                                currentIndex++
                            }
                            ActionButton("درست", Color(0xFF43A047)) {
                                updatedStatuses[currentWord] = WordStatus.CORRECT
                                isFlipped = false
                                currentIndex++
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .width(330.dp)
                                .height(53.dp)
                                .border(1.dp, Color(0xFF4D869C), RoundedCornerShape(12.dp))
                                .background(Color(0xFF8ACCCC), RoundedCornerShape(12.dp))
                                .clickable { isFlipped = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("برگرداندن", fontSize = 14.sp, fontFamily = iranSans)
                        }
                    }
                }
            }
        }

        // 🟢 دیالوگ خروج
        if (showExitDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                ExitReviewDialog(
                    onDismiss = { showExitDialog = false },
                    onConfirm = {
                        showExitDialog = false
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun ActionButton(text: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(53.dp)
            .background(color, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontFamily = iranSans)
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
