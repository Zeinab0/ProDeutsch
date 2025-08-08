package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.Text


@Composable
fun MusicScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val viewModel: MusicViewModel = viewModel()
    val songs by viewModel.songs.collectAsState()
    val singers by viewModel.singers.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var likedSongs by remember { mutableStateOf(listOf<Song>()) }
    var likedSongIds by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(userId) {
        userId?.let { uid ->
            viewModel.getLikedSongsForUser(uid) {
                likedSongs = it
            }
        }
    }

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.getLikedSongIdsForUser(it) { ids ->
                likedSongIds = ids
            }
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
                    .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f)
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

        // SearchBar
        com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = screenWidth * 0.06f) // 24.dp تقریباً
        ) {
            // Meine Favoriten
            Text(
                text = "Meine Favoriten",
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = screenHeight * 0.02f, bottom = screenHeight * 0.01f)
                    .align(Alignment.Start)
            )


            LazyRow {
                items(likedSongs.take(5)) { song ->
                    FavoriteSongCard(song = song) {
                        navController.navigate("detail/${song.id}")
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .width(screenWidth * 0.4f) // حدود 160.dp
                            .height(screenHeight * 0.1f) // حدود 80.dp
                            .clip(RoundedCornerShape(screenWidth * 0.03f))
                            .background(Color(0xFFE0E0E0))
                            .clickable {
                                navController.navigate("favorites")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mehr anzeigen",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Berühmte Sänger
            Text(
                text = "Berühmte Sänger",
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        top = screenHeight * 0.02f,
                        bottom = screenHeight * 0.01f
                    )
                    .align(Alignment.Start)
            )

            FamousSingersRow(singers = singers, navController = navController)

            Text(
                text = "Alle",
                fontSize = (screenWidth * 0.035f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        top = screenHeight * 0.02f,
                        bottom = screenHeight * 0.01f
                    )
                    .align(Alignment.Start)
            )


            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f)
            ) {
                items(songs) { song ->
                    val isFavorite = likedSongIds.contains(song.id)
                    val updatedSong = song.copy(isFavorite = isFavorite)

                    SongItem(
                        song = updatedSong,
                        onClick = { navController.navigate("detail/${song.id}") },
                        onLikeClick = { clickedSong ->
                            val newState = !clickedSong.isFavorite
                            userId?.let { uid ->
                                viewModel.toggleSongLike(
                                    userId = uid,
                                    song = clickedSong.copy(isFavorite = newState),
                                    liked = newState
                                ) {
                                    viewModel.getLikedSongIdsForUser(uid) { ids ->
                                        likedSongIds = ids
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun FavoriteSongCard(song: Song, onClick: (Song) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val cardWidth = screenWidth * 0.4f           // حدود 40٪ از عرض صفحه
    val cardHeight = screenHeight * 0.1f        // حدود 12٪ از ارتفاع صفحه
    val imageIconSize = screenWidth * 0.1f       // سایز آیکن جایگزین
    val fontSize = (screenWidth * 0.032f).value.sp
    val spacing = screenHeight * 0.007f

    Column(
        modifier = Modifier
            .width(cardWidth)
            .padding(end = screenWidth * 0.025f)
            .clickable { onClick(song) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = cardWidth, height = cardHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (song.imageUrl.isNullOrEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.music),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(imageIconSize)
                )
            } else {
                AsyncImage(
                    model = song.imageUrl,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing))

        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSize),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FamousSingersRow(singers: List<Singer>, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val itemSize = screenWidth * 0.25f // مثلاً 25% از عرض صفحه
    val fontSize = (screenWidth * 0.03f).value.sp

    LazyRow(
        contentPadding = PaddingValues(horizontal = screenWidth * 0.04f),
        horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.04f)
    ) {
        items(singers.take(5)) { singer ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(itemSize)
                    .clickable {
                        navController.navigate(
                            "singerDetail/${Uri.encode(singer.name)}/${Uri.encode(singer.imageUrl)}"
                        )
                    }
            ) {
                AsyncImage(
                    model = singer.imageUrl,
                    contentDescription = singer.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(itemSize)
                        .clip(CircleShape)
                )
            }
        }

        // ➕ Mehr anzeigen
        item {
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(CircleShape)
                    .background(Color(0xFFEAEAEA))
                    .clickable {
                        navController.navigate("famous_singers")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mehr anzeigen",
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun SongItem(
    song: Song,
    onClick: (Song) -> Unit,
    onLikeClick: (Song) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val rowHeight = screenHeight * 0.075f              // حدوداً 9٪ از ارتفاع صفحه
    val imageWidth = screenWidth * 0.22f              // عرض عکس
    val imageHeight = rowHeight                       // تصویر هم‌ارتفاع با ردیف
    val textPadding = screenWidth * 0.03f
    val titleFont = (screenWidth * 0.038f).value.sp
    val artistFont = (screenWidth * 0.032f).value.sp
    val likeIconSize = screenWidth * 0.07f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .background(color = Color(0xFFE5E5E5), RoundedCornerShape(12.dp))
            .padding(end = screenWidth * 0.04f)
    ) {
        // تصویر + متن (کلیک‌پذیر)
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onClick(song) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = song.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = imageWidth, height = imageHeight)
                    .clip(RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp))
            )

            Column(
                modifier = Modifier.padding(start = textPadding)
            ) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000),
                    fontSize = titleFont
                )
                Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light,
                    fontSize = artistFont
                )
            }
        }

        // آیکون لایک
        IconButton(
            onClick = { onLikeClick(song) },
            modifier = Modifier.size(likeIconSize)
        ) {
            Icon(
                imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = if (song.isFavorite) Color.Red else Color.Gray,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}



