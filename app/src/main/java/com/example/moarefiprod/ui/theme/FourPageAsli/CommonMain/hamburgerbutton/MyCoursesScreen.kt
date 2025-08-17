package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.MusicViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.Song
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music.SongItem
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.Story
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story.StoryCardB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.lazy.grid.items // ← این مهمه
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.Movie
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.VideoCard


@Composable
fun MyCoursesScreen(
    navController: NavController,
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    var selected by remember { mutableStateOf(0) } // 0=همه، 1=کلمات، 2=آهنگ
    var query by remember { mutableStateOf("") }
    var stories by remember { mutableStateOf(listOf<Story>()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // دکمه برگشت بالا
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = screenWidth * 0.03f,
                    top = screenHeight * 0.05f
                ),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.015f))

        // ✅ حتماً از SearchBar سفارشی خودت استفاده کن (نه متریال۳)
        com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = ":جستجوی دوره‌های من"
        )



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
            .padding(horizontal = screenWidth * 0.04f, vertical = screenHeight * 0.02f)
        ){

            // 📌 تیتر
            Text(
                text = ":دوره‌های من",
                fontFamily = iranSans,
                fontSize = (screenWidth * 0.035f).value.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-5).dp)
            )



            FilterChipsWithIndicatorRTL(
                categories = listOf( "کلمات", "آهنگ", "داستان", "فیلم"),
                selectedIndex = selected,
                onSelected = { selected = it }
            )



            Spacer(Modifier.height(12.dp))

            LibrarySection(
                userId = uid,
                selectedIndex = selected,
                query = query,
                onSongClick = { song -> navController.navigate("detail/${song.id}") },
                onStoryClick = { story -> navController.navigate("story_detail/${story.id}") },
                onMovieClick = { movie -> navController.navigate("movie_detail/${movie.id}") } // ← جدید
            )



            // 🧩 کارت دوره (نمونه‌ی ثابت)
//            CourseCardCustom()
        }

    }
}

@Composable
fun FilterChipsWithIndicatorRTL(
    categories: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    val indicatorColor = Color(0xFF90CECE)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 0.dp,
            containerColor = Color.White,
            divider = {},
            indicator = { tabs ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabs[selectedIndex]),
                    color = indicatorColor,
                    height = 3.dp
                )
            }
        ) {
            categories.forEachIndexed { i, txt ->
                Tab(selected = selectedIndex == i, onClick = { onSelected(i) }) {
                    Text(txt, fontFamily = iranSans, fontSize = 13.sp, modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}


@Composable
fun LibrarySection(
    userId: String,
    selectedIndex: Int,               // 0=همه، 1=کلمات، 2=آهنگ، 3=داستان، 4=فیلم
    query: String,
    onSongClick: (Song) -> Unit,
    onStoryClick: (Story) -> Unit,
    onMovieClick: (Movie) -> Unit,    // برای ناوبری فیلم
    viewModel: MusicViewModel = viewModel()
) {
    val db = remember { FirebaseFirestore.getInstance() }

    var songs    by remember { mutableStateOf(emptyList<Song>()) }
    var cards    by remember { mutableStateOf(emptyList<Map<String, Any>>()) }
    var stories  by remember { mutableStateOf(emptyList<Story>()) }
    var movies   by remember { mutableStateOf(emptyList<Movie>()) }

    // --- لیسنرهای زنده (کاربر-محور)
    DisposableEffect(userId) {
        val userRef = db.collection("users").document(userId)

        val regSongs = userRef.collection("likedSongs").addSnapshotListener { s, _ ->
            songs = s?.documents?.map { d ->
                Song(
                    id = d.id,
                    title = d.getString("title") ?: "",
                    artist = d.getString("artist") ?: "",
                    imageUrl = d.getString("imageUrl") ?: d.getString("coverUrl") ?: d.getString("thumbnail") ?: "",
                    isFavorite = true
                )
            }.orEmpty()
        }

        val regCards = userRef.collection("my_flashcards").addSnapshotListener { s, _ ->
            cards = s?.documents?.map { it.data ?: emptyMap() }.orEmpty()
        }

        val regStories = userRef.collection("purchased_stories").addSnapshotListener { s, _ ->
            stories = s?.documents?.map { d ->
                Story(
                    id = d.id,
                    title = d.getString("title") ?: "",
                    level = d.getString("level") ?: "",
                    duration = d.getString("duration") ?: "",
                    price = d.getString("price") ?: "",
                    author = d.getString("author") ?: "",
                    summary = d.getString("summary") ?: "",
                    content = d.getString("content") ?: "",
                    imageUrl = d.getString("imageUrl") ?: d.getString("coverUrl") ?: ""
                )
            }.orEmpty()
        }

        val regMovies = userRef.collection("purchased_movies").addSnapshotListener { s, _ ->
            movies = s?.documents?.map { d ->
                Movie(
                    id = d.id,
                    title = d.getString("title") ?: "",
                    description = d.getString("description") ?: "",
                    level = d.getString("level") ?: "",
                    price = d.getString("price") ?: "",
                    videoUrl = d.getString("videoUrl") ?: "",
                    imageUrl = d.getString("imageUrl") ?: "",
                    duration = d.getString("duration") ?: "" // اگر داری
                )
            }.orEmpty()
        }

        onDispose {
            regSongs.remove(); regCards.remove(); regStories.remove(); regMovies.remove()
        }
    }

    // --- فیلتر جستجو
    val q = remember(query) { query.trim() }
    val filteredSongs = remember(songs, q) {
        if (q.isEmpty()) songs else songs.filter { it.title.contains(q, true) || it.artist.contains(q, true) }
    }
    val filteredCards = remember(cards, q) {
        if (q.isEmpty()) cards else cards.filter {
            (it["front"] ?: it["word"] ?: "").toString().contains(q, true) ||
                    (it["back"]  ?: it["meaning"] ?: "").toString().contains(q, true)
        }
    }
    val filteredStories = remember(stories, q) {
        if (q.isEmpty()) stories else stories.filter {
            it.title.contains(q, true) || it.author.contains(q, true) || it.level.contains(q, true)
        }
    }
    val filteredMovies = remember(movies, q) {
        if (q.isEmpty()) movies else movies.filter {
            it.title.contains(q, true) || it.level.contains(q, true) || it.duration.contains(q, true)
        }
    }

    // --- نمایش بر اساس تب
    when (selectedIndex) {
        0 -> { // کلمات
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredCards.size) { i ->
                    val item = filteredCards[i]
                    FlashcardRow(
                        front = item["front"]?.toString() ?: item["word"]?.toString().orEmpty(),
                        back  = item["back"] ?.toString() ?: item["meaning"]?.toString().orEmpty()
                    )
                }
            }
        }

        1 -> { // آهنگ
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredSongs.size) { i ->
                    val song = filteredSongs[i]
                    SongItem(
                        song = song,
                        onClick = onSongClick,
                        onLikeClick = { clicked ->
                            val newState = !clicked.isFavorite
                            viewModel.toggleSongLike(
                                userId = userId,
                                song   = clicked.copy(isFavorite = newState),
                                liked  = newState
                            )
                        }
                    )
                }
            }
        }

        2 -> { // داستان
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredStories, key = { it.id }) { story ->
                    // ⛔️ قیمت را نمایش نده
                    StoryCardB(
                        story = story,
                        title = story.title,
                        level = story.level,
                        duration = story.duration,
                        price = "", // ← عمداً خالی
                        modifier = Modifier.clickable { onStoryClick(story) }
                    )
                }
            }
        }

        3 -> { // فیلم
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMovies, key = { it.id }) { m ->
                    // ⛔️ قیمت را نمایش نده؛ بقیه اطلاعات دقیقاً از دیتابیس
                    VideoCard(
                        title = m.title,
                        level = m.level,
                        duration = m.duration,
                        price = "", // ← عمداً خالی تا هیچ قیمتی دیده نشه
                        modifier = Modifier.clickable { onMovieClick(m) }
                    )
                }
            }
        }
    }

}


@Composable
fun FlashcardRow(front: String, back: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(back,  fontFamily = iranSans, color = Color.Gray)
        Text(front, fontFamily = iranSans, fontWeight = FontWeight.Bold)
    }
}
