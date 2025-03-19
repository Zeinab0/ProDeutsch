import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.R

@Composable
fun RecoverySuccess(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.2f))

        // ✅ ویدیوی تکرارشونده
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(Uri.parse("android.resource://${context.packageName}/${R.raw.sacsess}"))
                    setOnPreparedListener { it.isLooping = true } // 🔄 تنظیم تکرار
                    start() // ▶ شروع پخش خودکار
                }
            },
            modifier = Modifier
                .width(screenWidth * 0.8f)
                .height(screenHeight * 0.3f)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.15f))

        Text(
            text = "تبریک! رمز شما با موفقیت تغییر یافت",
            fontSize = (screenWidth * 0.05f).value.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = iranSans,
            color = Color.Black,
            modifier = Modifier.padding(top = screenHeight * 0.02f)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.1f))
        Button(
            onClick = { navController.navigate("login") }, // ✅ بازگشت به صفحه ورود
            modifier = Modifier
                .width(screenWidth * 0.73f)
                .height(screenHeight * 0.07f),
            colors = ButtonDefaults.buttonColors(Color(0xFF4D869C)),
            shape = RoundedCornerShape(screenWidth * 0.02f)
        ) {
            Text(
                text = "بازگشت به ورود",
                fontSize = (screenWidth * 0.045f).value.sp,
                fontFamily = iranSans,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.09f))

        Text(
            text = "ProDeutsch",
            fontSize = (screenWidth * 0.035f).value.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4D869C)
        )
    }
}
