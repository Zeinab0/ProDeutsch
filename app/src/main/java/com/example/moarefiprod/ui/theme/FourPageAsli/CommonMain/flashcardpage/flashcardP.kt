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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.NewLabel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun flashcardpage(navController: NavController){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var selectedFilter by remember { mutableStateOf("همه") } // ✅ مقدار اولیه


    var allCards by remember { mutableStateOf<List<Cards>>(emptyList()) }

    LaunchedEffect(Unit) {
        fetchCardsFromFirebase {
            allCards = it
        }
    }

// جدا کردن لیست مورد نیاز برای WordCard از Cards
    val wordCards = allCards.filter { it.count > 0 } // یا هر شرطی که مد نظرته


    var cards by remember { mutableStateOf<List<Cards>>(emptyList()) }

    LaunchedEffect(Unit) {
        fetchCardsFromFirebase {
            cards = it
            println("🔥 ${it.size} کارت لود شد")
            it.forEach { card ->
                println("📦 ${card.title} | ${card.image}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = screenWidth * 0.05f) // ✅ پدینگ کلی برای هماهنگی بهتر
    ){

        Text(
            text = ":کلمات من",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
                .wrapContentWidth(align = Alignment.End)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(wordCards) { card ->
                WordCard(card)
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
                .clickable {
                    navController.navigate("my_flashcards")
                }
        )

        // ✅ متن راست‌چین
        Text(
            text = ":دوره‌ها",
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
        val filteredCards = when (selectedFilter) {
            "رایگان" -> cards.filter { it.price == "رایگان" }
            "جدید" -> cards.filter { it.isNew }
            else -> cards
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredCards) { cards ->
                Box {
                    flashCard(cards = cards, navController = navController)
                    if (cards.isNew) {
                        NewLabel()
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