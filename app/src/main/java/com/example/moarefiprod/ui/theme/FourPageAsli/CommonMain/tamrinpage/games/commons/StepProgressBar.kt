//package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage.games.commons
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.unit.dp
//
////@Composable
////fun StepProgressBar(
////    currentStep: Int,
////    totalSteps: Int,
////    modifier: Modifier = Modifier
////) {
////    val spacing = 6.dp
////    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
////    val barWidth = remember(totalSteps) {
////        (screenWidth - spacing * (totalSteps - 1)) / totalSteps
////    }
////
////    Row(
////        modifier = modifier
////            .fillMaxWidth()
////            .padding(horizontal = 24.dp),
////        horizontalArrangement = Arrangement.spacedBy(spacing)
////    ) {
////        repeat(totalSteps) { index ->
////            Box(
////                modifier = Modifier
////                    .width(barWidth)
////                    .height(6.dp)
////                    .clip(RoundedCornerShape(5.dp))
////                    .background(
////                        if (index <= currentStep)
////                            Color(0xFF4D869C) // ✅ رنگ فعال
////                        else
////                            Color(0xFFE0F2F1) // ⬅️ رنگ خنثی
////                    )
////            )
////        }
////    }
////}
////
//@Composable
//fun StepProgressBar(
//    currentStep: Int,
//    totalSteps: Int,
//    modifier: Modifier = Modifier
//) {
//    val spacing = 6.dp
//
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(spacing)
//    ) {
//        repeat(totalSteps) { index ->
//            Box(
//                modifier = Modifier
//                    .weight(1f) // 👈 تقسیم مساوی بین همه
//                    .height(6.dp)
//                    .clip(RoundedCornerShape(5.dp))
//                    .background(
//                        if (index <= currentStep)
//                            Color(0xFF4D869C)
//                        else
//                            Color(0xFFE0F2F1)
//                    )
//            )
//        }
//    }
//}
