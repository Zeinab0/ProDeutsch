package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VideoScreen(
    courseId: String,
    lessonId: String,
    contentId: String,
    navController: NavController,
    viewModel: DarsViewModel = viewModel()

) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current

    val videoUrl = viewModel.videoUrl.collectAsState().value

    LaunchedEffect(contentId) {
        viewModel.loadVideoUrl(courseId, lessonId, contentId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // دکمه برگشت
        IconButton(
            onClick = { navController.popBackStack() },
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
                tint = Color.White,
                modifier = Modifier.size(screenWidth * 0.09f)
            )
        }

        if (videoUrl != null && videoUrl.isNotBlank()) {
            VideoPlayer(
                url = videoUrl,
                context = context,
                purchased = true, // شبیه جزوه و فیلم‌های رایگان
                modifier = Modifier.align(Alignment.Center)
//                modifier = Modifier
//                    .padding(top = screenHeight * 0.15f)
//                    .align(Alignment.TopCenter)
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
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
