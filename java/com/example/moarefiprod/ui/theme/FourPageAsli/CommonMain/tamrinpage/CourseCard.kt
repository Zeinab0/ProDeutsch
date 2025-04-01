package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun CourseCard(course: Course) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardHeight = screenWidth * 0.3f // ارتفاع متناسب با عرض صفحه
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = course.image),
                contentDescription = "Course Image",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(cardHeight)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(5.dp))
            )

            Column(
                modifier = Modifier
                    .width(60.dp)
                    .align(Alignment.Bottom)
                    .padding(bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = if (course.price == 0) "رایگان" else "هزار تومان ${course.price}",
                    fontSize = 10.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = if (course.price == 0) Color(0xFF2E7D32) else Color(0xFF000000), // سبز برای رایگان
                    textAlign = TextAlign.Right
                )


                Button(
                    onClick = { /* TODO */ },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C))
                ) {

                    Text(
                        text = "شروع دوره",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        color = Color.White,
                        fontSize = 8.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(0.dp,10.dp,10.dp,10.dp)
                    .fillMaxHeight()
                    .width(200.dp),
//                    .background(Color(0xFF9B3131)),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = course.title,
                    fontSize = 12.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Right
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = course.description,
                    fontSize = 10.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Right
                )
                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontFamily = iranSans)) {
                            append("سطح دوره: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontFamily = iranSans)) {
                            append(course.sath)
                        }
                    },
                    fontSize = 6.sp,
                    fontFamily = iranSans,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Right
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontFamily = iranSans)) {
                            append("مدت زمان دوره: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontFamily = iranSans)) {
                            append(course.teadad)
                        }
                    },
                    fontSize = 6.sp,
                    fontFamily = iranSans,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Right
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontFamily = iranSans)) {
                            append("تعداد دروس: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontFamily = iranSans)) {
                            append(course.teadad)
                        }
                    },
                    fontSize = 6.sp,
                    fontFamily = iranSans,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Right
                )


            }
        }
    }
}

@Composable
fun NewLabel() {
    Box(
        modifier = Modifier
            .offset(x = (-10).dp, y = 6.dp) // محل قرارگیری بالا چپ
            .rotate(-40f) // چرخش مورب
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

