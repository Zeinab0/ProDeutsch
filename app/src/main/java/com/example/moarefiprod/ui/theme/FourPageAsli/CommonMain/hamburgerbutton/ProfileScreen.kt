package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.hamburgerbutton

import UserProfileViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.CustomField
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.DropdownField

@Composable
fun ProfileScreen(navController: NavController,userViewModel: UserProfileViewModel) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val cornerRadius = 8.dp
    val context = LocalContext.current

    val firstName by userViewModel.firstName
    val lastName by userViewModel.lastName
    val birthDay by userViewModel.birthDay
    val birthMonth by userViewModel.birthMonth
    val birthYear by userViewModel.birthYear
    val gender by userViewModel.gender
    val profileImage by userViewModel.profileImage
    val email by userViewModel.email

    var showErrors by remember { mutableStateOf(false) }
    val years = (1350..1405).map { it.toString() }
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val days = (1..31).map { it.toString().padStart(2, '0') }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backprofedit),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(screenHeight * 0.4f)
                .fillMaxWidth()
        )

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

        Box(
            modifier = Modifier
                .size(screenWidth * 0.26f)
                .align(Alignment.TopCenter)
                .offset(y = screenHeight * 0.14f)
                .zIndex(1f)
                .border(
                    width = screenWidth * 0.004f,
                    color = Color(0xff4D869C),
                    shape = RoundedCornerShape(screenWidth * 0.03f)
                )
                .clip(RoundedCornerShape(screenWidth * 0.03f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(screenWidth * 0.22f)
                    .clip(RoundedCornerShape(screenWidth * 0.03f))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                when (profileImage) {
                    "profm" -> Image(
                        painter = painterResource(id = R.drawable.profm),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    "profw" -> Image(
                        painter = painterResource(id = R.drawable.profw),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    else -> Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.90f)
                .offset(y = screenHeight * 0.20f)
                .shadow(22.dp, RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
                    .padding(top = screenHeight * 0.10f, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomField("نام", firstName, { userViewModel.setFirstName(it) }, showErrors && firstName.isBlank())
                CustomField("نام خانوادگی", lastName, { userViewModel.setLastName(it) }, showErrors && lastName.isBlank())

                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("ایمیل", fontFamily = iranSans) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text("تاریخ تولد", fontFamily = iranSans, fontSize = 12.sp, color = Color(0xFF818181), textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DropdownField("روز", birthDay, { userViewModel.setBirthDay(it) }, days, showErrors && birthDay.isBlank(), Modifier.weight(1f).padding(end = 4.dp))
                    DropdownField("ماه", birthMonth, { userViewModel.setBirthMonth(it) }, months, showErrors && birthMonth.isBlank(), Modifier.weight(1f).padding(horizontal = 4.dp))
                    DropdownField("سال", birthYear, { userViewModel.setBirthYear(it) }, years, showErrors && birthYear.isBlank(), Modifier.weight(1f).padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("جنسیت", fontFamily = iranSans, fontSize = 12.sp, color = Color(0xFF818181), textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = gender == "زن", onClick = { userViewModel.setGender("زن") }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4D869C)))
                    Text("زن", modifier = Modifier.padding(end = 16.dp), fontFamily = iranSans)
                    RadioButton(selected = gender == "مرد", onClick = { userViewModel.setGender("مرد") }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4D869C)))
                    Text("مرد", fontFamily = iranSans)
                }

                if (showErrors && gender.isBlank()) {
                    Text("لطفاً جنسیت را انتخاب کنید", color = Color.Red, fontSize = 12.sp, fontFamily = iranSans)
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Box(
                        modifier = Modifier.width(129.dp).height(53.dp).background(Color(0xFF5B8EA3), RoundedCornerShape(cornerRadius)).clickable {
                            showErrors = true
                            val isValid = listOf(firstName, lastName, birthDay, birthMonth, birthYear, gender).all { it.isNotBlank() }
                            if (!isValid) return@clickable
                            userViewModel.saveProfile(
                                onSuccess = { Toast.makeText(context, "اطلاعات ذخیره شد", Toast.LENGTH_SHORT).show()},
                                onFailure = {
                                    Toast.makeText(context, "خطا در ذخیره اطلاعات", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ذخیره", color = Color.White, fontFamily = iranSans)
                    }

                    Box(
                        modifier = Modifier
                            .width(129.dp)
                            .height(53.dp)
                            .background(Color(0xFFD1F1EF), RoundedCornerShape(cornerRadius))
                            .clickable {
                                navController.popBackStack()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("انصراف", color = Color.Black, fontFamily = iranSans)
                    }
                }
            }
        }
    }
}