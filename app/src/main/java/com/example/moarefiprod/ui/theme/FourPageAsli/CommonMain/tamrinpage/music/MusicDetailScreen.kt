package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.media3.common.MediaItem
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moarefiprod.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@Composable
fun MusicDetailScreen(songId: String , navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val viewModel: MusicViewModel = viewModel()
    val songs by viewModel.songs.collectAsState()
    val song = songs.find { it.id == songId }

    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser == null || currentUser.isAnonymous) {
        // مثلاً: showSnackbar("لطفاً ابتدا وارد شوید")
        return
    }
    var waitFinished by remember { mutableStateOf(false) }

    LaunchedEffect(songId) {
        delay(10_000) // ۱۰ ثانیه
        waitFinished = true
    }

    if (song == null && !waitFinished) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (song == null) {
        Text("آهنگ یافت نشد")
        return
    }

    val context = LocalContext.current

    val exoPlayer = remember(songId) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(song.songUrl)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }


    val duration = remember { mutableStateOf(0L) }
    val position = remember { mutableStateOf(0L) }
    val isDurationReady = duration.value > 0 && duration.value != Long.MIN_VALUE

    LaunchedEffect(isPlaying) {
        while (true) {
            withContext(Dispatchers.Main) {
                position.value = exoPlayer.currentPosition
                duration.value = exoPlayer.duration
            }
            delay(500L)
        }

    }

    val progress = if (duration.value > 0) {
        position.value.toFloat() / duration.value
    } else 0f

    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid

    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(songId, userId) {
        if (!userId.isNullOrEmpty()) {
            val doc = Firebase.firestore
                .collection("users")
                .document(userId)
                .collection("likedSongs")
                .document(songId)
                .get()
                .await()

            isFavorite = doc.exists()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // رنگ پس‌زمینه خاکستری مشابه تصویر
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {navController.popBackStack() },
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


        Column(
            modifier = Modifier
//                .fillMaxWidth()
//                .height(620.dp)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            // بخش اطلاعات آهنگ (کاور و نام خواننده)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFB6B6B6)),
                    contentAlignment = Alignment.Center
                ) {
                    if (song.imageUrl.isNullOrEmpty()) {
                        // 📌 اگر عکس موجود نبود، آیکون پیش‌فرض
                        Icon(
                            painter = painterResource(id = R.drawable.music),
                            contentDescription = "Default Music Icon",
                            modifier = Modifier.size(50.dp),
                            tint = Color.White
                        )
                    } else {
                        // ✅ اگر عکس موجود بود، لود از URL
                        AsyncImage(
                            model = song.imageUrl,
                            contentDescription = "Album Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = song.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = song.artist,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.dola_chang),
                    contentDescription = "Lyrics Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                        .align(Alignment.Bottom)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // بخش متن ترانه (Lyrics Box)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE3F4F4)) // رنگ پس زمینه آبی روشن
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .height(530.dp)
                        .padding(bottom = 10.dp),
                    contentPadding = PaddingValues(bottom = 5.dp)
                ) {
                    item {
                        Text(
                            text = song.lyrics,
                            fontSize = 14.sp,
                            color = Color.Black,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(15.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .background(Color(0xFF6AB7B1))
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(position.value), // ⏱ زمان سپری‌شده
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = if (isDurationReady)
                            "-${formatTime(duration.value - position.value)}"
                        else
                            "--:--",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                }


            }

            Spacer(modifier = Modifier.height(4.dp))

            // دکمه‌های کنترل پخش
            Row(
                modifier = Modifier
//                    .fillMaxWidth(),
                    .width(300.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    downloadSong(context, song.title, song.songUrl)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "Download",
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(onClick = {
                    exoPlayer.seekTo(0L) // ⬅️ برگشت به زمان صفر
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_previous),
                        contentDescription = "Previous",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                        isPlaying = !isPlaying
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7AB2B2))
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isPlaying) R.drawable.ic_pause else R.drawable.play_ic
                        ),
                        contentDescription = "Play/Pause",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(60.dp)
                    )
                }

                IconButton(onClick = { /* TODO: Handle next track */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next),
                        contentDescription = "Next",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }

                val context = LocalContext.current
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val userId = firebaseUser?.uid

                IconButton(onClick = {
                    if (userId.isNullOrEmpty()) {
                        Toast.makeText(context, "لطفاً ابتدا وارد شوید", Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }

                    val newLikeState = !isFavorite
                    isFavorite = newLikeState  // بروزرسانی UI سریع

                    viewModel.toggleSongLike(
                        userId = userId,
                        song = song.copy(isFavorite = newLikeState),
                        liked = newLikeState
                    )
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }







            }
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}

fun downloadSong(context: Context, songTitle: String, url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(songTitle)
        .setDescription("در حال دانلود آهنگ...")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "$songTitle.mp3")

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}
