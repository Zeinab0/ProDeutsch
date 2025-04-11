package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.allcartlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.SearchBar
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain.WordStatus


@Composable
fun WordListPage(
    words: List<Word>,
    selectedView: WordViewType,
    onViewChange: (WordViewType) -> Unit,
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // ✅ دریافت فیلترهای ارسال‌شده از صفحه قبلی
    val selectedStatuses = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<List<WordStatus>>("filter_statuses")
            ?.toSet() ?: emptySet()
    }

    val filteredWords = if (selectedStatuses.isEmpty()) words
    else words.filter { it.status in selectedStatuses }

    Scaffold(
        bottomBar = {
            BottomViewSwitcher(current = selectedView, onSelect = onViewChange)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // 🔙 دکمه برگشت
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = screenWidth * 0.03f)
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

            // 🔍 نوار جستجو
            SearchBar()

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ نمایش کلمات فیلتر شده
            if (selectedView == WordViewType.LIST) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 26.dp,
                        end = 26.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(filteredWords.size) { index ->
                        val word = filteredWords[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                                .border(1.dp, Color.Black)
                                .padding(vertical = 10.dp, horizontal = 10.dp),
                            horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = word.persian,
                                    fontFamily = iranSans,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start
                                )
                            }

                            // 🔻 جداکننده عمودی
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color.LightGray)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                                    .border(1.dp, Color.Black)
                                    .padding(vertical = 10.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = word.german,
                                    fontFamily = iranSans,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    WordCardsGrid(
                        words = filteredWords,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
