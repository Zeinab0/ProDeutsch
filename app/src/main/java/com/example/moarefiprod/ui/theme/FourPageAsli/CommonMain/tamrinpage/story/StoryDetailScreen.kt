package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun StoryDetailScreen(
    id: String,
    title: String,
    author: String,
    duration: String,
    price: String,
    summary: String,
    imageUrl: String,
    onBack: () -> Unit,
    navController: NavController // اضافه شده
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var isPurchased by rememberSaveable { mutableStateOf(false) }

    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db  = remember { FirebaseFirestore.getInstance() }

    // ✅ سنجاق به لایف‌سایکل: با تغییر id یا uid دوباره وصل می‌شه
    DisposableEffect(id, uid) {
        if (uid == null || id.isBlank()) {
            isPurchased = false
            onDispose { }
        } else {
            val reg = db.collection("users")
                .document(uid)
                .collection("purchased_stories")
                .document(id)
                .addSnapshotListener { doc, _ ->
                    isPurchased = (doc != null && doc.exists())
                }
            onDispose { reg.remove() }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB4E0E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFD4F0F0), Color(0xFF52D3D3))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔙 دکمه برگشت و تصویر و عنوان
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(top = screenHeight * 0.05f, start = 16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(screenWidth * 0.09f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = screenHeight * 0.1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = screenWidth * 0.56f)
                            .heightIn(max = screenHeight * 0.32f)
                            .shadow(34.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = title,
                        fontFamily = iranSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 🧾 جزییات داستان
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .shadow(
                        22.dp,
                        shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                    .background(Color.White, shape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(45.dp))

                    // 🔤 عنوان‌های بالا
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("زمان مطالعه", "نویسنده", "قیمت").forEach {
                            Text(
                                text = it,
                                fontFamily = iranSans,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 🔢 مقادیر بالا
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            duration,
                            author,
                            if (isPurchased) "خریداری شده" else price
                        ).forEach {
                            Text(
                                text = it,
                                fontFamily = iranSans,
                                fontSize = 14.sp,
                                color = Color(0xFF7AB2B2),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = TextStyle(textDirection = TextDirection.Rtl)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "خلاصه داستان:",
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = summary,
                        fontFamily = iranSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF7AB2B2),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        maxLines = 7,
                        minLines = 7,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val ctx = LocalContext.current
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    // بررسی رایگان بودن
                    val isFree = price.equals("رایگان", true) || price.equals("frei", true) || price.equals("free", true)

// دکمه خرید (فقط پولی‌ها)
                    if (!isPurchased && !isFree && price.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .zIndex(3f)
                                .width(screenWidth * 0.33f)
                                .height(screenHeight * 0.07f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF7AB2B2))
                                .clickable {
                                    if (uid.isNullOrBlank()) {
                                        Toast.makeText(ctx, "ابتدا وارد حساب شوید", Toast.LENGTH_SHORT).show()
                                        return@clickable
                                    }

                                    val db = FirebaseFirestore.getInstance()
                                    val storyDoc = mapOf(
                                        "title" to title,
                                        "author" to author,
                                        "duration" to duration,
                                        "price" to price,
                                        "imageUrl" to imageUrl,
                                        "purchasedAt" to FieldValue.serverTimestamp()
                                    )

                                    db.collection("users").document(uid)
                                        .collection("purchased_stories").document(id)
                                        .set(storyDoc)
                                        .addOnSuccessListener {
                                            isPurchased = true
                                            Toast.makeText(ctx, "داستان خریداری شد", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(ctx, "خطا در خرید: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("خرید", color = Color.White, fontFamily = iranSans, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

// دکمه افزودن به داستان‌های من (فقط رایگان‌ها)
                    if (!isPurchased && isFree) {
                        Box(
                            modifier = Modifier
                                .zIndex(3f)
                                .width(screenWidth * 0.45f)
                                .height(screenHeight * 0.07f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF7AB2B2))
                                .clickable {
                                    if (uid.isNullOrBlank()) {
                                        Toast.makeText(ctx, "ابتدا وارد حساب شوید", Toast.LENGTH_SHORT).show()
                                        return@clickable
                                    }

                                    val db = FirebaseFirestore.getInstance()
                                    val storyDoc = mapOf(
                                        "title" to title,
                                        "author" to author,
                                        "duration" to duration,
                                        "price" to price, // می‌تونی "" بذاری
                                        "imageUrl" to imageUrl,
                                        "purchasedAt" to FieldValue.serverTimestamp()
                                    )

                                    db.collection("users").document(uid)
                                        .collection("purchased_stories").document(id)
                                        .set(storyDoc)
                                        .addOnSuccessListener {
                                            isPurchased = true
                                            Toast.makeText(ctx, "به داستان‌های من اضافه شد", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(ctx, "خطا: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("افزودن به داستان‌های من", color = Color.White, fontFamily = iranSans, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }


                }
            }
        }

        // 🟦 دکمه شروع مطالعه که وسط دو بخش بیاد
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 26.dp)
                .zIndex(2f)
                .clickable {
                    if (id.isNotBlank()) {
                        navController.navigate("reading_screen/$id")
                    } else {
                        Log.e("StoryDetailScreen", "⛔ ID داستان خالی است و نمی‌توان ادامه داد.")
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .width(screenWidth * 0.45f)
                    .height(screenHeight * 0.07f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF7AB2B2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "شروع مطالعه",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Medium
                )
            }
        }
     }
}