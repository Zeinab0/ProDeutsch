package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import UserProfileViewModel
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun CompleteProfileDialog(
    onDismiss: () -> Unit,
    viewModel: UserProfileViewModel

) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val email = FirebaseAuth.getInstance().currentUser?.email ?: "no-email"

    var birthYear by remember { mutableStateOf("") }
    var birthMonth by remember { mutableStateOf("") }
    var birthDay by remember { mutableStateOf("") }

    var gender by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val firstNameHasError = showErrors && firstName.isBlank()
    val lastNameHasError = showErrors && lastName.isBlank()
    val birthDayHasError = showErrors && birthDay.isBlank()
    val birthMonthHasError = showErrors && birthMonth.isBlank()
    val birthYearHasError = showErrors && birthYear.isBlank()
    val genderHasError = showErrors && gender.isBlank()

    val years = (1350..1405).map { it.toString() }
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val days = (1..31).map { it.toString().padStart(2, '0') }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)), // پس‌زمینه تار
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 کادر سفید فرم
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 26.dp, end = 26.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 🔹 عکس پروفایل
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray) ,
                        contentAlignment = Alignment.Center
                    ) {
                        when (gender) {
                            "مرد" -> painterResource(id = R.drawable.profm)
                            "زن" -> painterResource(id = R.drawable.profw)
                            else -> null
                        }?.let { painter ->
                            Image(
                                painter = painter,
                                contentDescription = "عکس پروفایل",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "آیکن پیش‌فرض",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )

                    }

                    CustomField("نام", firstName, { firstName = it }, firstNameHasError)
                    CustomField("نام خانوادگی", lastName, { lastName = it }, lastNameHasError)

                    OutlinedTextField(
                        value = email,
                        onValueChange = {},
                        label = { Text("ایمیل", fontFamily = iranSans) },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("تاریخ تولد", fontFamily = iranSans)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DropdownField(
                            label = "روز",
                            value = birthDay,
                            onValueChange = { birthDay = it },
                            options = days,
                            hasError = birthDayHasError,
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        )
                        DropdownField(
                            label = "ماه",
                            value = birthMonth,
                            onValueChange = { birthMonth = it },
                            options = months,
                            hasError = birthMonthHasError,
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                        )
                        DropdownField(
                            label = "سال",
                            value = birthYear,
                            onValueChange = { birthYear = it },
                            options = years,
                            hasError = birthYearHasError,
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("جنسیت", fontFamily = iranSans)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "زن",
                            onClick = { gender = "زن" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4D869C))
                        )
                        Text("زن", modifier = Modifier.padding(end = 16.dp), fontFamily = iranSans)

                        RadioButton(
                            selected = gender == "مرد",
                            onClick = { gender = "مرد" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4D869C))
                        )
                        Text("مرد", fontFamily = iranSans)
                    }

                    if (genderHasError) {
                        Text("لطفاً جنسیت را انتخاب کنید", color = Color.Red, fontSize = 12.sp, fontFamily = iranSans)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 دکمه ذخیره
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .height(50.dp)
                                .background(
                                    color = Color(0xFF4D869C),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    showErrors = true

                                    val isValid = listOf(
                                        firstName, lastName, birthDay, birthMonth, birthYear, gender
                                    ).all { it.isNotBlank() }

                                    if (!isValid) return@clickable

                                    val scope = CoroutineScope(Dispatchers.IO)
                                    scope.launch {
                                        try {
                                            val uid = generateReadableUserId(firstName, lastName)
                                            val birthday = "$birthYear-$birthMonth-$birthDay"

                                            val profileImageResId = when (gender) {
                                                "مرد" -> "profm"
                                                "زن" -> "profw"
                                                else -> "default"
                                            }

                                            val userData = hashMapOf(
                                                "firstName" to firstName,
                                                "lastName" to lastName,
                                                "email" to email,
                                                "birthday" to birthday,
                                                "gender" to gender,
                                                "profileImage" to profileImageResId,
                                                "enrolledCourses" to listOf<String>()
                                            )

//                                            FirebaseFirestore.getInstance()
//                                                .collection("users")
//                                                .document(uid)
//                                                .set(userData)
//                                                .await() // 👈 بهتر از addOnSuccessListener

                                            FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .whereEqualTo("email", email)
                                                .get()
                                                .addOnSuccessListener { result ->
                                                    if (result.isEmpty) {
                                                        // اولین ورود، سند جدید بساز با uid سفارشی
                                                        FirebaseFirestore.getInstance()
                                                            .collection("users")
                                                            .document(uid) // <- استفاده از generateReadableUserId
                                                            .set(userData)
                                                            .addOnSuccessListener {
                                                                viewModel.loadUserData() // ⬅️ خیلی مهم
                                                                Toast.makeText(context, "اطلاعات ذخیره شد", Toast.LENGTH_SHORT).show()
                                                                onDismiss()
                                                            }
                                                    } else {
                                                        // اگر سندی وجود داشت، به‌روزرسانی کن
                                                        val doc = result.documents.first()
                                                        FirebaseFirestore.getInstance()
                                                            .collection("users")
                                                            .document(doc.id)
                                                            .set(userData)
                                                            .addOnSuccessListener {
                                                                viewModel.loadUserData()
                                                                Toast.makeText(context, "اطلاعات به‌روزرسانی شد", Toast.LENGTH_SHORT).show()
                                                                onDismiss()
                                                            }
                                                    }
                                                }

                                            withContext(Dispatchers.Main) {
                                                viewModel.loadUserData()
                                                Toast.makeText(context, "اطلاعات ذخیره شد", Toast.LENGTH_SHORT).show()
                                                onDismiss()
                                            }


                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                val errorMsg = e.localizedMessage ?: e.message ?: "خطای ناشناخته"
                                                Toast.makeText(context, "❌ $errorMsg", Toast.LENGTH_LONG).show()
                                            }
                                            Log.e("FirestoreError", "خطا در ذخیره", e)

                                        }

                                    }

                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ذخیره",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = iranSans,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun CustomField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hasError: Boolean,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, fontFamily = iranSans, textAlign = TextAlign.Right, fontSize = 14.sp,)
        },
        placeholder = {
            Text("لطفاً $label را وارد کنید", fontFamily = iranSans, color = Color.Gray,fontSize = 10.sp,)
        },
        isError = hasError,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF4D869C),
            unfocusedBorderColor = Color(0xFFB0BEC5),
            errorBorderColor = Color.Red,
            focusedLabelColor = Color(0xFF4D869C),
            unfocusedLabelColor = Color(0xFFB0BEC5),
            errorLabelColor = Color.Red,
            textColor = Color.Black,
            cursorColor = Color(0xFF4D869C)
        ),
        modifier = modifier,
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = iranSans,
            textAlign = TextAlign.Right,
            fontSize = 16.sp
        )
    )

    if (hasError) {
        Text(
            text = "لطفاً $label را وارد کنید",
            color = Color.Red,
            fontSize = 12.sp,
            fontFamily = iranSans,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}

@Composable
fun DropdownField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    hasError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val boxWidth = if (label == "سال") 140.dp else 100.dp

    Column(modifier = modifier.width(boxWidth)) {
        Text(
            text = label,
            fontFamily = iranSans,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = "انتخاب کنید",
                    fontFamily = iranSans,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            isError = hasError,
            modifier = Modifier
                .width(boxWidth)
                .height(52.dp)
                .clickable { expanded = true },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = iranSans,
                fontSize = 13.sp,
                textAlign = TextAlign.End, // مهم برای اعداد فارسی
                textDirection = TextDirection.Content
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF4D869C),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red,
                focusedLabelColor = Color(0xFF4D869C),
                unfocusedLabelColor = Color(0xFFB0BEC5),
                errorLabelColor = Color.Red,
                textColor = Color.Black,
                cursorColor = Color(0xFF4D869C)
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .width(boxWidth)
                .heightIn(max = 200.dp)
        ) {
            options.forEach { item ->
                DropdownMenuItem(onClick = {
                    onValueChange(item)
                    expanded = false
                }) {
                    Text(
                        text = item,
                        fontFamily = iranSans,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        if (hasError) {
            Text(
                text = "لطفاً $label را انتخاب کنید",
                color = Color.Red,
                fontSize = 11.sp,
                fontFamily = iranSans,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}




suspend fun generateReadableUserId(firstName: String, lastName: String): String {
    val base = "${firstName.lowercase()}.${lastName.lowercase()}"
    val db = FirebaseFirestore.getInstance().collection("users")
    var index = 1
    var finalId = "$base.${"%03d".format(index)}"

    while (true) {
        val snapshot = db.document(finalId).get().await()
        if (!snapshot.exists()) break
        index++
        finalId = "$base.${"%03d".format(index)}"
    }
    return finalId
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewCompleteProfileDialog() {
//    MaterialTheme {
//        CompleteProfileDialog(onDismiss = {})
//    }
//}
