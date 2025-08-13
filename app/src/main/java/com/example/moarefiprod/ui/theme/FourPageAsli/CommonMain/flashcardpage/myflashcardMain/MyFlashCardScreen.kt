package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.myflashcardMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Word
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.WordStatus
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

@Composable
fun MyFlashCardScreen(
    navController: NavController,
    vm: com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.viewmodel.FlashcardViewModel = viewModel()
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val myCards by vm.myCards.collectAsState()

    // همهٔ کلماتِ تمام کارت‌های کاربر
    var allWords by remember { mutableStateOf<List<Word>>(emptyList()) }

    // وقتی کارت‌ها لود/تغییر کردند، همهٔ کلمات مرتبط را از فایراستور بگیر
    LaunchedEffect(myCards) {
        val cardIds = myCards.map { it.id }
        fetchAllWordsForCards(cardIds) { loaded ->
            allWords = loaded
        }
    }

    val total       = allWords.size
    val correctCnt  = allWords.count { it.status == WordStatus.CORRECT }
    val wrongCnt    = allWords.count { it.status == WordStatus.WRONG }
    val idkCnt      = allWords.count { it.status == WordStatus.IDK }
    val newCnt      = allWords.count { it.status == WordStatus.NEW }

    var selectedStatuses by remember { mutableStateOf(setOf<WordStatus>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(
                        start = screenWidth * 0.03f,
                        top = screenHeight * 0.05f
                    )
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
        Text(
            text = "وضعیت",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
        )

        ChartPager(
            correct = correctCnt,
            wrong   = wrongCnt,
            idk     = idkCnt,
            new     = newCnt,
            total   = total,
            selected = selectedStatuses,
            onStatusToggle = { s ->
                selectedStatuses = if (s in selectedStatuses) selectedStatuses - s else selectedStatuses + s
            },
            weeklyData = listOf(
                "جمعه" to 25, "پنج شنبه" to 10, "چهارشنبه" to 18,
                "سه‌شنبه" to 32, "دوشنبه" to 22, "یکشنبه" to 12, "شنبه" to 5
            )
        )

        Text(
            text = "کلمات من",
            fontSize = (screenWidth * 0.04f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(myCards, key = { it.id }) { card ->
                flashCard(
                    cards = card,
                    navController = navController,
                    viewModel = vm,
                    manageMode = true
                )
            }
        }
    }
}

fun fetchAllWordsForCards(
    cardIds: List<String>,
    onResult: (List<Word>) -> Unit
) {
    if (cardIds.isEmpty()) { onResult(emptyList()); return }

    val db = FirebaseFirestore.getInstance()

    // تمام ساب‌کالکشن‌های "words" را می‌خوانیم
    db.collectionGroup("words")
        .get()
        .addOnSuccessListener { result ->
            val idSet = cardIds.toSet()

            val words = result.mapNotNull { doc: QueryDocumentSnapshot ->
                try {
                    // id کارتِ والد این کلمه:
                    val parentCardId = doc.reference.parent.parent?.id

                    if (parentCardId != null && parentCardId in idSet) {
                        Word(
                            id = doc.id,
                            text = doc.getString("text") ?: "",
                            translation = doc.getString("translation") ?: "",
                            status = when ((doc.getString("status") ?: "").uppercase()) {
                                "CORRECT" -> WordStatus.CORRECT
                                "WRONG"   -> WordStatus.WRONG
                                "IDK"     -> WordStatus.IDK
                                else      -> WordStatus.NEW
                            }
                        )
                    } else null
                } catch (_: Exception) { null }
            }

            onResult(words)
        }
        .addOnFailureListener { onResult(emptyList()) }
}

