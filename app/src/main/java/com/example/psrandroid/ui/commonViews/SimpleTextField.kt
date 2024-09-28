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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue

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
fun MyTextFieldWithoutBorder(
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    placeholder: String,
    onValueChange: (String) -> Unit,
    imageId: Int?,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = placeholder, color = Color.White)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent, // Change focused border color
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
        ),
        trailingIcon = {
            if (imageId!= null)
                Image(
                    painter = painterResource(id = imageId), // Using the provided icon
                    contentDescription = null, // Add appropriate content description
                )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
//            .fillMaxHeight()
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
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordIcon =
        if (passwordVisible && isFocused) painterResource(id = R.drawable.s_eye_close)
        else if (!passwordVisible && isFocused) painterResource(id = R.drawable.s_eye_ic)
        else if (passwordVisible) painterResource(id = R.drawable.eye_close_ic)
        else painterResource(id = R.drawable.eye_ic)

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                placeholder,
//                fontFamily = regularFont,
                color = Color.White
            )
        },
        textStyle = TextStyle(
            color = Color.White // Use the appropriate color based on focus
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent, // Change focused border color
            unfocusedBorderColor = Color.Transparent, // Change unfocused border color
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        leadingIcon = {
            Image(
                painter = passwordIcon, // Using the provided icon
                contentDescription = null, // Add appropriate content description
                modifier = Modifier
                    .clickable {
                        passwordVisible = !passwordVisible
                    }
            )
        },
//        shape = RectangleShape, // Set corner radius here
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = Color.Transparent,
//                shape = RectangleShape
            )
//            .border(width = 2.dp, shape = RectangleShape, color = Color.White)
            .onFocusChanged {
//                isFocused = it.isFocused
            },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}