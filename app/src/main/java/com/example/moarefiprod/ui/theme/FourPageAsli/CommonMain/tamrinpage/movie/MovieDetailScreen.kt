package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun MovieDetailScreen(
    title: String = "گم شدن نیکوس (قسمت اول)",
    description: String = "نیکوس پسری که به قصد دیدن خاله خود از ایتالیا به آلمان سفر میکند..." +
            " اما بلافاصله بعد رسیدن به آلمان در فرودگاه سگ دستی خود که شامل پاسپورت و آدرس خانه خاله اش می باشد را گم میکند و به دنبال کیف گمشده خود میگردد",
    level: String = "A1",
    price: String = "۴۰ هزار تومان",
    onBack: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {
        // 🔙 Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        // 🎬 Title
        Text(
            text = "پیش نمایش",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.End,
            fontFamily = iranSans,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )

        // 🖼 Video Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.25f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.baner), // 📷 جایگزین با تصویرت
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📘 Title
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontFamily = iranSans,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 📄 Description
        Text(
            text = description,
            textAlign = TextAlign.Center,
            fontFamily = iranSans,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 📌 سطح و قیمت
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "سطح : $level",
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "قیمت : $price",
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🛒 Buy Button
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF7AB2B2))
                .clickable { /* Handle buy */ }
                .padding(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Text(
                text = "خرید",
                color = Color.White,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
