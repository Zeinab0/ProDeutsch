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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.annotation.OptIn
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.compose.runtime.*



// imports لازم:
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MovieDetailScreen(
    movieId: String,                 // 👈 جدید
    title: String ,
    description: String  ,
    level: String,
    price: String ,
    videoUrl: String,
    imageUrl: String = "",          // اگر داری، پاس بده (برای نمایش بعدی در دوره‌های من)
    onBack: () -> Unit = {}
) {

    val ctx = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }
    val uid = remember { FirebaseAuth.getInstance().currentUser?.uid }

    val isFree = price.equals("رایگان", true) || price.equals("Frei", true) || price.equals("Free", true)
    var purchased by remember { mutableStateOf(false) }   // 👈 دیگر خودکار خریداری‌شده نیست

    DisposableEffect(uid, movieId) {
        if (uid.isNullOrBlank() || movieId.isBlank()) {
            // حتی در حالت no-op هم باید DisposableEffectResult برگردونیم
            onDispose { /* no-op */ }
        } else {
            val ref = db.collection("users")
                .document(uid)
                .collection("purchased_movies")
                .document(movieId)

            val reg = ref.addSnapshotListener { snap, e ->
                if (e == null) {
                    purchased = purchased || (snap?.exists() == true)
                }
                // اگر خواستی خطا رو لاگ کن؛ مهم نیست چیزی برگردونی
            }

            // این مقدارِ برگشتیِ لازم برای DisposableEffectResult است
            onDispose { reg.remove() }
        }
    }


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
        Box(modifier = Modifier.fillMaxWidth()) {
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
                // پیش‌نمایش
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

                // ویدیو (۵ ثانیه اگر نخریده)
                val context = LocalContext.current
                key(purchased) {
                    VideoPlayer(
                        url = videoUrl,
                        context = context,
                        purchased = purchased
                    )
                }

                // عنوان/توضیح/سطح/قیمت
                Text(
                    text = title,
                    textAlign = TextAlign.Right,
                    fontFamily = iranSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = description,
                    textAlign = TextAlign.Right,
                    fontFamily = iranSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "سطح : $level",
                    fontFamily = iranSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = TextStyle(textDirection = TextDirection.Rtl)
                )

                if (!purchased && !isFree) {
                    Text(
                        text = "قیمت : $price",
                        fontFamily = iranSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF75ABAB),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )
                }

                Spacer(modifier = Modifier.height(55.dp))

                // 🛒 دکمه خرید → ذخیره در users/{uid}/purchased_movies/{movieId}
                if (!purchased && !isFree) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF7AB2B2))
                            .clickable {
                                if (uid.isNullOrBlank()) {
                                    Toast.makeText(ctx, "ابتدا وارد حساب شوید", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }

                                // movieId را تمیز و چک کن
                                val mid = movieId.toString().trim()   // اگر nullable/Int بود هم حل می‌شود
                                if (mid.isEmpty() || '/' in mid) {
                                    Toast.makeText(ctx, "شناسه‌ی فیلم نامعتبر است", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }

                                val ref = db.collection("users")
                                    .document(uid)                      // ← فقط uid
                                    .collection("purchased_movies")     // ← کالکشن
                                    .document(mid)                      // ← فقط ID سند (بدون اسلش)

                                val doc = mapOf(
                                    "title" to title,
                                    "description" to description,
                                    "level" to level,
                                    "duration" to "",           // اگر داری مقدار بده
                                    "price" to price,
                                    "imageUrl" to imageUrl,
                                    "videoUrl" to videoUrl,
                                    "purchasedAt" to FieldValue.serverTimestamp()
                                )

                                ref.set(doc)
                                    .addOnSuccessListener {
                                        purchased = true
                                        Toast.makeText(ctx, "فیلم خریداری شد", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(ctx, "خطا: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            },
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

                // اگر پولی و نخریده -> دکمه خرید (کدِ فعلی خودت باقی بماند)

                if (isFree && !purchased) {
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF7AB2B2))
                            .clickable {
                                if (uid.isNullOrBlank()) {
                                    Toast.makeText(ctx, "ابتدا وارد حساب شوید", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }
                                val mid = movieId.toString().trim()
                                if (mid.isEmpty() || '/' in mid) {
                                    Toast.makeText(ctx, "شناسه‌ی فیلم نامعتبر است", Toast.LENGTH_SHORT).show()
                                    return@clickable
                                }

                                val ref = db.collection("users")
                                    .document(uid)
                                    .collection("purchased_movies")
                                    .document(mid)

                                val doc = mapOf(
                                    "title" to title,
                                    "description" to description,
                                    "level" to level,
                                    "duration" to "",        // اگر داری مقدار بده
                                    "price" to price,
                                    "imageUrl" to imageUrl,
                                    "videoUrl" to videoUrl,
                                    "purchasedAt" to FieldValue.serverTimestamp()
                                )

                                ref.set(doc)
                                    .addOnSuccessListener {
                                        purchased = true
                                        Toast.makeText(ctx, "به دوره‌های شما اضافه شد", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(ctx, "خطا: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "شروع یادگیری",
                            color = Color.White,
                            fontFamily = iranSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
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
