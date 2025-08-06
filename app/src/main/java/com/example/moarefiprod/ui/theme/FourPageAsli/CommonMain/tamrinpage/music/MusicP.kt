package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar

// مدل‌های داده
data class MusicItem(
    val id: Int,
    val title: String,
    val artist: String,
    val coverResId: Int,
    val duration: String
)

data class Singer(
    val id: Int,
    val name: String,
    val coverResId: Int
)

class MusicItemWithFavorite(
    val musicItem: MusicItem,
    isFavorite: Boolean
) {
    var isFavorite by mutableStateOf(isFavorite)
}

// کامپوزبل اصلی صفحه
@Composable
fun MusicHomeScreen() {
    var selectedFilter by remember { mutableStateOf("همه") }

    val topSongs = listOf(
        MusicItem(1, "Top Song 1", "Artist A", R.drawable.music, "3:45"),
        MusicItem(2, "Top Song 2", "Artist B", R.drawable.music, "4:12"),
        MusicItem(3, "Top Song 3", "Artist C", R.drawable.music, "2:45"),
        MusicItem(4, "Top Song 4", "Artist D", R.drawable.music, "3:22")
    )

    val famousSingers = listOf(
        Singer(1, "Singer A", R.drawable.music),
        Singer(2, "Singer B", R.drawable.music),
        Singer(3, "Singer C", R.drawable.music),
        Singer(4, "Singer D", R.drawable.music),
        Singer(5, "Singer E", R.drawable.music),
    )

    // ✅ اصلاح: لیست اصلی آهنگ‌ها به یک mutableStateListOf از نوع MusicItemWithFavorite تبدیل شد
    val allMusicListState = remember {
        mutableStateListOf(
            MusicItemWithFavorite(MusicItem(1, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "3:15"), isFavorite = true),
            MusicItemWithFavorite(MusicItem(2, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "2:40"), isFavorite = true),
            MusicItemWithFavorite(MusicItem(3, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "5:00"), isFavorite = false),
            MusicItemWithFavorite(MusicItem(4, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "3:55"), isFavorite = false),
            MusicItemWithFavorite(MusicItem(5, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "4:20"), isFavorite = true),
            MusicItemWithFavorite(MusicItem(6, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "3:35"), isFavorite = false),
            MusicItemWithFavorite(MusicItem(7, "Lena Meyer-Landrut", "Lena Me", R.drawable.music, "4:00"), isFavorite = true),
        )
    }

    // ✅ اصلاح: از allMusicListState برای فیلتر کردن استفاده می‌شود
    val filteredMusicList by remember {
        derivedStateOf {
            when (selectedFilter) {
                "Favoriten" -> allMusicListState.filter { it.isFavorite }
                else -> allMusicListState
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backbtn),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            SearchBar()
            Spacer(modifier = Modifier.height(24.dp))

            // Am beliebtesten Section
            Text(
                text = "Am beliebtesten",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(topSongs) { musicItem ->
                    TopMusicCard(musicItem = musicItem)
                }
                item {
                    AddMoreCard {
                        // TODO: Handle navigation to full list of popular songs
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Berühmte Sänger Section
            Text(
                text = "Berühmte Sänger",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(famousSingers) { singer ->
                    SingerCard(singer = singer) {
                        // TODO: Handle navigation to singer's songs list
                    }
                }
                item {
                    SeeMoreCard {
                        // TODO: Handle navigation to full list of singers
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    text = "Favoriten",
                    isSelected = selectedFilter == "Favoriten",
                    onClick = { selectedFilter = "Favoriten" } // ✅ با کلیک، وضعیت فیلتر تغییر می‌کند
                )
                FilterChip(
                    text = "Alle",
                    isSelected = selectedFilter == "همه",
                    onClick = { selectedFilter = "همه" } // ✅ با کلیک، وضعیت فیلتر تغییر می‌کند
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Music List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // ✅ لیست فیلتر شده نمایش داده می‌شود
                items(filteredMusicList) { musicWithFavorite ->
                    MusicListItem(
                        musicWithFavorite = musicWithFavorite
                    )
                }
            }
        }

        // Bottom Music Player - Fixed at the bottom
        BottomMusicPlayer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}
// بقیه کامپوزبل‌ها بدون تغییر باقی می‌مانند
@Composable
fun TopMusicCard(musicItem: MusicItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .size(120.dp, 100.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = musicItem.coverResId),
            contentDescription = "Top Music",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}

@Composable
fun AddMoreCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp, 100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = "Add More",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SingerCard(singer: Singer, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = singer.coverResId),
            contentDescription = "Famous Singer",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}

@Composable
fun SeeMoreCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "see more",
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFF7AB2B2) else Color.White)
            .border(1.dp, if (isSelected) Color.Transparent else Color.LightGray, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MusicListItem(musicWithFavorite: MusicItemWithFavorite) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = musicWithFavorite.musicItem.coverResId),
                    contentDescription = "Album Cover",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = musicWithFavorite.musicItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = musicWithFavorite.musicItem.artist,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { musicWithFavorite.isFavorite = !musicWithFavorite.isFavorite }) {
                Icon(
                    // استفاده از آیکون‌های آپلود شده
                    painter = painterResource(id = if (musicWithFavorite.isFavorite) R.drawable.favorites else R.drawable.unfavorites),
                    contentDescription = "Favorite",
                    tint = Color.Unspecified, // آیکون‌ها رنگ پیش‌فرض دارند
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { /* TODO: Handle download */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Download",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
@Composable
fun BottomMusicPlayer(modifier: Modifier = Modifier) {
    val isPlaying by remember { mutableStateOf(true) } // وضعیت پخش

    Row(
        modifier = modifier
            .background(Color(0xFFE3F4F4))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.music),
                    contentDescription = "Album Cover",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Lena Meyer-Landrut",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Lena Me",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        IconButton(onClick = { /* TODO: Handle play/pause toggle */ }) {
            Icon(
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.play_ic),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MusicHomeScreenPreview() {
    // از یک NavController ساختگی برای پیش‌نمایش استفاده کنید
    MusicHomeScreen()
}