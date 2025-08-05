package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.example.moarefiprod.R
import android.content.Context
import androidx.annotation.OptIn

@Composable
fun VideoScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282828)) // رنگ پس‌زمینه خاکستری تیره
    ) {
        // دکمه بازگشت
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = screenHeight * 0.05f,
                    start = 16.dp
                )
        ) {
            IconButton(
                onClick = { /* Handle back action */ },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.18f)) // فاصله از بالای صفحه

        // مستطیل موقت پخش‌کننده ویدیو
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFB6B6B6)), // مستطیل خاکستری کم‌رنگ
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.video), // آیکون موقت پخش
                contentDescription = "Video Placeholder",
                tint = Color.Gray,
                modifier = Modifier.size(60.dp) // اندازه آیکون
            )
        }
    }
}

// کامپوزبل پخش کننده ویدیو (حذف شده از نمایش)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    url: String,
    context: Context,
    purchased: Boolean,
    modifier: Modifier = Modifier
) {
    val playerHolder = remember { mutableStateOf<ExoPlayer?>(null) }

    DisposableEffect(url, purchased) {
        val exoPlayer = ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))

            val finalSource = if (!purchased) {
                ClippingMediaSource(mediaSource, 0, 5_000_000)
            } else {
                mediaSource
            }

            setMediaSource(finalSource)
            prepare()
            playWhenReady = true
        }

        playerHolder.value = exoPlayer

        onDispose {
            playerHolder.value?.release()
            playerHolder.value = null
        }
    }

    playerHolder.value?.let { player ->
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = true
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VideoScreenPreview() {
    VideoScreen()
}
// پخش‌کننده ویدیو
//        VideoPlayer(
//            url = videoUrl,
//            context = LocalContext.current,
//            purchased = purchased,
//            modifier = Modifier
//                .align(Alignment.Center) // ویدیو در وسط صفحه قرار می‌گیرد
//        )