//package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.moarefiprod.iranSans
//
//@Composable
//fun MusicScreen(navController: NavController) {
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    val screenHeight = configuration.screenHeightDp.dp
//
//    var showFavorites by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(horizontal = 16.dp)
//    ) {
//        // Header with Back Button and Search Bar
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = screenHeight * 0.05f),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    painter = painterResource(id = android.R.drawable.ic_menu_revert),
//                    contentDescription = "Back",
//                    tint = Color.Black,
//                    modifier = Modifier.size(screenWidth * 0.09f)
//                )
//            }
//            OutlinedTextField(
//                value = "",
//                onValueChange = {},
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(start = 8.dp),
//                placeholder = { Text("جستجو", fontFamily = iranSans) },
//                leadingIcon = { Icon(painterResource(id = android.R.drawable.ic_menu_search), null) },
//                trailingIcon = { Text("جستجو", fontFamily = iranSans, color = Color.Gray) },
//                shape = RoundedCornerShape(24.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = Color.LightGray,
//                    unfocusedBorderColor = Color.LightGray
//                )
//            )
//        }
//
//        // Section Titles
//        Text(
//            text = "Am beliebtesten",
//            fontSize = 18.sp,
//            fontFamily = iranSans,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .padding(top = 16.dp, bottom = 8.dp)
//                .align(Alignment.End)
//        )
//
//        // Popular Artists
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Image(
//                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                contentDescription = "Artist 1",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clickable { }
//            )
//            Image(
//                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                contentDescription = "Artist 2",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clickable { }
//            )
//            Button(
//                onClick = { },
//                modifier = Modifier
//                    .size(100.dp)
//                    .align(Alignment.CenterVertically),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//            ) {
//                Text("see more", fontFamily = iranSans)
//            }
//        }
//
//        // Favorites Toggle
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            horizontalArrangement = Arrangement.End
//        ) {
//            Text(
//                text = "Favoriten",
//                fontSize = 18.sp,
//                fontFamily = iranSans,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = "Alle",
//                fontSize = 16.sp,
//                fontFamily = iranSans,
//                color = Color.Gray,
//                modifier = Modifier.clickable { showFavorites = !showFavorites }
//            )
//        }
//
//        // Song List
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(listOf("Lena Meyer-Landrut", "Lena Meyer-Landrut", "Lena Meyer-Landrut", "Lena Meyer-Landrut", "Lena Me")) { song ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { }
//                        .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
//                        .padding(8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                        contentDescription = song,
//                        modifier = Modifier.size(50.dp)
//                    )
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(
//                            text = song,
//                            fontSize = 16.sp,
//                            fontFamily = iranSans,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.btn_star),
//                        contentDescription = "Favorite",
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.ic_menu_download),
//                        contentDescription = "Download",
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//            }
//            item {
//                AnimatedVisibility(visible = showFavorites) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { }
//                            .background(Color(0xFF7AB2B2).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
//                            .padding(8.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                            contentDescription = "Lena Me",
//                            modifier = Modifier.size(50.dp)
//                        )
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = "Lena Me",
//                                fontSize = 16.sp,
//                                fontFamily = iranSans,
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
//                        Icon(
//                            painter = painterResource(id = android.R.drawable.ic_media_play),
//                            contentDescription = "Play",
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}