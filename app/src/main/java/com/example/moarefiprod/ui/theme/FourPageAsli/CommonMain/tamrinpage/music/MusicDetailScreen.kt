package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R // مسیر پکیج شما برای دسترسی به R

//MusicPlayerLyricsScreen
@Composable
fun MusicDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // رنگ پس‌زمینه خاکستری مشابه تصویر
            .padding(24.dp)
    ) {
        // بخش هدر (Back Button)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            IconButton(
                onClick = { /* TODO: Handle back navigation */ },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // بخش اطلاعات آهنگ (کاور و نام خواننده)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFB0B0B0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.music),
                    contentDescription = "Album Cover",
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Lena Meyer-Landrut",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Lena Me",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.dola_chang),
                contentDescription = "Lyrics Icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // بخش متن ترانه (Lyrics Box)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE3F4F4)) // رنگ پس زمینه آبی روشن
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Text(
                        text = "In dieser Geschichte geht ein Mann über eine alte Brücke, die ihn in eine verlassene Stadt führt. " +
                                "Als er versucht, zur Brücke zurückzukehren, verschwindet sie, " +
                                "und er findet sich in einer weltlosen, zeitlosen Realität wieder.\n\n" +
                                "In dieser Geschichte geht ein Mann über eine alte Brücke, die ihn in eine verlassene Stadt führt. " +
                                "Als er versucht, zur Brücke zurückzukehren, verschwindet sie, " +
                                "und er findet sich in einer weltlosen, zeitlosen Realität wieder.\n\n" +
                                "In dieser Geschichte geht ein Mann über eine alte Brücke, die ihn in eine verlassene Stadt führt. " +
                                "Als er versucht, zur Brücke zurückzukehren, verschwindet sie, " +
                                "und er findet sich in einer weltlosen, zeitlosen Realität wieder.",
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // بخش نوار پیشرفت و کنترل‌های پخش
        // نوار پیشرفت
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight()
                        .background(Color(0xFF6AB7B1))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "2:16",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "-4:32",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // دکمه‌های کنترل پخش
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            IconButton(onClick = { /* TODO: Handle download */ }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.download),
//                    contentDescription = "Download",
//                    tint = Color.Gray,
//                    modifier = Modifier.size(28.dp)
//                )
//            }
            IconButton(onClick = { /* TODO: Handle previous track */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_previous),
                    contentDescription = "Previous",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(
                onClick = { /* TODO: Handle play/pause */ },
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7AB2B2))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = "Play/Pause",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = { /* TODO: Handle next track */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_next),
                    contentDescription = "Next",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
//            IconButton(onClick = { /* TODO: Handle shuffle */ }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.favorites),
//                    contentDescription = "Favorite",
//                    tint = Color.Red,
//                    modifier = Modifier.size(28.dp)
//                )
//            }
        }
    }
}

// کد Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MusicDetailScreenPreview() {
MusicDetailScreen()
 }
