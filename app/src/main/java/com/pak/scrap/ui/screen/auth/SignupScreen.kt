package com.pak.scrap.ui.screen.auth

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.pak.scrap.R
import com.pak.scrap.dto.UserCredential
import com.pak.scrap.navigation.Screen
import com.pak.scrap.network.isNetworkAvailable
import com.pak.scrap.response.LocationData
import com.pak.scrap.response.mockup
import com.pak.scrap.ui.commonViews.AppButton
import com.pak.scrap.ui.commonViews.Header
import com.pak.scrap.ui.commonViews.LoadingDialog
import com.pak.scrap.ui.commonViews.MyTextFieldWithBorder
import com.pak.scrap.ui.commonViews.PasswordTextFields
import com.pak.scrap.ui.commonViews.PhoneTextField
import com.pak.scrap.ui.screen.profile.models.UpdateUserData
import com.pak.scrap.ui.theme.AppBG
import com.pak.scrap.ui.theme.DarkBlue
import com.pak.scrap.ui.theme.LightBlue
import com.pak.scrap.ui.theme.LightRed40
import com.pak.scrap.ui.theme.PSP_AndroidTheme
import com.pak.scrap.ui.theme.mediumFont
import com.pak.scrap.ui.theme.regularFont
import com.pak.scrap.utils.Utils.isRtlLocale
import com.pak.scrap.utils.Utils.isValidPassword
import com.pak.scrap.utils.Utils.isValidPhone
import com.pak.scrap.utils.Utils.isValidText
import es.dmoral.toasty.Toasty
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignupScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    var isPhoneNoVerified by remember { mutableStateOf(false) }
    val otpText = remember { mutableStateOf("") }
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
            Toasty.success(context, message ?: "", Toast.LENGTH_SHORT, true).show()
            showDialog.value = true
        } else if (message == "Verification successful") {
            Toasty.success(context, message ?: "", Toast.LENGTH_SHORT, true).show()
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
            authVM.sendVerificationCode("+92$phoneNumber", context as Activity)
        }
        authVM.resetPasswordResponse = null
    }

    if (authData != null) {
        if (authData.status) {
            Toasty.success(context, authData.message, Toast.LENGTH_SHORT, true).show()
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
        otpText = otpText.value,
        otpCount = 6, // Example OTP length
        onOtpTextChange = { text, _ ->
            otpText.value = text
        },
        onConfirmBtn = {
            authVM.verifyCode(otpText.value)
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
                    Toasty.error(context, isValidPhone("+92$phone"), Toast.LENGTH_SHORT, true)
                        .show()
                else if (selectedCity.isEmpty() || selectedCity == context.getString(R.string.city))
                    Toasty.error(
                        context,
                        context.getString(R.string.select_city_error),
                        Toast.LENGTH_SHORT,
                        false
                    ).show()
                else if (isValidPassword(password).isNotEmpty())
                    Toasty.error(context, isValidPassword(password), Toast.LENGTH_SHORT, true)
                        .show()
                else if (isValidPassword(confirmPass).isNotEmpty())
                    Toasty.error(context, isValidPassword(confirmPass), Toast.LENGTH_SHORT, true)
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
                        phone = "+92$phoneNo"
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
                            fontSize = 12.sp,
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
    onConfirmBtn: () -> Unit,
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "Enter OTP", fontFamily = mediumFont,
                    color = DarkBlue, fontSize = 18.sp,
                )
            },
            text = {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Spacer(modifier = Modifier.height(40.dp))
                    PinTextField(
                        otpText = otpText,
                        otpCount = otpCount,
                        onOtpTextChange = onOtpTextChange
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmBtn()
                }) {
                    Text(
                        stringResource(id = R.string.confirm),
                        fontSize = 16.sp, fontFamily = mediumFont, color = DarkBlue,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(
                        stringResource(id = R.string.cancel),
                        fontSize = 16.sp, fontFamily = mediumFont, color = Color.Red
                    )
                }
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
