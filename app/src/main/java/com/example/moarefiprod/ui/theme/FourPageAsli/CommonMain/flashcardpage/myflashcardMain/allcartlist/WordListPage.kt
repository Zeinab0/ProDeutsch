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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun WordListPage(
    words: List<Word>,
    selectedView: WordViewType,
    onViewChange: (WordViewType) -> Unit,
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // وضعیت‌های انتخاب‌شده از صفحه قبل (فقط یک‌بار)
    val selectedStatuses = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<List<WordStatus>>("filter_statuses")
            ?.toSet() ?: emptySet()
    }

    // سرچ محلی
    var query by remember { mutableStateOf("") }
    val q = remember(query) { query.trim().lowercase() }

    // 1) فیلتر بر اساس وضعیت
    val filteredByStatus = remember(words, selectedStatuses) {
        if (selectedStatuses.isEmpty()) words
        else words.filter { it.status in selectedStatuses }
    }

    // 2) فیلتر بر اساس سرچ
    val filteredWords = remember(filteredByStatus, q) {
        if (q.isEmpty()) filteredByStatus
        else filteredByStatus.filter { w ->
            listOfNotNull(w.text, w.translation)
                .any { it.lowercase().contains(q) }
        }
    }

    Scaffold(
        bottomBar = { BottomViewSwitcher(current = selectedView, onSelect = onViewChange) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // 🔙 دکمه‌ی برگشت
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

            // 🔍 سرچ مخصوص کلمات
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = ":جستجوی کلمه"
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedView == WordViewType.LIST) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 26.dp, end = 26.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(
                        items = filteredWords,
                        key = { it.text } // اگر id داری، از it.id استفاده کن
                    ) { word ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ترجمه (چپ)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, Color.Black)
                                    .padding(vertical = 10.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = word.translation,
                                    fontFamily = iranSans,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start
                                )
                            }

                            // جداکننده عمودی
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color.LightGray)
                            )

                            // واژه (راست)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, Color.Black)
                                    .padding(vertical = 10.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = word.text,
                                    fontFamily = iranSans,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            } else {
                // گرید کارت‌ها با همان فیلترها
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    WordCardsGrid(words = filteredWords)
                }
            }
        }
    }
}
