package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage

import FilterChips
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.NewLabel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel.FlashcardViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun flashcardpage(
    navController: NavController,
    query: String = "" // ← از HomeScreen پاس بده (flashcardsQuery)
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val q = remember(query) { query.trim().lowercase() }
    var selectedFilter by remember { mutableStateOf("همه") }

    val vm: FlashcardViewModel = viewModel()
    LaunchedEffect(Unit) { vm.listenAllCards() }
    val allCards by vm.allCards.collectAsState()
    val wordCards = remember(allCards) { allCards.filter { it.count > 0 } }


    // فیلتر تب‌ها (جدید/رایگان/همه) + سرچ
    val filteredCardsByTab = remember(allCards, selectedFilter) {
        when (selectedFilter) {
            "رایگان" -> allCards.filter { it.price == "رایگان" }
            "جدید"   -> allCards.filter { it.isNew }
            else     -> allCards
        }
    }

    val filteredCards = remember(filteredCardsByTab, q) {
        if (q.isEmpty()) filteredCardsByTab
        else filteredCardsByTab.filter { c ->
            val t = c.title.lowercase()
            val d = c.description.lowercase()
            t.contains(q) || d.contains(q)
        }
    }


    val myCards     by vm.myCards.collectAsState()        // users/{uid}/my_flashcards
    val purchasedIds by vm.purchasedIds.collectAsState()  // users/{uid}/purchases
// دوره‌های اضافه‌شده به «کلمات من»
    val myWordCourses = remember(myCards) { myCards.filter { it.count > 0 } }

// دوره‌های خریداری‌شده (با id از purchases و جزئیات از allCards)
    val purchasedWordCourses = remember(allCards, purchasedIds) {
        allCards.filter { it.count > 0 && it.id in purchasedIds }
    }
// ادغام + حذف تکراری + جستجو
    val filteredWordCards = remember(myWordCourses, purchasedWordCourses, q) {
        val merged = (myWordCourses + purchasedWordCourses).distinctBy { it.id }
        if (q.isEmpty()) merged else merged.filter { c ->
            c.title.lowercase().contains(q) || c.description.lowercase().contains(q)
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f)
    ) {
        // --- کلمات من ---
        Text(
            text = " : کلمات من",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
                .wrapContentWidth(align = Alignment.End)
        )

// اگر خالی نیست → LazyRow کارت‌ها
        if (filteredWordCards.isNotEmpty()) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(filteredWordCards, key = { it.id }) { card ->
                        WordCard(card, navController)
                    }
                }
            }
// اگر خالی بود → پیام راهنما
        } else {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "هنوز دورهٔ آموزش فلش‌کارتی نداری.\nاز قسمت پایین دوره اضافه کن.",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280), // خاکستری ملایم
                        textAlign = TextAlign.Center
                    )
                }
            }
        }



        Text(
            text = "...موارد بیشتر",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 6.dp)
                .wrapContentWidth(align = Alignment.Start)
                .clickable { navController.navigate("my_flashcards") }
        )

        // --- دوره‌ها/فلش‌کارت‌ها ---
        Text(
            text = ": دوره ها",
            fontSize = (screenWidth * 0.035f).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .padding(top = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            val filters = listOf("جدید", "رایگان", "همه")
            filters.forEach { filter ->
                Spacer(modifier = Modifier.width(8.dp))
                FilterChips(
                    text = filter,
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredCards, key = { it.id }) { card ->
                Box {
                    flashCard(cards = card, navController = navController)
                    if (card.isNew) {
                        // برچسب فقط یک‌جا و به‌صورت Overlay
                        NewLabel(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = (-12).dp, y = 8.dp)
                                .zIndex(1f)
                        )
                    }
                }
            }
        }
    }
}

fun fetchCardsFromFirebase(onResult: (List<Cards>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("flashcards").get()
        .addOnSuccessListener { result ->
            val cardsList = result.mapNotNull { doc ->
                try {
                    Cards(
                        id = doc.id, // 🆕 اینو اضافه کن
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        count = doc.getLong("count")?.toInt() ?: 0,
                        price = doc.getString("price") ?: "نامشخص",
                        image = doc.getString("image") ?: "",
                        isNew = doc.getBoolean("isNew") ?: false
                    )
                } catch (e: Exception) {
                    null // اگر داده ناقص بود، ردش کن
                }
            }
            onResult(cardsList)
        }
        .addOnFailureListener {
            onResult(emptyList()) // یا هندل ارور
        }
}