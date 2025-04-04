package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.tamrinpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@Composable
fun UnavailableDialog(onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(52.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "متأسفیم",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "این دوره هنوز موجود نیست اما تیم ما در تلاش است تا هر چه سریع‌تر این قابلیت را در اختیار شما عزیزان قرار دهند. متشکریم از صبر و شکیبایی شما",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = iranSans,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7AB2B2),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(46.dp)
                ) {
                    Text(
                        text = "تایید",
                        color = Color.White,
                        fontSize = (screenWidth * 0.035f).value.sp,
                        fontFamily = iranSans,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
