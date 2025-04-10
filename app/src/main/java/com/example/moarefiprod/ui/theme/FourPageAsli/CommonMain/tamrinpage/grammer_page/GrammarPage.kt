package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow

@Composable
fun GrammarPage(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val viewModel: GrammarViewModel = viewModel()
    val grammarTopics by viewModel.topics.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedTopic by remember { mutableStateOf<GrammarTopic?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f)
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
            text = "گرامر",
            fontSize = (screenWidth * 0.045f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .padding(horizontal = 30.dp)
                .padding(top = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "چه مبحثی رو دوسداری تمرین کنیم؟",
                fontSize = (screenWidth * 0.04f).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = iranSans,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.grammer),
                contentDescription = "Grammer",
                tint = Color.Black,
                modifier = Modifier.size(screenWidth * 0.08f)
            )
        }

        Spacer(modifier = Modifier.height(0.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 24.dp)
            ) {
                item { Spacer(modifier = Modifier.height(1.dp)) }

                items(grammarTopics) { topic ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(53.dp)
                            .shadow(12.dp, RoundedCornerShape(12.dp))
                            .clickable {
                                selectedTopic = topic
                                showDialog = true
                            }
                    ) {
                        Text(
                            text = topic.title,
                            fontSize = 16.sp,
                            fontFamily = iranSans,
                            color = Color(0xFF4D869C),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 18.dp, top = 15.dp),
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }
        }

    }

    if (showDialog && selectedTopic != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "شروع کنیم؟",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = iranSans,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "سوالات به صورت تصادفی می‌باشند و هر آزمون شامل ۱۰ سوال است.",
                        fontSize = 12.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(45.dp)
                                .clickable { showDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("خروج", color = Color.Red, fontFamily = iranSans)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .evenShadow(radius = 25f, cornerRadius = 20f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF7AB2B2))
                                .height(45.dp)
                                .clickable {
                                    // TODO: رفتن به صفحه بازی با topicId → selectedTopic?.id
                                    showDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("شروع", color = Color.White, fontFamily = iranSans)
                        }
                    }
                }
            }
        }
    }
}