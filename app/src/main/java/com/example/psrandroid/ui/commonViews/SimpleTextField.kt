package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isValidPassword
import com.example.psrandroid.utils.Utils.isValidPhone
import com.example.psrandroid.utils.Utils.isValidText

@Composable
fun ProfileInputField(
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit,
    icon: ImageVector, placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
        },
        placeholder = {
            Text(text = placeholder, color = Color.White)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBlue, // Change focused border color
            unfocusedBorderColor = Color.Transparent, // Change unfocused border color
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Sentences
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(LightBlue, Color.White)
                ),
                shape = RoundedCornerShape(50) // Rounded corners for background
            )
            .padding(0.dp)
    )
}

@Composable
fun MyTextFieldWithBorder(
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    placeholder: String,
    onValueChange: (String) -> Unit,
    imageId: Int,
) {
    var isValid by remember { mutableStateOf(true) }
    val textColor = if (isValid) Color.White else Color.Red
    isValid = isValidText(value).isEmpty()
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = placeholder, fontFamily = mediumFont)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White, // Change focused border color
            unfocusedBorderColor = colorResource(id = R.color.text_grey), // Change unfocused border color
            focusedLabelColor = textColor,
            unfocusedLabelColor = if (value.isEmpty())Color.White else textColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        trailingIcon = {
            Image(
                painter = painterResource(
                    id = imageId
                ), // Using the provided icon
                contentDescription = null, // Add appropriate content description
            )
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    )
}

@Composable
fun PasswordTextFields(
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    val textColor = if (isValid) Color.White else Color.Red
    val passwordIcon =
        if (passwordVisible && isFocused) painterResource(id = R.drawable.s_eye_close)
        else if (!passwordVisible && isFocused) painterResource(id = R.drawable.s_eye_ic)
        else if (passwordVisible) painterResource(id = R.drawable.eye_close_ic)
        else painterResource(id = R.drawable.eye_ic)
    isValid = isValidPassword(value).isEmpty()

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                placeholder,
                fontFamily = mediumFont,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White, // Change focused border color
            unfocusedBorderColor = colorResource(id = R.color.text_grey), // Change unfocused border color
            focusedLabelColor = textColor,
            unfocusedLabelColor = if (value.isEmpty())Color.White else textColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        trailingIcon = {
            Image(
                painter = passwordIcon, // Using the provided icon
                contentDescription = null, // Add appropriate content description
                modifier = Modifier
                    .clickable {
                        passwordVisible = !passwordVisible
                    }
            )
        },
        shape = RoundedCornerShape(12.dp), // Set corner radius here
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
            )
            .onFocusChanged {
                isFocused = it.isFocused
            },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun PhoneTextField(
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    placeholder: String,
    onValueChange: (String) -> Unit,
    imageId: Int,
) {
    val prefix = "+92 "
    var isValid by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    val textColor = if (isValid) Color.White else Color.Red
    isValid = isValidPhone(value).isEmpty()
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = placeholder, fontFamily = mediumFont)
        },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
        leadingIcon = {
            Text(
                text = prefix,
                color = Color.White,
                fontFamily = mediumFont
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White, // Change focused border color
            unfocusedBorderColor = colorResource(id = R.color.text_grey), // Change unfocused border color
            focusedLabelColor = textColor,
            unfocusedLabelColor = if (value.isEmpty())Color.White else textColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        trailingIcon = {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null, // Add appropriate content description
            )
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .onFocusChanged {
                isFocused = it.isFocused
            },
    )
}
