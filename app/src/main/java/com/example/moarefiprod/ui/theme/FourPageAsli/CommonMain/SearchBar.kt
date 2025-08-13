package com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moarefiprod.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.iranSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    onSearch: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                fontSize = (screenWidth * 0.04f).value.sp,
                color = Color(0xFF90CECE),
                fontFamily = iranSans,
                fontWeight = FontWeight.ExtraLight,
            )
        },
        textStyle = TextStyle(
            fontSize = (screenWidth * 0.045f).value.sp,
            fontFamily = iranSans,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4D869C),
            textAlign = TextAlign.Right,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        shape = RoundedCornerShape(screenWidth * 0.04f),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF4D869C),
            unfocusedBorderColor = Color(0xFF90CECE),
            cursorColor = Color(0xFF4D869C),
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                tint = Color(0xFF90CECE),
                contentDescription = "search Icon",
                modifier = Modifier
                    .size(screenWidth * 0.055f)
                    .padding(end = 6.dp)
                    .clickable { onSearch() } // کلیک روی آیکون
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.065f)
            .padding(horizontal = screenWidth * 0.06f)
            .background(Color(0xffF1FFFF), RoundedCornerShape(screenWidth * 0.04f))
    )
}
