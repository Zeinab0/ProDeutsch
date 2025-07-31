package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import android.content.Context
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import kotlinx.coroutines.*


@Composable
fun MovieDetailScreen(
    title: String ,
    description: String  ,
    level: String,
    price: String ,
    videoUrl: String,
    onBack: () -> Unit = {}
) {
    val isFree = price == "رایگان"
    var purchased by remember { mutableStateOf(isFree) } // اگر رایگانه، نیازی به خرید نیست

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.03f)
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(top = screenHeight * 0.05f)
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ✅ پیش‌نمایش (فقط متنش محو میشه، فضا حفظ میشه)
                val context = LocalContext.current
                Box(modifier = Modifier.height(20.dp)) {
                    if (!purchased && !isFree) {
                        Text(
                            text = "پیش نمایش",
                            textAlign = TextAlign.End,
                            fontFamily = iranSans,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

// 🎥 پخش ویدیو (فقط این بخش ری‌کامپوز میشه)
                key(purchased) {
                    VideoPlayer(
                        url = videoUrl,
                        context = context,
                        purchased = purchased
                    )
                }




                // 📘 عنوان
                Text(
                    text = title,
                    textAlign = TextAlign.Right,
                    fontFamily = iranSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier.fillMaxWidth()
                )

                // 📝 توضیحات
                Text(
                    text = description,
                    textAlign = TextAlign.Right,
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier.fillMaxWidth()
                )

                // 🎯 سطح
                Text(
                    text = "سطح : $level",
                    fontFamily = iranSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = TextStyle(textDirection = TextDirection.Rtl)
                )

                // 💰 قیمت فقط قبل از خرید
                if (!purchased && !isFree) {
                    Text(
                        text = "قیمت : $price",
                        fontFamily = iranSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF75ABAB),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )
                }

                Spacer(modifier = Modifier.height(55.dp))

                // 🛒 دکمه خرید (فقط قبل از خرید)
                if (!purchased && !isFree) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF7AB2B2))
                            .clickable { purchased = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "خرید",
                            color = Color.White,
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailWrapper(id: String, onBack: () -> Unit) {
    var movie by remember { mutableStateOf<Movie?>(null) }

    LaunchedEffect(id) {
        getMoviesFromFirestore { allMovies ->
            movie = allMovies.find { it.id == id }
        }
    }

    if (movie == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        MovieDetailScreen(
            title = movie!!.title,
            description = movie!!.description,
            level = movie!!.level,
            price = movie!!.price,
            videoUrl = movie!!.videoUrl,
            onBack = onBack
        )
    }
}




@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    url: String,
    context: Context,
    purchased: Boolean
) {
    val playerHolder = remember { mutableStateOf<ExoPlayer?>(null) }

    DisposableEffect(url, purchased) {
        val exoPlayer = ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))

            val finalSource = if (!purchased) {
                ClippingMediaSource(mediaSource, 0, 5_000_000) // 5 ثانیه
            } else {
                mediaSource
            }

            setMediaSource(finalSource)
            prepare()
            playWhenReady = true
        }

        playerHolder.value = exoPlayer

        onDispose {
            exoPlayer.release()
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
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}
