package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story

import android.annotation.SuppressLint
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.rememberAsyncImagePainter
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.FilterChip
import kotlinx.coroutines.delay


@Composable
fun StoryScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var selectedFilter by remember { mutableStateOf("Alle") }
    val stories = remember { mutableStateOf<List<Story>>(emptyList()) }
    val centerIndex = remember { mutableStateOf(0) }

    // ✅ لایف‌سایکل‌-سیف: روی ورود گوش می‌دیم، روی خروج لغو می‌کنیم
    DisposableEffect(Unit) {
        val reg = listenStoriesLive { stories.value = it }
        onDispose { reg.remove() }
    }

    val storyList = stories.value

    // 🔍 سرچ محلی
    var query by remember { mutableStateOf("") }
    val q: String = remember(query) { query.trim() }

    // 1) فیلتر چیپ‌ها
    val filteredByTab: List<Story> = remember(storyList, selectedFilter) {
        when (selectedFilter) {
            "Alle" -> storyList
            "Frei" -> storyList.filter { it.price.equals("Frei", true) || it.price == "رایگان" }
            else   -> storyList.filter { it.level == selectedFilter || it.price == selectedFilter }
        }
    }

    // 2) فیلتر سرچ روی نتیجهٔ بالا
    val filteredStories: List<Story> = remember(filteredByTab, q) {
        if (q.isEmpty()) filteredByTab else filteredByTab.filter { s ->
            listOfNotNull(
                s.title,
                s.level,
                s.price,
                // اگر در مدل Story فیلد دیگری مثل description داری، اضافه کن:
//                s.description
            ).any { it.contains(q, ignoreCase = true) }
        }
    }

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

        // ✅ از سرچ‌بار سفارشی پروژه استفاده کن (نه SearchBar متریال)
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = ":جستجوی داستان"
        )

        Text(
            text = "Aktuelles",
            fontSize = 12.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 27.dp, top = 20.dp)
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(5.dp))

        if (storyList.size >= 3) {
            val visibleItems = remember(centerIndex.value, storyList) {
                val size = storyList.size
                val left = (centerIndex.value - 1 + size) % size
                val right = (centerIndex.value + 1) % size
                listOf(left, centerIndex.value, right)
            }

            LaunchedEffect(storyList.size) {
                while (storyList.isNotEmpty()) {
                    delay(3000)
                    centerIndex.value = (centerIndex.value + 1) % storyList.size
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                visibleItems.forEachIndexed { position, index ->
                    val isCenter = index == centerIndex.value
                    val story = storyList[index]

                    val scale by animateFloatAsState(
                        targetValue = if (position == 1) 1.1f else 0.9f,
                        label = "scale"
                    )
                    val alpha by animateFloatAsState(
                        targetValue = if (position == 1) 1f else 0.8f,
                        label = "alpha"
                    )

                    StoryCard(
                        story = story,
                        isCenter = isCenter,
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
                                navController.navigate("story_detail/${story.id}")
                            }
                    )
                }
            }
        } else if (storyList.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                storyList.forEach { story ->
                    StoryCard(
                        story = story,
                        isCenter = true,
                        modifier = Modifier
                            .animateContentSize()
                            .clickable {
                                navController.navigate("story_detail/${story.id}")
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
            ) { CircularProgressIndicator() }
        }

        // فیلتر چیپ‌ها
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            listOf("B2", "B1", "A2", "A1", "Frei", "Alle").forEach { label ->
                FilterChip(
                    text = label,
                    selected = selectedFilter == label,
                    onClick = { selectedFilter = label }
                )
            }
        }

        if (filteredStories.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, end = 25.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredStories, key = { it.id }) { story ->
                    StoryCardB(
                        story = story,
                        title = story.title,
                        level = story.level,
                        duration = story.duration,
                        price = story.price,
                        modifier = Modifier.clickable {
                            navController.navigate("story_detail/${story.id}")
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // اگر storyList خالیه یعنی هنوز لود نشده → لودینگ
                // اگر storyList پره ولی filteredStories خالیه → “موردی یافت نشد”
                if (storyList.isEmpty()) CircularProgressIndicator()
                else Text("موردی یافت نشد", fontFamily = iranSans, color = Color.Gray)
            }
        }
    }
}


@Composable
fun StoryCard(
    story: Story,
    isCenter: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val cardWidth = if (isCenter) screenWidth * 0.28f else screenWidth * 0.24f
    val cardHeight = if (isCenter) screenHeight * 0.18f else screenHeight * 0.16f
    val imageHeight = cardHeight * 0.65f
    val padding = screenWidth * 0.02f
    val textSize = if (isCenter) screenWidth.value * 0.022f else screenWidth.value * 0.018f
    val buttonWidth = cardWidth * 0.7f
    val buttonHeight = cardHeight * 0.12f
    val buttonTextSize = if (isCenter) textSize * 1.1f else textSize

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFEF)),
        modifier = modifier
            .width(cardWidth)
            .height(cardHeight),
//            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = story.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(cardHeight * 0.03f))

            Text(
                text = story.title,
                fontSize = textSize.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(cardHeight * 0.03f))

            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .height(buttonHeight)
                    .background(Color(0xFF7AB2B2), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start",
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
fun StoryCardB(
    story: Story,
    title: String,
    level: String,
    duration: String,
    price: String,
    modifier: Modifier = Modifier,

    ) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val width = maxWidth
        val cardHeight = width * 1.4f // 👈 نسبت به عرض کارت (200.dp تقریبی = 0.85 * 235.dp)

        Card(
            modifier = modifier
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

            val imageHeight = width * 0.9f
            val spacingSmall = width * 0.07f
            val fontScale = width.value / 15f

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
                        painter = rememberAsyncImagePainter(model = story.imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(width * 0.01f))

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

                Spacer(modifier = Modifier.height(width * 0.01f))

                Text(
                    text = "تعداد صفحه: $duration",
                    fontSize = (fontScale * 1.0f).sp,
                    fontFamily = iranSans,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(), // 👈 اضافه شد
                    textAlign = TextAlign.Right,
                    style = TextStyle(textDirection = TextDirection.Rtl)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = price,
                        fontSize = (fontScale * 1.0f).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (price == "رایگان") Color.Green else Color.Black,
                        fontFamily = iranSans,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )
                    Text(
                        text = "سطح: $level",
                        fontSize = (fontScale * 1.0f).sp,
                        fontFamily = iranSans,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        style = TextStyle(textDirection = TextDirection.Rtl),

                    )
                }
            }
        }
    }
}