package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Dars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast

@Composable
fun Jozve(
    courseId: String,
    lessonId: String,
    contentId: String,
    navController: NavController,
    viewModel: DarsViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current

    val handout = viewModel.handout.collectAsState().value

    LaunchedEffect(contentId) {
        viewModel.loadHandout(courseId, lessonId, contentId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /** دکمه برگشت **/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = screenWidth * 0.03f, top = screenHeight * 0.05f),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.backbtn),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(screenWidth * 0.09f)
                )
            }
        }

        if (handout != null) {
            /** هدر و عنوان **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = screenHeight * 0.15f)
                    .align(Alignment.TopCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = handout.title,
                    fontSize = (screenWidth * 0.04f).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = iranSans,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.document),
                    contentDescription = "document",
                    tint = Color(0xFF4D869C),
                    modifier = Modifier.size(screenWidth * 0.08f)
                )
            }

            /** نمایش مشخصات فایل **/
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                Box(
                    modifier = Modifier
                        .width(screenWidth * 0.8f)
                        .height(screenHeight * 0.07f)
                        .shadow(6.dp, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = handout.fileName,
                                fontFamily = iranSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = handout.size,
                                fontFamily = iranSans,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.08f))

                /** دکمه‌های دانلود و پرینت **/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    /** پرینت: باز کردن برای مشاهده / پرینت**/
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(Uri.parse(handout?.url ?: ""), "application/pdf")
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                                Toast.makeText(context, "📄 آماده برای پرینت یا مشاهده", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "❌ فایل قابل نمایش نیست یا مشکلی در اجرا پیش آمده.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .width(129.dp)
                            .height(53.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "پرینت",
                            fontFamily = iranSans,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    /** دانلود: فقط باز کردن در مرورگر برای دانلود **/
                    Button(
                        onClick = {
                            try {
                                val request = DownloadManager.Request(Uri.parse(handout?.url ?: ""))
                                    .setTitle(handout?.fileName ?: "جزوه")
                                    .setDescription("در حال دانلود...")
                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    .setAllowedOverMetered(true)
                                    .setAllowedOverRoaming(true)
                                    .setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS,
                                        handout?.fileName ?: "jozve.pdf"
                                    )

                                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                downloadManager.enqueue(request)

                                Toast.makeText(context, "✅ فایل با موفقیت دانلود شد", Toast.LENGTH_SHORT).show()

                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "❌ خطایی در دانلود فایل رخ داده است. لطفاً بعداً امتحان کنید.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .width(129.dp)
                            .height(53.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "دانلود",
                            fontFamily = iranSans,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun JozvePreview() {
//    Jozve()
//}