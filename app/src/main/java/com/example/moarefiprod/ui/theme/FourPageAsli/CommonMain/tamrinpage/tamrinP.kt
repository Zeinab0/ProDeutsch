package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.NewLabel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.observeFlashcardsRealtime

@Composable
fun courspage(
    onShowDialog: () -> Unit,                 // دیالوگ را والد مدیریت می‌کند
    navController: NavController,
    query: String = "",
    viewModel: CourseViewModel = viewModel()
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // فلش‌کارت‌های جدید (one-shot)
    // در Composable:
    var newCards by remember { mutableStateOf<List<Cards>>(emptyList()) }

    DisposableEffect(Unit) {
        val reg = observeFlashcardsRealtime(
            onlyNew = true,
            orderByField = null, // یا مثلاً "order"
            onUpdate = { list -> newCards = list },
            onError  = { /* لاگ/هندل خطا */ }
        )
        onDispose { reg.remove() }
    }

    // دوره‌های جدید از ویومدل
    val newCourses by viewModel.newCourses.collectAsState()

    // ----------- فیلتر بر اساس query -----------
    val q = remember(query) { query.trim().lowercase() }

    val filteredCourses = remember(newCourses, q) {
        if (q.isEmpty()) newCourses
        else newCourses.filter { c ->
            (c.title?.lowercase()?.contains(q) == true) ||
                    (c.description?.lowercase()?.contains(q) == true)
        }
    }

    val filteredCards = remember(newCards, q) {
        if (q.isEmpty()) newCards
        else newCards.filter { card ->
            card.title.lowercase().contains(q) ||
                    card.description.lowercase().contains(q)
        }
    }
    // -------------------------------------------

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            ExercisesSection(
                items = exerciseList,
                onItemClick = { selected ->
                    when (selected.title) {
                        "پادکست"       -> onShowDialog()                 // ✅ والد باز می‌کند
                        "گرامر"        -> navController.navigate("grammar_page")
                        "مهارت شنیداری"-> navController.navigate("hören_page")
                        "فیلم"         -> navController.navigate("MovieScreen")
                        "داستان"       -> navController.navigate("StoryScreen")
                        "آهنگ"         -> navController.navigate("MusicScreen")
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "جدیدترین ها",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = iranSans,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End)
                    .padding(bottom = 18.dp, end = 30.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = bottomInset + 96.dp)
            ) {
                // دوره‌های جدید (بعد از فیلتر)
                items(filteredCourses, key = { it.id }) { course ->
                    Box {
                        CourseCard(course = course, navController = navController)
                        if (course.isNew) {
                            NewLabel(
                                modifier = Modifier
                                    .align(Alignment.TopStart)  // AbsoluteAlignment لازم نیست
                                    .offset(x = (-12).dp, y = 8.dp)
                                    .zIndex(1f)
                            )
                        }
                    }
                }

                // فلش‌کارت‌های جدید (بعد از فیلتر)
                items(filteredCards, key = { it.id }) { card ->
                    Box {
                        flashCard(cards = card, navController = navController)
                        if (card.isNew) {
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

        // ⛔️ توجه: دیگه اینجا دیالوگ محلی نداریم؛
        // دیالوگ در HomeScreen رندر می‌شود (showDialog والد).
    }
}

//if (showDialog) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 0.5f)),
//        contentAlignment = Alignment.Center
//    ) {
//        UnavailableDialog(onDismiss = { showDialog = false })
//    }
//}