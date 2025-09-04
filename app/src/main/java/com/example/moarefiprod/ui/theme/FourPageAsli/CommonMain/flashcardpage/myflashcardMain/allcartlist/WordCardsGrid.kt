package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus

@Composable
fun WordCardsGrid(words: List<Word>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        items(words.chunked(2)) { rowWords ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center // 🔹 ردیف‌ها همیشه وسط
            ) {
                rowWords.forEach { word ->
                    WordCardItem(
                        word = word,
                        modifier = Modifier
                            .weight(1f) // 🔹 هر کارت نصف عرض ردیف
                            .aspectRatio(0.95f) // 🔹 نسبت ارتفاع به عرض (مثلاً 3:4)
                            .padding(horizontal = 8.dp) // فاصله مساوی بین کارت‌ها
                    )
                }
            }
        }
    }
}



@Composable
fun WordCardItem(word: Word, modifier: Modifier = Modifier) {
    val (color, statusLabel) = when (word.status) {
        WordStatus.CORRECT -> Color(0xFF00BD10) to "درست"
        WordStatus.WRONG -> Color.Red to "غلط"
        WordStatus.IDK -> Color(0xFFFFA500) to "نمی‌دانم"
        WordStatus.NEW -> Color(0xFF00CCFF) to "جدید"
    }

    androidx.compose.material3.Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDEDED)),
            verticalArrangement = Arrangement.SpaceBetween, // بخش بالا و وسط و پایین
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ نوار بالا با متن وضعیت
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .background(color, shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                androidx.compose.material3.Text(
                    text = statusLabel,
                    color = Color.White,
                    fontFamily = iranSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // ✅ محتوای وسط کارت (کلمه + خط + ترجمه)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // وسط چین بشه
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Text(
                    text = word.text,
                    fontSize = 18.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f) // 🔹 نصف عرض کارت
                        .height(1.dp)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                androidx.compose.material3.Text(
                    text = word.translation,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = iranSans
                )
            }
        }
    }
}
