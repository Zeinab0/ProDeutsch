package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.courspage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.flashcardpage.flashcardpage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.Homepage.mainpage
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.tamrinpage
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.UnavailableDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) } // ✅ اینجا کنترل پاپ‌آپ

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var isDrawerOpen by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(selectedIndex) { index ->
                    selectedIndex = index
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = screenHeight * 0.1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                HeaderSection(
                    onMenuClick = { isDrawerOpen = true } // ✅ باز کردن دراور از اینجا
                )

                Spacer(modifier = Modifier.height(8.dp))
                SearchBar()

                // ⬇ صفحه‌ها
                when (selectedIndex) {
                    0 -> mainpage()
                    1 -> tamrinpage()
                    2 -> courspage(
                        onShowDialog = { showDialog = true },
                        navController = navController
                    )
                    3 -> flashcardpage(navController = navController)
                }
            }
        }

        // ⬇ لایه‌ی محو و دیالوگ در بالاترین سطح
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(enabled = true, onClick = {}) // جلوگیری از کلیک زیر
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                UnavailableDialog(onDismiss = { showDialog = false })
            }
        }

        // ✅ لایه‌ی طوسی محو و بستن با کلیک
        if (isDrawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { isDrawerOpen = false }
                    .zIndex(1f)
            )
        }

        // ✅ Drawer از سمت راست با انیمیشن
        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
            modifier = Modifier
                .fillMaxHeight()
                .width(260.dp)
                .align(Alignment.TopEnd)
                .zIndex(2f)
        ) {
            DrawerContent(onClose = { isDrawerOpen = false })
        }
    }
}
