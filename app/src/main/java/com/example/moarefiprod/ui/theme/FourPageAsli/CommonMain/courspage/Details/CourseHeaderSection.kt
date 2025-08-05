package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Details

// In file: FourPageAsli.CommonMain.courspage/BannerSection.kt
// (You might want to rename BannerSection.kt to CourseHeaderSection.kt or create a new file if BannerSection.kt is used elsewhere)

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

@Composable
fun CourseHeaderSection(navController: NavController, courseSath: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val imageSectionHeight = screenHeight * 0.3f

    Box(modifier = Modifier.fillMaxWidth().height(imageSectionHeight)) {
        Image(
            painter = painterResource(id = R.drawable.course_pic),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageSectionHeight)
                .align(Alignment.TopStart)
        )

        IconButton(
            onClick = {
                navController.navigate("home")
            },
            modifier = Modifier
                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back",
                tint = Color.Unspecified,
                modifier = Modifier.size(screenWidth * 0.09f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = screenWidth * 0.048f)
                .offset(y = (-screenHeight * 0.04f))
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "آموزش زبان آلمانی",
                fontSize = (screenWidth * 0.048f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color.White,
                textAlign = TextAlign.End,
                style = androidx.compose.ui.text.TextStyle(
                    textDirection = androidx.compose.ui.text.style.TextDirection.Rtl
                )
            )

            Text(
                text = "سطح $courseSath",
                fontSize = (screenWidth * 0.048f).value.sp, // کمی کوچکتر اختیاریه
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color.White,
                textAlign = TextAlign.End,
                style = androidx.compose.ui.text.TextStyle(
                    textDirection = androidx.compose.ui.text.style.TextDirection.Rtl
                )
            )
        }
    }
}