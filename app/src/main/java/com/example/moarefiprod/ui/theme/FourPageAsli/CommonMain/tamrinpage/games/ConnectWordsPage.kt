package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons.StepProgressBarWithExit
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.ConnectResult
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.grammer_page.game.GrammerGameViewModel
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören.evenShadow
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.hören_page.AudioProgressVisualizerr
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay


@Composable
fun ConnectWordsPage(
    navController: NavController,
    courseId: String,
    lessonId: String,
    contentId: String,
    gameId: String,
    gameIndex: Int,
    totalGames: Int,
    viewModel: GrammerGameViewModel,
    onGameFinished: (Boolean, String?) -> Unit
) {
    val gameState = viewModel.connectWordsGameState.collectAsState().value

    if (gameState == null) return

    val words = gameState.words
    val audioUrls = gameState.audioUrls
    val correctPairs = gameState.correctPairs

    val pathType = if (lessonId.isNotEmpty() && contentId.isNotEmpty()) {
        GrammerGameViewModel.GamePathType.COURSE
    } else {
        GrammerGameViewModel.GamePathType.GRAMMAR_TOPIC
    }
    val audioMap = remember(words, audioUrls) {
        words.mapIndexed { index, word ->
            val start = index * 2
            val pair = audioUrls.slice(start until (start + 2).coerceAtMost(audioUrls.size))
            word to pair
        }.toMap()
    }
    LaunchedEffect(gameId) {
        viewModel.loadConnectWordsGame(pathType, courseId, lessonId, contentId, gameId)
    }


    val selectedAudios = remember { mutableStateMapOf<String, String>() }
    var playingUrl by remember { mutableStateOf<String?>(null) }
    var showResultBox by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var resultStatus by remember { mutableStateOf<Map<String, Boolean>?>(null) }
    var resultList by remember { mutableStateOf<List<ConnectResult>>(emptyList()) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize()) {

        var showExitDialog by remember { mutableStateOf(false) }

        val returnRoute = if (pathType == GrammerGameViewModel.GamePathType.COURSE) {
            "darsDetails/$courseId/$lessonId"
        } else {
            "grammar_page"
        }

        StepProgressBarWithExit(
            navController = navController,
            currentStep = gameIndex,
            totalSteps = totalGames,
            returnRoute = returnRoute,
            onRequestExit = { showExitDialog = true },
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.1f, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            StepProgressBarWithExit(currentStep = 3 , totalSteps = 6)
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = gameState.questionText ?: "", // 👈 از Firestore خونده میشه
                fontFamily = iranSans,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Right,
                style = TextStyle(textDirection = TextDirection.Rtl),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                audioMap.forEach { (word, audioList) -> // ← audioList به‌جای urls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WordBox(word = word, isSelected = false, onClick = {})

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            audioList.forEach { audioUrl ->
                                AudioBoxFromUrl(
                                    audioUrl = audioUrl,
                                    isPlayingNow = playingUrl == audioUrl,
                                    isSelected = selectedAudios[word] == audioUrl,
                                    result = if (showResultBox && selectedAudios[word] == audioUrl) {
                                        resultStatus?.get(word)
                                    } else null,
                                    onPlayClick = { playingUrl = audioUrl },
                                    onSelect = {
                                        if (resultStatus == null) {
                                            if (selectedAudios[word] == audioUrl)
                                                selectedAudios.remove(word)
                                            else
                                                selectedAudios[word] = audioUrl
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

        }

        val allSelected = selectedAudios.keys.containsAll(words)

        val userIsCorrect = selectedAudios == correctPairs
        val selectedWordAndUrl: Map<String, String> = selectedAudios.toMap()
        val newResultStatus = words.associateWith { word ->
            selectedAudios[word] == correctPairs[word]
        }

        Button(
            onClick = {
                showResultBox = true
                isCorrect = userIsCorrect
                resultStatus = newResultStatus

                resultList = words.map { word ->
                    val selectedAudio = selectedAudios[word] ?: ""
                    val correctAudio = correctPairs[word] ?: ""
                    val userSentence = gameState.audioTexts[selectedAudio] ?: "---"
                    val correctSentence = gameState.audioTexts[correctAudio] ?: "---"
                    val isCorrect = selectedAudio == correctAudio
                    val translation = gameState.wordTranslations[word] ?: ""

                    Log.d("TranslationCheck", "Translation for $word: $translation")

                    ConnectResult(
                        word = word,
                        selectedAudioUrl = selectedAudio,
                        correctAudioUrl = correctAudio,
                        userSentence = userSentence,
                        correctSentence = correctSentence,
                        isCorrect = isCorrect,
                        translation = translation
                    )
                }
            },
            //enabled = allSelected,
            enabled = true,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 180.dp)
                .width(screenWidth * 0.20f)
                .height(40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (allSelected) Color(0xFF4D869C) else Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = "تأیید", fontFamily = iranSans, fontWeight = FontWeight.Bold)
        }

        if (showResultBox) {
            ConnectWordsResultBox(
                resultList = resultList,
                isLastGame = gameIndex == totalGames - 1,
                navController = navController,
                returnRoute = returnRoute,
                onNext = {
                    showResultBox = false
                    selectedAudios.clear()
                    resultStatus = null
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp)
            )
        }
    }
}

@Composable
fun AudioBoxFromUrl(
    audioUrl: String,
    isPlayingNow: Boolean,
    isSelected: Boolean,
    result: Boolean?,
    onPlayClick: () -> Unit,
    onSelect: () -> Unit
) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var remainingPlays by remember { mutableStateOf(3) }
    val scope = rememberCoroutineScope()


    DisposableEffect(audioUrl) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.parse(audioUrl))
            prepareAsync()
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    val boxBgColor = when (result) {
        true -> Color(0xFF14CB00)
        false -> Color(0xFFFF3B3B)
        null -> if (isSelected) Color(0xFF4D869C) else Color(0xFFEFEFEF)
    }

    val iconTint = if (result != null || isSelected) Color.White else Color(0xFF4D869C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(boxBgColor)
            .clickable(enabled = result == null) { onSelect() }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (remainingPlays > 0 && mediaPlayer != null) {
                    onPlayClick()
                    scope.launch {
                        mediaPlayer?.let { player ->
                            if (!player.isPlaying) {
                                player.start()
                                isPlaying = true

                                while (player.isPlaying) {
                                    progress = player.currentPosition.toFloat() / player.duration.toFloat()
                                    delay(100L)
                                }

                                progress = 1f
                                remainingPlays--
                                isPlaying = false
                            } else {
                                player.pause()
                                isPlaying = false
                            }
                        }
                    }
                }
            },
            enabled = remainingPlays > 0 && result == null
        ) {
            Icon(
                painter = painterResource(id = R.drawable.volume),
                contentDescription = "Play Audio",
                tint = iconTint,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AudioProgressVisualizerr(
            isPlaying = isPlaying,
            isDisabled = remainingPlays == 0,
            progress = progress
        )
    }
}



// کامپوزابل برای نمایش یک کلمه
@Composable
fun WordBox(word: String, isSelected: Boolean, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(10.dp))
            .evenShadow(radius = 25f, cornerRadius = 20f)
            //.shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color(0xFFCDE8E5))
            .clickable { onClick(word) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = word,
            fontFamily = iranSans,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ConnectWordsResultBox(
    resultList: List<ConnectResult>,
    isLastGame: Boolean,
    navController: NavController,
    returnRoute: String,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .background(color = Color(0xFF90CECE), shape = RoundedCornerShape(25.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            resultList.forEachIndexed { index, result ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // =================== جمله آلمانی + وضعیت چکمارک (سمت چپ)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = if (result.isCorrect) R.drawable.tik else R.drawable.cross),
                            contentDescription = null,
                            tint = if (result.isCorrect) Color(0xFF14CB00) else Color(0xFFFF3B3B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${result.userSentence}",
                            fontFamily = iranSans,
                            fontSize = 14.sp,
                            color = Color.Black,
                        )
                    }

                    // ==================== ترجمه فارسی کلمه (سمت راست)
                    Text(
                        text = result.translation,
                        fontFamily = iranSans,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (!result.isCorrect) {
                    // جمله درست (در صورت غلط بودن)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                       // Spacer(modifier = Modifier.width(2.dp)) // ایجاد فاصله از چپ

                        Icon(
                            painter = painterResource(id = R.drawable.tik),
                            contentDescription = null,
                            tint = Color(0xFF14CB00),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = result.correctSentence,
                            fontFamily = iranSans,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // دکمه پایین سمت چپ
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .offset(y = (-14).dp)
                    .width(90.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF4D869C))
                    .clickable {
                        if (isLastGame) {
                            navController.navigate(returnRoute) {
                                popUpTo("home") { inclusive = false }
                            }
                        } else {
                            onNext()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isLastGame) "تمام" else "بریم بعدی",
                        fontFamily = iranSans,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    if (!isLastGame) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.nextbtn),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}