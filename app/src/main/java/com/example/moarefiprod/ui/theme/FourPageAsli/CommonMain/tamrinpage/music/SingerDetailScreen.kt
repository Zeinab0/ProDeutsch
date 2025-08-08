package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SingerDetailScreen(
    singer: Singer,
    navController: NavController,
    viewModel: MusicViewModel = viewModel()
) {
    val songs by viewModel.songs.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var likedSongIds by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.getLikedSongIdsForUser(it) { ids ->
                likedSongIds = ids
            }
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val singerSongs = songs.filter { it.artist == singer.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🔹 Header
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
                    modifier = Modifier.size(screenWidth * 0.08f)
                )
            }
        }

        // 🔹 تصویر و نام خواننده
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = screenHeight * 0.01f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = singer.imageUrl,
                contentDescription = singer.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(screenWidth * 0.3f)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.01f))

            Text(
                text = singer.name,
                fontSize = (screenWidth * 0.035f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans
            )
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.03f))

        // 🔹 لیست آهنگ‌ها
        LazyColumn(
            contentPadding = PaddingValues(
                start = screenWidth * 0.05f,
                end = screenWidth * 0.05f,
                bottom = screenHeight * 0.05f
            ),
            verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f)
        ) {
            items(singerSongs) { song ->
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
