package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moarefiprod.R



@Composable
fun BannerSection(
    navController: NavController,
    viewModel: BannerViewModel = viewModel()
) {
  //  val viewModel: BannerViewModel = viewModel() // ✅ اینجا بکش داخل تابع
    val banners = viewModel.banners.collectAsState().value


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        viewModel.loadBanners()
    }

    if (banners.isNotEmpty()) {
        val banner = banners.first() // در حال حاضر فقط یک بنر داریم

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.23f)
                .padding(vertical = screenHeight * 0.024f)
                .shadow(
                    elevation = 10.dp,
                    ambientColor = Color.Black,
                    spotColor = Color.Black,
                    clip = false
                )
                .background(Color.White)
                .clickable {
                    try {
                        if (banner.targetUrl.isNotEmpty()) {
                            navController.navigate(banner.targetUrl)
                        }
                    } catch (e: Exception) {
                        Log.e("BannerNav", "❌ Navigation failed: ${e.message}")
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = banner.imageUrl,
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    else {
        Log.e("Banner", "❌ No active banners found.")
    }
}
