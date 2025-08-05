package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun Jozve() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // دکمه برگشت بالا
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = screenWidth * 0.03f,
                    top = screenHeight * 0.05f
                ),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        // هدر و آیکون
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = screenHeight * 0.15f)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "الفبا و تلفظ حروف",
                fontSize = (screenWidth * 0.045f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.document),
                contentDescription = "Grammer",
                tint = Color(0xFF4D869C),
                modifier = Modifier.size(screenWidth * 0.08f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.01f))

            // ** این قسمت جدید است: فیلد نمایش فایل با استایل عکس **
            Box(
                modifier = Modifier
                    .width(screenWidth * 0.8f)
                    .height(screenHeight * 0.07f) // ارتفاع را کمی بیشتر کردیم تا محتوا جا شود
                    .shadow(
                        elevation = 6.dp, // شدت سایه
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White) // پس‌زمینه سفید
                    .padding(horizontal = 16.dp, vertical = 8.dp) // پدینگ داخلی برای فاصله از لبه
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "alfabet_german.pdf",
                            fontFamily = iranSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "۵mg",
                            fontFamily = iranSans,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            // ** پایان قسمت جدید **

            Spacer(modifier = Modifier.height(screenHeight * 0.08f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Handle download */ },
                    modifier = Modifier
                        .width(129.dp)
                        .height(53.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "پرینت",
                        fontFamily = iranSans,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { /* Handle download */ },
                    modifier = Modifier
                        .width(129.dp)
                        .height(53.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "دانلود",
                        fontFamily = iranSans,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JozvePreview() {
    Jozve()
}