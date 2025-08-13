package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.Cards
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.NewLabel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashCard
import com.example.moarefiprod.data.models.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseCard
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.fetchCardsFromFirebase
import java.util.Date
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun courspage(
    onShowDialog: () -> Unit,
    navController: NavController,
    viewModel: CourseViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // فلش‌کارت‌های جدید
    var newCards by remember { mutableStateOf<List<Cards>>(emptyList()) }
    LaunchedEffect(Unit) {
        fetchCardsFromFirebase { list ->
            newCards = list.filter { it.isNew }
        }
    }

    // دوره‌های جدید
    val newCourses by viewModel.newCourses.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ExercisesSection(
                items = exerciseList,
                onItemClick = { selected ->
                    when (selected.title) {
                        "پادکست" -> onShowDialog()
                        "گرامر" -> navController.navigate("grammar_page")
                        "مهارت شنیداری" -> navController.navigate("hören_page")
                        "فیلم" -> navController.navigate("MovieScreen")
                        "داستان" -> navController.navigate("StoryScreen")
                        "آهنگ" -> navController.navigate("MusicScreen")
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                    .navigationBarsPadding(),                  // ← محتوای صفحه از زیر نوار پایین میاد بیرون
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = bottomInset + 96.dp)   // ← فاصله‌ی مطمئن
            ) {
                // دوره‌های جدید
//                item {
//                    Text("دوره‌های جدید", fontFamily = iranSans, fontSize = 12.sp,
//                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp))
//                }
                items(newCourses, key = { it.id }) { course ->
                    Box {
                        CourseCard(course = course, navController = navController)
                        if (course.isNew) {
                            NewLabel()
                            // یا:
                            // NewLabel(Modifier.align(Alignment.TopEnd).zIndex(1f))
                        }
                    }
                }
                items(newCards, key = { it.id }) { card ->
                    Box {
                        flashCard(cards = card, navController = navController)
                        if (card.isNew) {
                            NewLabel()
                            // یا:
                            // NewLabel(Modifier.align(Alignment.TopEnd).zIndex(1f))
                        }
                    }
                }
            }
        }

        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                UnavailableDialog(onDismiss = { showDialog = false })
            }
        }
    }
}
