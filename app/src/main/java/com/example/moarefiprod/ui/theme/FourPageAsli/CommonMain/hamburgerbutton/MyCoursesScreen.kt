package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.Movie
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.movie.VideoCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel.FlashcardViewModel
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment


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

            // ⬇️ پارامتر navController اضافه شد
            LibrarySection(
                userId = uid,
                selectedIndex = selected,
                query = query,
                navController = navController, // ← اضافه شد
                onSongClick = { song -> navController.navigate("detail/${song.id}") },
                onStoryClick = { story -> navController.navigate("story_detail/${story.id}") },
                onMovieClick = { movie -> navController.navigate("movie_detail/${movie.id}") }
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
    selectedIndex: Int,               // 0=کلمات، 1=آهنگ، 2=داستان، 3=فیلم
    query: String,
    navController: NavController,     // ← اضافه شد
    onSongClick: (Song) -> Unit,
    onStoryClick: (Story) -> Unit,
    onMovieClick: (Movie) -> Unit,
    viewModel: MusicViewModel = viewModel(),
    flashVm: FlashcardViewModel = viewModel() // ← ویومدل فلش‌کارت
) {
    val db = remember { FirebaseFirestore.getInstance() }

    var songs   by remember { mutableStateOf(emptyList<Song>()) }
    var cards   by remember { mutableStateOf(emptyList<Map<String, Any>>()) } // «کلمات من» (آیتم‌های منفرد)
    var stories by remember { mutableStateOf(emptyList<Story>()) }
    var movies  by remember { mutableStateOf(emptyList<Movie>()) }

    // --- لیسنرهای فعلی‌ات برای کاربر ---
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

        val regCards = userRef.collection("purchases_flashcards").addSnapshotListener { s, _ ->
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
                    duration = d.getString("duration") ?: ""
                )
            }.orEmpty()
        }

        onDispose { regSongs.remove(); regCards.remove(); regStories.remove(); regMovies.remove() }
    }

    // --- سرچ روی لیست‌های فعلی‌ات ---
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

    // --- دوره‌های آموزش کلمات (از خریدها) ---
    // اطمینان از پر شدن مرجع کارت‌ها؛ اگه در ViewModelت داخل init نیست:
    LaunchedEffect(Unit) { flashVm.listenAllCards() }  // در صورت لزوم


    // 1) دوره‌های خریداری‌شده‌ی کلمات از دیتابیس
    val purchased by flashVm.purchasedCards.collectAsState()
    val purchasedWordCourses = remember(purchased, query) {
        val q = query.trim()
        val base = purchased.filter { it.count > 0 }   // کارت‌های آموزشی کلمات
        if (q.isEmpty()) base else base.filter { c ->
            c.title.contains(q, true) || c.description.contains(q, true)
        }
    }

// 2) فیلتر امن برای آیتم‌های «کلمات» تا کارت خالی رندر نشه
    val filteredCardsSafe = remember(filteredCards, query) {
        val q = query.trim()
        val base = filteredCards.mapNotNull { m ->
            val front = (m["front"] ?: m["word"])?.toString()?.trim().orEmpty()
            val back  = (m["back"]  ?: m["meaning"])?.toString()?.trim().orEmpty()
            if (front.isBlank() && back.isBlank()) null else mapOf("front" to front, "back" to back)
        }
        if (q.isEmpty()) base else base.filter { m ->
            m["front"].toString().contains(q, true) || m["back"].toString().contains(q, true)
        }
    }


    when (selectedIndex) {
        0 -> { // کلمات (دوره‌های آموزش کلمات خریداری‌شده)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(purchasedWordCourses.size) { i ->
                    val card = purchasedWordCourses[i]
                    // اگر می‌خوای key هم مثل تب آهنگ نباشه و پایدار باشه، می‌تونی از itemsIndexed + key استفاده کنی
                    flashCard(
                        cards = card,
                        navController = navController,
                        viewModel = flashVm
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
                    StoryCardB(
                        story = story,
                        title = story.title,
                        level = story.level,
                        duration = story.duration,
                        price = "", // قیمت عمداً مخفی
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
                    VideoCard(
                        title = m.title,
                        level = m.level,
                        duration = m.duration,
                        price = "", // قیمت عمداً مخفی
                        modifier = Modifier.clickable { onMovieClick(m) }
                    )
                }
            }
        }
    }
}