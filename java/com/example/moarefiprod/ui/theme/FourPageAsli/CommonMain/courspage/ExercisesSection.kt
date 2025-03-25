package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

// مدل داده تمرین
data class ExerciseItem(val title: String, val icon: Int, val isSelected: Boolean = false)

val exerciseList = listOf(
    ExerciseItem("فیلم", R.drawable.movie),
    ExerciseItem("مهارت شنیداری", R.drawable.horen),
    ExerciseItem("گرامر", R.drawable.grammer),
    ExerciseItem("پادکست", R.drawable.podcast, isSelected = true),
    ExerciseItem("آهنگ", R.drawable.music),
    ExerciseItem("داستان", R.drawable.story)
)

@Composable
fun ExercisesSection(
    items: List<ExerciseItem>,
    onItemClick: (ExerciseItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "تمرینات",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = iranSans,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .padding(bottom = 18.dp, end = 30.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items.chunked(3).forEachIndexed { rowIndex, row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    row.forEachIndexed { index, item ->
                        ExerciseCard(
                            item = item,
                            index = rowIndex * 3 + index,
                            onClick = {
                                onItemClick(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    item: ExerciseItem,
    index: Int,
    onClick: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = screenWidth * 0.25f

    val isEven = index % 2 == 0
    val backgroundColor = if (isEven) Color(0xFFCDE8E5) else Color(0xFF90CECE)
    val contentColor = if (item.isSelected || !isEven) Color.White else Color.Black

    Box(
        modifier = Modifier
            .width(cardWidth)
            .aspectRatio(1f)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color(0xFF4D869C)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth * 0.15f)
            )
//            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}
