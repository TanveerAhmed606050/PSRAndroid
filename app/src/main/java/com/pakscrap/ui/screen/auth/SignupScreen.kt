package com.pakscrap.ui.screen.auth

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.pakscrap.R
import com.pakscrap.dto.UserCredential
import com.pakscrap.navigation.Screen
import com.pakscrap.network.isNetworkAvailable
import com.pakscrap.response.LocationData
import com.pakscrap.response.mockup
import com.pakscrap.ui.commonViews.AppButton
import com.pakscrap.ui.commonViews.Header
import com.pakscrap.ui.commonViews.LoadingDialog
import com.pakscrap.ui.commonViews.MyTextFieldWithBorder
import com.pakscrap.ui.commonViews.PasswordTextFields
import com.pakscrap.ui.commonViews.PhoneTextField
import com.pakscrap.ui.screen.profile.models.UpdateUserData
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.LightBlue
import com.pakscrap.ui.theme.LightRed40
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.mediumFont
import com.pakscrap.ui.theme.regularFont
import com.pakscrap.utils.Utils.isRtlLocale
import com.pakscrap.utils.Utils.isValidPassword
import com.pakscrap.utils.Utils.isValidPhone
import com.pakscrap.utils.Utils.isValidText
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignupScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    var isPhoneNoVerified by remember { mutableStateOf(false) }
    var otpText by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCity by rememberSaveable { mutableStateOf("") }
    val message by authVM.message.collectAsState()
    var showProgress by remember { mutableStateOf(false) }
    showProgress = authVM.isLoading
    if (showProgress)
        LoadingDialog()
    val noNetworkMessage = stringResource(id = R.string.network_error)
    //register api response
    val authData = authVM.loginData
    val locationList = authVM.userPreferences.getLocationList()?.data ?: listOf()
    if (isNetworkAvailable(context)) {
        if (locationList.isEmpty())
            authVM.getLocation()
    }
    //handle firebase validation
    if (message?.isNotEmpty() == true) {
        if (message?.contains("code sent to", ignoreCase = true)!!) {
            Toasty.success(context, message ?: "", Toast.LENGTH_SHORT, false).show()
            if (!showDialog.value)
                showDialog.value = true
        } else if (message == "Verification successful") {
            Toasty.success(context, message ?: "", Toast.LENGTH_SHORT, false).show()
            showDialog.value = false
            isPhoneNoVerified = true
        } else
            Toasty.error(context, message ?: "", Toast.LENGTH_SHORT, false).show()

        authVM.updateMessage("")
    }
    val isPhoneNumberExistResponse = authVM.resetPasswordResponse
    if (isPhoneNumberExistResponse != null) {
        if (isPhoneNumberExistResponse.status) {
            Toasty.error(
                context,
                context.getString(R.string.number_available_err),
                Toast.LENGTH_LONG,
                false
            ).show()
        } else {
            showDialog.value = true
            authVM.sendVerificationCode("+92$phoneNumber", context as Activity)
        }
        authVM.resetPasswordResponse = null
    }

    if (authData != null) {
        if (authData.status) {
            Toasty.success(context, authData.message, Toast.LENGTH_SHORT, false).show()
            authVM.userPreferences.isFirstLaunch = false
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(navController.graph.id)
            }
        } else
            Toasty.error(context, authData.message, Toast.LENGTH_SHORT, false).show()
        authVM.loginData = null
    }
    //OTP dialog
    OtpDialog(
        showDialog = showDialog,
        otpText = otpText,
        otpCount = 6, // Example OTP length
        onOtpTextChange = { otp, _ ->
            otpText = otp
        },
        onResendOtp = {
            authVM.sendVerificationCode("+92$phoneNumber", context as Activity)
        },
        onVerifyOtp = {
            authVM.verifyCode(otpText)
        }
    )
    SignupScreen(
        context = context,
        locationList = locationList,
        isPhoneNoVerified = isPhoneNoVerified,
        backClick = { navController.popBackStack() },
        onLoginClick = {
            navController.popBackStack()
        },
        onRegisterButtonClick = { phone, name, password, confirmPass ->
            phoneNumber = phone
            if (isNetworkAvailable(context)) {
                if (isValidText(name).isNotEmpty())
                    Toasty.error(context, isValidText(name), Toast.LENGTH_SHORT, true).show()
                else if (isValidPhone(phone).isNotEmpty())
                    Toasty.error(context, isValidPhone("+92$phone"), Toast.LENGTH_SHORT, false)
                        .show()
                else if (selectedCity.isEmpty() || selectedCity == context.getString(R.string.city))
                    Toasty.error(
                        context,
                        context.getString(R.string.select_city_error),
                        Toast.LENGTH_SHORT,
                        false
                    ).show()
                else if (isValidPassword(password).isNotEmpty())
                    Toasty.error(context, isValidPassword(password), Toast.LENGTH_SHORT, false)
                        .show()
                else if (isValidPassword(confirmPass).isNotEmpty())
                    Toasty.error(context, isValidPassword(confirmPass), Toast.LENGTH_SHORT, false)
                        .show()
                else if (password != confirmPass)
                    Toasty.error(
                        context,
                        context.getString(R.string.confirm_pass_err),
                        Toast.LENGTH_SHORT,
                        false
                    ).show()
                else {
                    phoneNumber = phone.takeLast(10)
                    authVM.register(
                        UserCredential(
                            phone = "+92$phoneNumber", name = name, password = password,
                            location = selectedCity
                        )
                    )
                    showDialog.value = true
                }
            } else
                Toasty.error(
                    context,
                    noNetworkMessage,
                    Toast.LENGTH_SHORT,
                    false
                )
                    .show()

        },
        onCitySelect = { selectedLocation ->
            if (selectedLocation == "empty") {
                if (isNetworkAvailable(context)) {
                    authVM.getLocation()
                } else
                    Toasty.error(
                        context,
                        noNetworkMessage,
                        Toast.LENGTH_SHORT,
                        false
                    )
                        .show()
            } else
                selectedCity = selectedLocation
        },
        onVerificationIconClick = { phoneNo ->
            if (isValidPhone(phoneNo).isNotEmpty())
                Toasty.error(
                    context,
                    isValidPhone(phoneNo),
                    Toast.LENGTH_SHORT,
                    false
                )
                    .show()
            else {
                phoneNumber = phoneNo.takeLast(10)
                authVM.phoneValidate(
                    UpdateUserData(
                        phone = "+92$phoneNumber"
                    )
                )
            }
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignupScreen(
    context: Context,
    locationList: List<LocationData>,
    isPhoneNoVerified: Boolean,
    backClick: () -> Unit, onLoginClick: () -> Unit,
    onRegisterButtonClick: (String, String, String, String) -> Unit,
    onCitySelect: (String) -> Unit,
    onVerificationIconClick: (String) -> Unit,
) {
    val currentLocale = Locale.getDefault()
    val isRtl = isRtlLocale(currentLocale)
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var fullName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf(context.getText(R.string.city)) }
    var expandedCity by remember { mutableStateOf(false) }
    val locationData = locationList.map { it.name }

    if (expandedCity) {
        ListDialog(dataList = locationData, onDismiss = { expandedCity = false },
            onConfirm = { locationName ->
                val locationId = locationList.find { it.name == locationName }?.name ?: ""
                onCitySelect(locationId)
                city = locationName
                expandedCity = false
            })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.signup),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(50.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                MyTextFieldWithBorder(
                    value = fullName,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    placeholder = stringResource(id = R.string.full_name),
                    onValueChange = { value ->
                        fullName = value
                    },
                    imageId = R.drawable.baseline_person_24
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isRtl)
                        Text(
                            text = if (isPhoneNoVerified) stringResource(id = R.string.verified)
                            else stringResource(id = R.string.verify_now),
                            color = if (isPhoneNoVerified) DarkBlue else LightRed40,
                            fontFamily = mediumFont,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 8.dp)
                                .clickable { onVerificationIconClick(phoneNumber) }
                        )
                    PhoneTextField(
                        value = phoneNumber,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                        placeholder = stringResource(id = R.string.phone),
                        onValueChange = { phoneNumber = it },
                        imageId = R.drawable.baseline_phone_24,
                        modifier = Modifier.weight(1f) // Ensure it takes available space
                    )
                    if (!isRtl)
                        Text(
                            text = if (isPhoneNoVerified) stringResource(id = R.string.verified)
                            else stringResource(id = R.string.verify_now),
                            color = if (isPhoneNoVerified) LightBlue else LightRed40,
                            fontFamily = mediumFont,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                                .clickable { onVerificationIconClick(phoneNumber) }
                        )
                }

                Spacer(modifier = Modifier.padding(top = 10.dp))
//                if (isValidPhone(phoneNumber).isEmpty() && !isPhoneNoVerified)
//                if (!isPhoneNoVerified)
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { onVerificationIconClick(phoneNumber) },
//                        horizontalArrangement = if (isRtl) Arrangement.Start else Arrangement.End,
//                    ) {
//                        Text(
//                            text = stringResource(
//                                id = R.string.verify_now
//                            ),
//                            color = LightRed40,
//                            fontFamily = regularFont,
//                            fontSize = 14.sp,
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.verify_mbl_ic),
//                            contentDescription = "",
//                            modifier = Modifier.size(32.dp),
//                            colorFilter = ColorFilter.tint(DarkBlue)
//                        )
//                    }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (locationList.isNotEmpty())
                                expandedCity = true
                            else
                                onCitySelect("empty")
                        }
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = colorResource(id = R.color.text_grey)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isRtl) {
                        Text(
                            text = city.toString(),
                            color = DarkBlue,
                            fontSize = 14.sp,
                            fontFamily = regularFont,
                            modifier = Modifier
                                .padding(horizontal = 12.dp),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Image(
                        painter = painterResource(id = R.drawable.baseline_location_pin_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        colorFilter = ColorFilter.tint(DarkBlue),
                    )
                    if (isRtl) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = city.toString(),
                            color = DarkBlue,
                            fontSize = 14.sp,
                            fontFamily = regularFont,
                            modifier = Modifier
                                .padding(horizontal = 12.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(top = 20.dp))
                PasswordTextFields(
                    value = password,
                    KeyboardType.Password,
                    ImeAction.Next,
                    placeholder = stringResource(id = R.string.password),
                    onValueChange = { newText ->
                        password = newText
                    }
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                PasswordTextFields(
                    value = confirmPassword,
                    KeyboardType.Password,
                    ImeAction.Done,
                    placeholder = stringResource(id = R.string.confirm_pass),
                    onValueChange = { newText ->
                        confirmPassword = newText
                    }
                )
                Spacer(modifier = Modifier.padding(top = 30.dp))
                AppButton(
                    modifier = Modifier
                        .widthIn(min = 300.dp, max = 600.dp)
                        .padding(bottom = 30.dp),
                    text = stringResource(id = R.string.signup),
                    isEnable = isPhoneNoVerified
                ) {
                    onRegisterButtonClick(phoneNumber, fullName, password, confirmPassword)
                }
                Row(modifier = Modifier.align(CenterHorizontally)) {
                    if (isRtl)
                        Text(
                            text = stringResource(id = R.string.login_in), modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp, end = 4.dp)
                                .clickable {
                                    onLoginClick()
                                },
                            color = LightBlue,
                            fontFamily = mediumFont,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    Text(
                        text = stringResource(id = R.string.already_acc),
                        modifier = Modifier
                            .padding(vertical = 20.dp),
                        fontSize = 12.sp,
                        color = DarkBlue,
                        fontFamily = regularFont,
                        textAlign = TextAlign.Center,
                    )
                    if (!isRtl)
                        Text(
                            text = stringResource(id = R.string.login_in), modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp, start = 4.dp)
                                .clickable {
                                    onLoginClick()
                                },
                            color = LightBlue,
                            fontFamily = mediumFont,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                }
            }
        }
    }
}

@Composable
fun OtpDialog(
    showDialog: MutableState<Boolean>,
    otpText: String,
    otpCount: Int,
    onOtpTextChange: (String, Boolean) -> Unit,
    onVerifyOtp: () -> Unit,
    onResendOtp: () -> Unit,
) {
    var timerSeconds by remember { mutableIntStateOf(60) }
    var isResendEnabled by remember { mutableStateOf(false) }
    var resendTrigger by remember { mutableIntStateOf(0) } // Force LaunchedEffect restart

    // Start countdown when dialog opens or resend is triggered
    LaunchedEffect(showDialog.value, resendTrigger) {
        if (showDialog.value) {
            timerSeconds = 60
            isResendEnabled = false
            while (timerSeconds > 0) {
                delay(1000L)
                timerSeconds--
            }
            isResendEnabled = true
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Header(
                    modifier = Modifier,
                    headerText = stringResource(id = R.string.enter_otp),
                    backClick = {
                        showDialog.value = false
                    })
            },
            text = {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Spacer(modifier = Modifier.height(40.dp))
                    PinTextField(
                        otpText = otpText,
                        otpCount = otpCount,
                        onOtpTextChange = { value, isLastDigit ->
                            onOtpTextChange(value, isLastDigit)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    AppButton(modifier = Modifier, text = stringResource(id = R.string.verify_now),
                        isEnable = otpText.length == 6,
                        onButtonClick = { onVerifyOtp() })
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        if (isResendEnabled) stringResource(id = R.string.resend_otp)
                        else "00:${String.format(Locale.getDefault(), "%02d", timerSeconds)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = isResendEnabled) {
                                if (isResendEnabled) {
                                    onResendOtp()
                                    resendTrigger++ // Change state to restart LaunchedEffect
                                }
                            },
                        fontSize = 16.sp,
                        textDecoration = if (isResendEnabled) TextDecoration.Underline else TextDecoration.None,
                        fontFamily = mediumFont,
                        textAlign = TextAlign.Center,
                        color = if (isResendEnabled) DarkBlue else Color.Gray
                    )
                }
            },
            confirmButton = {
//                Text(
//                    if (isResendEnabled) stringResource(id = R.string.resend_otp)
//                    else "00:$timerSeconds",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable(enabled = isResendEnabled) {
//                            if (isResendEnabled) {
//                                onResendOtp()
//                                resendTrigger++ // Change state to restart LaunchedEffect
//                            }
//                        },
//                    fontSize = 16.sp,
//                    fontFamily = mediumFont,
//                    color = if (isResendEnabled) DarkBlue else Color.Gray
//                )
            }
        )
    }
}

@Composable
fun ListDialog(
    dataList: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Ensures column takes the full width
                    .verticalScroll(rememberScrollState())
            ) {
                // List of cities from assets as clickable items
                dataList.forEach { city ->
                    Text(
                        text = city,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onConfirm(city)
                            }
                            .padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewSignupScreen() {
    PSP_AndroidTheme {
        SignupScreen(context = LocalContext.current, listOf(LocationData.mockup),
            false,
            backClick = {},
            onLoginClick = {},
            onRegisterButtonClick = { _, _, _, _ -> },
            onCitySelect = {},
            onVerificationIconClick = {})
    }
}
