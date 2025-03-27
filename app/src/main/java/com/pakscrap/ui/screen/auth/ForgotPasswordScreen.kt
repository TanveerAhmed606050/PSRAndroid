package com.pakscrap.ui.screen.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pakscrap.R
import com.pakscrap.navigation.Screen
import com.pakscrap.network.isNetworkAvailable
import com.pakscrap.ui.commonViews.Header
import com.pakscrap.ui.commonViews.LoadingDialog
import com.pakscrap.ui.commonViews.PhoneTextField
import com.pakscrap.ui.screen.profile.models.UpdateUserData
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.LightBlue
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.mediumFont
import com.pakscrap.ui.theme.regularFont
import com.pakscrap.utils.Utils.isValidPhone
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(navController: NavController, authVM: AuthVM) {
    var isOtpFieldsVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val noNetworkMessage = stringResource(id = R.string.network_error)
//    var phone by remember { mutableStateOf("") }
    var pinValue by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }
    showProgress = authVM.isLoading
    if (showProgress) {
        LoadingDialog()
    }
    val forgotPasswordResponse = authVM.resetPasswordResponse
    if (forgotPasswordResponse != null) {
        if (forgotPasswordResponse.status) {
            isOtpFieldsVisible = true
            val phoneNo = authVM.phone.takeLast(10)
            Toasty.success(context, forgotPasswordResponse.message, Toast.LENGTH_SHORT, false)
                .show()
            authVM.sendVerificationCode("+92$phoneNo", context as Activity)
        } else
            Toasty.error(context, forgotPasswordResponse.message, Toast.LENGTH_SHORT, false).show()
        authVM.resetPasswordResponse = null
    }

    val message by authVM.message.collectAsState()
    if (message?.isNotEmpty() == true) {
        Toasty.success(context, message ?: "", Toast.LENGTH_SHORT, false).show()
        if (message == "Verification successful") {
//                navController.navigate(Screen.ResetPasswordScreen.route + "myPhone/$phone")
            navController.navigate(Screen.ResetPasswordScreen.route)
        }
        authVM.updateMessage("")
    }

    ForgotPasswordDesign(
        phone = authVM.phone,
        pinValue = pinValue,
        isOtpFieldsVisible = isOtpFieldsVisible,
        backClick = {
            navController.popBackStack()
            authVM.phone = ""
        },
        sendOtpClick = {
            val phoneNo = authVM.phone.takeLast(10)
            if (isValidPhone(phoneNo).isNotEmpty())
                Toasty.error(context, isValidPhone(phoneNo), Toast.LENGTH_SHORT, false).show()
            else {
                if (isNetworkAvailable(context)) {
                    authVM.phoneValidate(
                        UpdateUserData(
                            phone = "+92$phoneNo"
                        )
                    )
                } else {
                    Toasty.error(
                        context,
                        noNetworkMessage,
                        Toast.LENGTH_SHORT,
                        false
                    )
                        .show()
                }
            }
        },
        onPhone = {
            authVM.phone = it
        },
        onPinValue = { value, isLastDigit ->
            pinValue = value
            if (isLastDigit)
                authVM.verifyCode(pinValue)
        },
        onResendOtp = {
            authVM.sendVerificationCode("+92${authVM.phone}", context as Activity)
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
    onResendOtp: () -> Unit,
) {
    var timerSeconds by remember { mutableIntStateOf(60) }
    var isResendEnabled by remember { mutableStateOf(isOtpFieldsVisible) }
    var resendTrigger by remember { mutableIntStateOf(0) } // Force LaunchedEffect restart
    // Start countdown when dialog opens
    LaunchedEffect(resendTrigger) {
        timerSeconds = 60
        isResendEnabled = false
        while (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        }
        isResendEnabled = true
    }

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
            if (!isOtpFieldsVisible) {
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Text(
                    text = stringResource(R.string.phone_no),
                    color = DarkBlue,
                    fontFamily = regularFont,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PhoneTextField(
                        value = phone,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                        placeholder = stringResource(id = R.string.phone),
                        onValueChange = { onPhone(it) },
                        imageId = R.drawable.baseline_phone_24,
                        modifier = Modifier.weight(1f) // Ensure it takes available space
                    )
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
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
            }
            if (isOtpFieldsVisible) {
                Spacer(modifier = Modifier.padding(top = 90.dp))
                PinTextField(
                    otpText = pinValue,
                    otpCount = 6,
                    onOtpTextChange = { value, isLastDigit ->
                        onPinValue(value, isLastDigit)
                    })
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    if (isResendEnabled) stringResource(id = R.string.resend_otp)
                    else "Resend OTP in $timerSeconds s",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isResendEnabled) {
                                onResendOtp()
                                resendTrigger++ // Change state to restart LaunchedEffect
                            }
                        },
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    textAlign = TextAlign.End,
                    color = if (isResendEnabled) DarkBlue else Color.Gray
                )

            }
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
            onPinValue = { _, _ -> },
            onResendOtp = {}
        )
    }
}