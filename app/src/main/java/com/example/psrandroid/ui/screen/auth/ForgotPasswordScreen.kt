package com.example.psrandroid.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.commonViews.LoadingDialog
import com.example.psrandroid.ui.commonViews.MyTextFieldWithBorder
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isValidPhone
import es.dmoral.toasty.Toasty

@Composable
fun ForgotPasswordScreen(navController: NavController, authVM: AuthVM) {
    var isOtpFieldsVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showProgress by remember { mutableStateOf(false) }
    showProgress = authVM.isLoading
    if (showProgress) {
        LoadingDialog()
    }
    var phone by remember { mutableStateOf("") }
    var pinValue by remember { mutableStateOf("") }

    ForgotPasswordDesign(
        phone = phone,
        pinValue = pinValue,
        isOtpFieldsVisible = isOtpFieldsVisible,
        backClick = {
            navController.popBackStack()
        },
        sendOtpClick = {
            if (isValidPhone(phone).isNotEmpty())
                Toasty.error(context, isValidPhone(phone), Toast.LENGTH_SHORT, true).show()
            else {
            }
        },
        onPhone = {
            phone = it
        },
        onPinValue = { value, isLastDigit ->
            pinValue = value
        },
    )
}

@Composable
fun ForgotPasswordDesign(
    phone: String,
    pinValue: String,
    isOtpFieldsVisible: Boolean,
    onPhone: (String) -> Unit,
    onPinValue: (String, Boolean) -> Unit,
    backClick: () -> Unit,
    sendOtpClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.forgot_pass),
                backClick = { backClick() })

            Spacer(modifier = Modifier.padding(top = 20.dp))
            Text(
                text = stringResource(R.string.phone),
                color = DarkBlue,
                fontFamily = regularFont,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            MyTextFieldWithBorder(
                value = phone,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done,
                placeholder = stringResource(id = R.string.phone_no),
                onValueChange = { onPhone(it) },
                imageId = R.drawable.baseline_phone_24
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.send_otp),
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clickable {
                            sendOtpClick()
                        },
                    textAlign = TextAlign.End,
                    color = LightBlue,
                    fontFamily = regularFont,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            if (isOtpFieldsVisible)
                PinTextField(
                    otpText = pinValue,
                    otpCount = 6,
                    onOtpTextChange = { value, isLastDigit ->
                        onPinValue(value, isLastDigit)
                    })
        }
    }
}

@Preview
@Composable
fun ForgotPasswordPreview() {
    PSP_AndroidTheme {
        ForgotPasswordDesign(
            phone = "",
            pinValue = "",
            isOtpFieldsVisible = true,
            backClick = {},
            sendOtpClick = {},
            onPhone = {},
            onPinValue = { _, _ -> }
        )
    }
}