package com.pakscrap.ui.screen.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pakscrap.R
import com.pakscrap.network.isNetworkAvailable
import com.pakscrap.ui.commonViews.Header
import com.pakscrap.ui.commonViews.LoadingDialog
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.mediumFont
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay

@Composable
fun OTPScreen(navController: NavController, authVM: AuthVM, phoneNumber: String?) {
    val isOtpFieldsVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val noNetworkMessage = stringResource(id = R.string.network_error)
//    var phone by remember { mutableStateOf("") }
    var pinValue by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }
    showProgress = authVM.isLoading
    if (showProgress) {
        LoadingDialog()
    }
    val message by authVM.message.collectAsState()
    if (message?.isNotEmpty() == true) {
        Toasty.success(context, message ?: "", Toast.LENGTH_SHORT, false).show()
        if (message == "Verification successful") {
            navController.popBackStack()
        }
        authVM.updateMessage("")
    }

    LaunchedEffect(Unit) {
        authVM.sendVerificationCode("+92$phoneNumber", context as Activity)
    }
    OTPScreen(
        isOtpFieldsVisible = isOtpFieldsVisible,
        pinValue = pinValue,
        backClick = {
            navController.popBackStack()
        },
        onNextClick = {
            if (isNetworkAvailable(context)) {

                authVM.verifyCode(pinValue)
            } else {
                Toasty.error(
                    context,
                    noNetworkMessage,
                    Toast.LENGTH_SHORT,
                    false
                )
                    .show()
            }
        },
        onResendOtp = {
            authVM.sendVerificationCode("+92$phoneNumber", context as Activity)
        },
        onPinValue = { value, _ ->
            pinValue = value
        },
    )
}

@Composable
fun OTPScreen(
    isOtpFieldsVisible: Boolean,
    pinValue: String,
    backClick: () -> Unit, onNextClick: () -> Unit,
    onResendOtp: () -> Unit,
    onPinValue: (String, Boolean) -> Unit,
) {
    var timerSeconds by remember { mutableIntStateOf(60) }
    var isResendEnabled by remember { mutableStateOf(isOtpFieldsVisible) }
    // Start countdown when dialog opens
    LaunchedEffect(timerSeconds) {
        if (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        } else {
            isResendEnabled = true
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.enter_pin),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(100.dp))
            PinTextField(otpText = pinValue, otpCount = 6, onOtpTextChange = { value, status ->
                onPinValue(value, status)
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
                            timerSeconds = 60
                            isResendEnabled = false
                        }
                    },
                fontSize = 16.sp,
                fontFamily = mediumFont,
                textAlign = TextAlign.End,
                color = if (isResendEnabled) DarkBlue else Color.Gray
            )

        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable { onNextClick() }
        ) {
            Row(
                modifier = Modifier
                    .background(color = DarkBlue, shape = CircleShape)
                    .size(50.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun PinTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText,
                        modifier = Modifier
                            .weight(1f) // Ensure equal width for each CharView
                            .aspectRatio(0.8f) // Optional: Adjust aspect ratio for better appearance
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }

    Text(
        modifier = modifier
//            .width(40.dp)
//            .height(50.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = DarkBlue,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(top = 12.dp),
        text = char,
        style = MaterialTheme.typography.titleLarge,
        color = DarkBlue,
        textAlign = TextAlign.Center,
        fontSize = 16.sp
    )
}

@Preview
@Composable
fun PreviewPasswordScreen() {
    PSP_AndroidTheme {
        OTPScreen(isOtpFieldsVisible = false, backClick = {}, onNextClick = {},
            onResendOtp = {},
            pinValue = "", onPinValue = { _, _ -> })
    }
}