package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.moarefiprod.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun MovieScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var selectedFilter by remember { mutableStateOf("همه") }

    // داده‌ها
    val moviesState = remember { mutableStateOf<List<Movie>>(emptyList()) }

    // 🔴 قبلاً LaunchedEffect(Unit) با getMoviesFromFirestore داشتی — حذف شد
    // 🟢 جایگزین: گوش‌دادن زنده به فایربیس
    DisposableEffect(Unit) {
        val reg = listenMoviesLive { list ->
            Log.d("Firestore", "Live movies: ${list.size}")
            moviesState.value = list
        }
        onDispose { reg.remove() }
    }

    val movieList = moviesState.value

    // سرچ مخصوص همین صفحه
    var query by remember { mutableStateOf("") }
    val q: String = remember(query) { query.trim() }

    // شاخص اسلایدر بالا
    val centerIndex = remember { mutableStateOf(0) }

    // 1) فیلتر تب‌ها (چیپ‌ها)
    val filteredByTab: List<Movie> = remember(movieList, selectedFilter) {
        when (selectedFilter) {
            "همه" -> movieList
            "رایگان" -> movieList.filter {
                it.price.equals("رایگان", true) || it.price.equals("Frei", true) || it.price.equals("Free", true)
            }
            else -> movieList.filter { m ->
                m.level.equals(selectedFilter, true) || m.price.equals(selectedFilter, true)
            }
        }
    }

    // 2) فیلتر سرچ روی نتیجهٔ تب‌ها
    val filteredMovies: List<Movie> = remember(filteredByTab, q) {
        if (q.isEmpty()) filteredByTab
        else {
            filteredByTab.filter { m ->
                listOf(
                    m.title,
                    m.level,
                    m.price,
                    m.description // اگر ندارى، خط را بردار
                ).any { it.contains(q, ignoreCase = true) }
            }
        }
    }


    val uid = remember { FirebaseAuth.getInstance().currentUser?.uid }
    val purchasedIdsState = remember { mutableStateOf<Set<String>>(emptySet()) }

    DisposableEffect(uid) {
        if (uid == null) {
            purchasedIdsState.value = emptySet()
            onDispose { /* no-op */ }
        } else {
            val reg = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("purchased_movies")
                .addSnapshotListener { snap, _ ->
                    purchasedIdsState.value = snap?.documents?.mapNotNull { it.id }?.toSet().orEmpty()
                }
            onDispose { reg.remove() }
        }
    }

    val purchasedIds = purchasedIdsState.value




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(modifier = Modifier.fillMaxWidth()) {
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
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        // 🔍 سرچ فقط برای فیلم‌ها (سفارشی خودت)
       SearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = ":جستجوی فیلم"
        )

        Text(
            text = "جدیدترین ها",
            fontSize = 12.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 27.dp, top = 20.dp)
                .align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(5.dp))

        // اسلایدر سه‌تایی بالا (از کل لیست یا اگر خواستی از filteredMovies)
        if (movieList.size >= 3) {
            val visibleItems = remember(centerIndex.value, movieList) {
                val size = movieList.size
                val left = (centerIndex.value - 1 + size) % size
                val right = (centerIndex.value + 1) % size
                listOf(left, centerIndex.value, right)
            }

            LaunchedEffect(movieList.size) {
                while (movieList.isNotEmpty()) {
                    delay(3000)
                    centerIndex.value = (centerIndex.value + 1) % movieList.size
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                visibleItems.forEachIndexed { position, index ->
                    val isCenter = index == centerIndex.value
                    val movie = movieList[index]

                    val scale by animateFloatAsState(
                        targetValue = if (position == 1) 1.1f else 0.9f,
                        label = "scale"
                    )
                    val alpha by animateFloatAsState(
                        targetValue = if (position == 1) 1f else 0.8f,
                        label = "alpha"
                    )

                    MovieCard(
                        isCenter = isCenter,
                        title = movie.title,
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                                translationX = when (position) {
                                    0 -> -30f
                                    2 -> 30f
                                    else -> 0f
                                }
                            }
                            .animateContentSize()
                            .clickable {
                                navController.navigate("movie_detail/${movie.id}")
                            }
                    )
                }
            }
        } else if (movieList.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                movieList.forEach { movie ->
                    MovieCard(
                        isCenter = true,
                        title = movie.title,
                        modifier = Modifier.clickable {
                            navController.navigate("movie_detail/${movie.id}")
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.22f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // چیپ‌های فیلتر
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            listOf("B2", "B1", "A2", "A1", "رایگان", "همه").forEach { label ->
                FilterChip(
                    text = label,
                    selected = selectedFilter == label,
                    onClick = { selectedFilter = label }
                )
            }
        }

        // گرید پایین با فیلتر نهایی (تب + سرچ)
        if (filteredMovies.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, end = 25.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredMovies, key = { it.id }) { movie ->
                    val purchased = purchasedIds.contains(movie.id)
                    VideoCard(
                        title = movie.title,
                        level = movie.level,
                        duration = movie.duration,
                        price = movie.price,
                        purchased = purchased,                 // 👈 جدید
                        modifier = Modifier.clickable {
                            navController.navigate("movie_detail/${movie.id}")
                        }
                    )
                }

            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("موردی یافت نشد", fontFamily = iranSans, color = Color.Gray)
            }
        }
    }
}

@Composable
fun MovieCard(
    isCenter: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    title: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val cardWidth = if (isCenter) screenWidth * 0.28f else screenWidth * 0.24f
    val cardHeight = if (isCenter) screenHeight * 0.15f else screenHeight * 0.13f
    val imageHeight = cardHeight * 0.45f
    val padding = screenWidth * 0.025f
    val textSize = if (isCenter) screenWidth.value * 0.022f else screenWidth.value * 0.018f
    val buttonWidth = cardWidth * 0.7f
    val buttonHeight = cardHeight * 0.12f
    val buttonTextSize = if (isCenter) textSize * 1.2f else textSize

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFEF)),
        modifier = modifier
            .width(cardWidth)
            .height(cardHeight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = textSize.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(cardHeight * 0.03f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cours1),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(cardHeight * 0.05f))

            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .height(buttonHeight)
                    .background(Color(0xFF7AB2B2), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "شروع",
                    fontSize = buttonTextSize.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Color(0xFF7AB2B2) else Color(0xFFE0E0E0),
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp),
        tonalElevation = if (selected) 4.dp else 0.dp
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 2.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
        )
    }
}

@Composable
fun VideoCard(
    title: String,
    level: String,
    duration: String,
    price: String,
    purchased: Boolean = false,          // 👈 جدید
    imageRes: Int? = null,
    modifier: Modifier = Modifier,
){
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val width = maxWidth
        val cardHeight = width * 1.2f // 👈 نسبت به عرض کارت (200.dp تقریبی = 0.85 * 235.dp)

        Card(
            modifier = modifier // 👈 باید اینجا از modifier استفاده بشه
                .fillMaxWidth()
                .height(cardHeight)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = Color(0xFF000000),
                    spotColor = Color(0xFF9B9B9B)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {

                val imageHeight = width * 0.6f
                val spacingSmall = width * 0.07f
                val fontScale = width.value / 15f
                val iconSize = width * 0.4f

                Column(
                    modifier = Modifier
                        .padding(spacingSmall)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baner),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(0.7f)
                        )

                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "پخش",
                            modifier = Modifier.size(iconSize),
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(spacingSmall))

                    Text(
                        text = title,
                        textAlign = TextAlign.Right,
                        fontFamily = iranSans,
                        fontSize = (fontScale * 1.2f).sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )

                    Spacer(modifier = Modifier.height(spacingSmall / 2))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "سطح: $level",
                            fontSize = (fontScale * 1.0f).sp,
                            fontFamily = iranSans,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(textDirection = TextDirection.Rtl)
                        )
                        Text(
                            text = "مدت زمان: $duration",
                            fontSize = (fontScale * 1.0f).sp,
                            fontFamily = iranSans,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(textDirection = TextDirection.Rtl)
                        )
                    }

                    Spacer(modifier = Modifier.height(spacingSmall / 2))


                    if (!purchased) {
                        Text(
                            text = price,
                            fontSize = (fontScale * 1.0f).sp,
                            fontWeight = FontWeight.Bold,
                            color = if (price == "رایگان") Color.Green else Color.Black,
                            fontFamily = iranSans,
                            style = TextStyle(textDirection = TextDirection.Rtl)
                        )
                    }

                }
            }
        }
}
