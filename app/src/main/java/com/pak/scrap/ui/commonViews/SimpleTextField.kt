package com.pak.scrap.ui.commonViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pak.scrap.R
import com.pak.scrap.ui.theme.DarkBlue
import com.pak.scrap.ui.theme.regularFont
import com.pak.scrap.utils.Utils.isRtlLocale
import com.pak.scrap.utils.Utils.isValidPassword
import com.pak.scrap.utils.Utils.isValidPhone
import com.pak.scrap.utils.Utils.isValidText
import java.util.Locale

@Composable
fun MyTextFieldWithBorder(
    modifier: Modifier = Modifier,
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    placeholder: String,
    onValueChange: (String) -> Unit,
    imageId: Int,
    isEnable: Boolean = true,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var isValid by remember { mutableStateOf(true) }
    val textColor = if (isValid) Color.White else Color.Red
    isValid = isValidText(value).isEmpty()
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                modifier = Modifier.padding(top = 4.dp),
                fontFamily = regularFont,
                letterSpacing = 2.sp,
                fontSize = 14.sp,
                maxLines = 1,
                color = DarkBlue,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
            )
        }
        ,
        enabled = isEnable,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBlue, // Change focused border color
            unfocusedBorderColor = colorResource(id = R.color.text_grey), // Change unfocused border color
            focusedLabelColor = textColor,
            unfocusedLabelColor = if (value.isEmpty()) Color.White else textColor,
            focusedTextColor = DarkBlue,
            unfocusedTextColor = colorResource(id = R.color.text_grey)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Sentences // Capitalize first letter
        ),
        leadingIcon = if (isRtl) {
            {
                Image(
                    painter = painterResource(id = imageId), // Using the provided icon
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(DarkBlue)
                )
            }
        } else null,
        trailingIcon = if (!isRtl) {
            {
                Image(
                    painter = painterResource(id = imageId), // Using the provided icon
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(DarkBlue)
                )
            }
        } else null,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = regularFont,
            textAlign = if (isRtl) TextAlign.End else TextAlign.Start, // Align input text as well
            color = DarkBlue,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
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
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)

    var isFocused by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    val textColor = if (isValid) DarkBlue else Color.Red
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
        placeholder = {
            Text(
                placeholder,
                fontFamily = regularFont,
                letterSpacing = 2.sp,
                fontSize = 14.sp,
                maxLines = 1,
                color = DarkBlue,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBlue, // Change focused border color
            unfocusedBorderColor = colorResource(id = R.color.text_grey), // Change unfocused border color
            focusedLabelColor = textColor,
            unfocusedLabelColor = if (value.isEmpty()) DarkBlue else textColor,
            focusedTextColor = DarkBlue,
            unfocusedTextColor = DarkBlue
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        leadingIcon = if (isRtl) {
            {
                Image(
                    painter = passwordIcon, // Using the provided icon
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(DarkBlue),
                    modifier = Modifier
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            }
        } else null,
        trailingIcon = if (!isRtl) {
            {
                Image(
                    painter = passwordIcon, // Using the provided icon
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(DarkBlue),
                    modifier = Modifier
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            }
        } else null,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = regularFont,
            textAlign = if (isRtl) TextAlign.End else TextAlign.Start, // Align input text as well
            color = DarkBlue,
        ),
        shape = RoundedCornerShape(12.dp), // Set corner radius here
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
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
    modifier: Modifier
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    val prefix = "+92 "
    var isValid by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    val textColor = if (isValid) DarkBlue else Color.Red
    isValid = isValidPhone(value).isEmpty()
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 11) { // Restrict input length
                onValueChange(it)
            }
        },
        placeholder = {
            Text(
                text = placeholder, fontFamily = regularFont,
                letterSpacing = 2.sp,
                color = DarkBlue,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start
            )
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = regularFont,
            textAlign = if (isRtl) TextAlign.End else TextAlign.Start, // Align input text as well
            color = DarkBlue,
        ),
        leadingIcon = {
            if (isRtl)
                Image(
                    painter = painterResource(id = imageId),
                    colorFilter = ColorFilter.tint(DarkBlue),
                    contentDescription = null, // Add appropriate content description
                )
            else
                Text(
                    text = prefix,
                    color = DarkBlue,
                    fontFamily = regularFont,
                    letterSpacing = 2.sp,
                    fontSize = 14.sp,
                )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBlue, // Change focused border color
            unfocusedBorderColor = colorResource(id = R.color.text_grey), // Change unfocused border color
            focusedLabelColor = textColor,
            unfocusedLabelColor = if (value.isEmpty()) DarkBlue else textColor,
            focusedTextColor = DarkBlue,
            unfocusedTextColor = colorResource(id = R.color.text_grey)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        trailingIcon = {
            if (isRtl)
                Text(
                    text = prefix,
                    color = DarkBlue,
                    fontFamily = regularFont,
                    letterSpacing = 2.sp,
                    fontSize = 14.sp,
                )
            else
                Image(
                    painter = painterResource(id = imageId),
                    colorFilter = ColorFilter.tint(DarkBlue),
                    contentDescription = null, // Add appropriate content description
                )
        },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .background(Color.Transparent)
            .onFocusChanged {
                isFocused = it.isFocused
            },
    )
}

@Composable
fun CustomTextField(
    modifier: Modifier,
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    placeholder: String,
    onValueChange: (String) -> Unit,
    imageId: Int,
    enable: Boolean = true,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 12.dp),
    ) {
        // Placeholder Text
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontFamily = regularFont,
                fontSize = 14.sp,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = if (isRtl) 8.dp else 40.dp, start = if (isRtl) 40.dp else 8.dp),
                color = DarkBlue,
                letterSpacing = 2.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
        // TextField
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = regularFont,
                textAlign = if (isRtl) TextAlign.End else TextAlign.Start, // Align input text as well
                color = DarkBlue,
                letterSpacing = 2.sp,
            ),
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .padding(
                    start = if (isRtl) 30.dp else 8.dp,
                    top = 0.dp,
                    end = if (isRtl) 8.dp else 30.dp
                ), // Space for the leading icon
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = KeyboardCapitalization.Sentences // Capitalize first letter
            ),
            enabled = enable,
        )

        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier
                .align(if (isRtl) Alignment.TopStart else Alignment.TopEnd) // Align icon vertically center
                .padding(horizontal = 8.dp, vertical = 4.dp) // Consistent padding
                .size(16.dp),
            colorFilter = ColorFilter.tint(DarkBlue)
        )
    }
}