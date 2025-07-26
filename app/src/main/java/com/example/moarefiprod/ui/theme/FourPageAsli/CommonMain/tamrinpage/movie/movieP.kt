package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar

@Composable
fun MovieScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {  },
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

        SearchBar()

        // Title: جدیدترین‌ها
        Text(
            text = "جدیدترین ها",
            fontSize = 12.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 27.dp , top = 20.dp)
                .align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(15.dp))


        LazyRow(
            contentPadding = PaddingValues(horizontal = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                MovieCard(isHorizontal = true)
            }
        }
    }
}

@Composable
fun MovieCard(isHorizontal: Boolean = false) {
    Card(
//        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = if (isHorizontal) Modifier
            .width(90.dp)
            .height(120.dp)
        else Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Play", tint = Color.DarkGray, modifier = Modifier.size(36.dp))
            }
            Text(
                text = "گم شدن نیکوس (قسمت اول)",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "سطح: A1 • ۱۵ دقیقه")
            Text(text = if (isHorizontal) "شروع" else "رایگان", color = Color.Blue)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MovieScreenPreview() {
    MovieScreen()
}
