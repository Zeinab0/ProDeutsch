package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalConfiguration

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    var selectedIndex by remember { mutableStateOf(0) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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
                .padding(bottom = screenHeight * 0.1f) // فضای خالی بالا برای BottomNav
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HeaderSection()
            Spacer(modifier = Modifier.height(8.dp))
            var searchText by remember { mutableStateOf("") }

            SearchBar()

            // اینجا صفحه‌ها تغییر می‌کنن:
            when (selectedIndex) {
                0 -> mainpage()
                1 -> tamrinpage()
                2 -> courspage()
                3 -> flashcardpage()
            }
        }
    }
}
