package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.story

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.min
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun StoryReadingScreen(
    title: String,
    content: String,
    isPurchased: Boolean,
    storyId: String,
    userId: String,
    initialScrollOffset: Int = 0,
    onBack: () -> Unit
){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val scrollState = rememberLazyListState()
    val isScrollRestored = remember { mutableStateOf(false) }

    val allLines = remember(content) { content.split("\n") }
    val visibleLines = if (isPurchased) allLines else allLines.take(24)
    val textToShow = visibleLines.joinToString("\n")

    LaunchedEffect(scrollState, initialScrollOffset) {
        if (!isScrollRestored.value && initialScrollOffset > 0) {
            snapshotFlow { scrollState.isScrollInProgress }
                .collect { inProgress ->
                    if (!inProgress && scrollState.firstVisibleItemIndex == 0) {
                        scrollState.scrollBy(initialScrollOffset.toFloat())
                        isScrollRestored.value = true
                        Log.d("ScrollRestore", "✅ Restored scroll to offset=$initialScrollOffset")
                    }
                }
        }
    }




    DisposableEffect(Unit) {
        onDispose {
            Log.d("FirestoreScrollSave", "✅ Scroll saved for user=$userId, story=$storyId")
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("reading_progress")
                .document(storyId)
                .set(
                    mapOf(
                        "scrollIndex" to scrollState.firstVisibleItemIndex,
                        "scrollOffset" to scrollState.firstVisibleItemScrollOffset,
                        "lastReadAt" to FieldValue.serverTimestamp()
                    )
                )
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC2AE90))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth()
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

                Text(
                    text = title,
                    fontFamily = iranSans,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    modifier = Modifier
                        .padding(top = screenHeight * 0.05f)
                        .align(Alignment.Center)
                )

                IconButton(
                    onClick = { /* ذخیره */ },
                    modifier = Modifier
                        .padding(top = screenHeight * 0.05f, end = 16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Bookmark",
                        tint = Color(0xFFC9C9C9),
                        modifier = Modifier.size(screenWidth * 0.07f)
                    )
                }
            }

            Divider(
                color = Color(0xFF000000),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 26.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 📖 کل متن
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 26.dp),
                state = scrollState
            ) {
                item {
                    Text(
                        text = textToShow,
                        fontFamily = iranSans,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        lineHeight = 26.sp,
                        color = Color(0xFF333333),
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp)) // انتهای صفحه برای زیبایی
        }
    }
}
