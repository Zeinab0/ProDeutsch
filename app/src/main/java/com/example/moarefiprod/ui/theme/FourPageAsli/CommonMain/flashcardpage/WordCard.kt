package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Course

@Composable
fun WordCard(course: Course) {
    val configuration = LocalConfiguration.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val cardWidth = screenWidth * 0.27f
    val cardHeight = screenHeight * 0.18f
    val imageHeight = cardHeight * 0.32f
    val buttonHeight = cardHeight * 0.2f
    val fontSizeTitle = screenWidth * 0.03f
    val fontSizeButton = screenWidth * 0.025f

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(0.dp) // ❌ بدون گوشه گرد
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // عنوان
            Text(
                text = course.title,
                fontSize = fontSizeTitle.value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                textAlign = TextAlign.Center
            )

            // تصویر
            Image(
                painter = painterResource(id = course.image),
                contentDescription = null,
                modifier = Modifier
                    .width(cardWidth * 0.85f)
                    .height(imageHeight)
                    .background(Color(0xFF983434))
                    .clip(RectangleShape), // برش دقیق طبق قاب
                contentScale = ContentScale.Crop // ❗ برش بزنه تا فیت بشه داخل اندازه
            )

            // دکمه ادامه یادگیری
            Button(
                onClick = { /* TODO */ },
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(0.dp), // ❌ بدون گوشه گرد
                modifier = Modifier
                    .width(cardWidth * 0.65f)
                    .height(buttonHeight)
                    .padding(top = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCDE8E5))
            ) {
                Text(
                    text = "ادامه یادگیری",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    color = Color(0xFF4D869C),
                    fontSize = fontSizeButton.value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}