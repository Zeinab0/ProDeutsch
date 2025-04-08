import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moarefiprod.R
import com.example.moarefiprod.iranSans

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var searchText by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = {
            Text(
                text = ":جستجو دوره",
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
            keyboardType = KeyboardType.Text,
            autoCorrectEnabled  = true,
            imeAction = ImeAction.Done // ✅ دکمه "Done"
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() } // ✅ بستن کیبورد بعد از Done
        ),
        shape = RoundedCornerShape(screenWidth * 0.04f),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF4D869C),
            unfocusedBorderColor = Color(0xFF90CECE),
            cursorColor = Color(0xFF4D869C),
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                tint = Color(0xFF90CECE),
                contentDescription = "search Icon",
                modifier = Modifier.size(screenWidth * 0.055f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.065f)
            .padding(horizontal = screenWidth * 0.06f)
            .background(Color(0xffF1FFFF), RoundedCornerShape(screenWidth * 0.04f))
    )
}
