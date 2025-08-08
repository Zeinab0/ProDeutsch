package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.google.firebase.auth.FirebaseAuth

@Composable
fun faveriteScreen(
    navController: NavController,
    viewModel: MusicViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var likedSongs by remember { mutableStateOf(listOf<Song>()) }

    LaunchedEffect(userId) {
        userId?.let { uid ->
            viewModel.getLikedSongsForUser(uid) {
                likedSongs = it
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
                    modifier = Modifier.size(screenWidth * 0.08f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.06f)
        ) {
            // عنوان لیست
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = screenHeight * 0.03f, bottom = screenHeight * 0.015f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dola_chang),
                    contentDescription = "Lyrics Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.045f)
                )
                Spacer(modifier = Modifier.width(screenWidth * 0.02f))
                Text(
                    text = "Am beliebtesten",
                    fontSize = (screenWidth * 0.038f).value.sp,
                    fontFamily = iranSans,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))

            // لیست آهنگ‌های لایک‌شده
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f),
                contentPadding = PaddingValues(bottom = screenHeight * 0.05f)
            ) {
                items(likedSongs) { song ->
                    SongItem(
                        song = song.copy(isFavorite = true),
                        onClick = { navController.navigate("detail/${song.id}") },
                        onLikeClick = { clickedSong ->
                            val newState = !clickedSong.isFavorite
                            userId?.let { uid ->
                                viewModel.toggleSongLike(
                                    userId = uid,
                                    song = clickedSong.copy(isFavorite = newState),
                                    liked = newState
                                ) {
                                    viewModel.getLikedSongsForUser(uid) {
                                        likedSongs = it
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