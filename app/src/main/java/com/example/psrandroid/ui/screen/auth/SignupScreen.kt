package com.example.psrandroid.ui.screen.auth

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.dto.UserCredential
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.response.LocationData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.commonViews.MyTextFieldWithBorder
import com.example.psrandroid.ui.commonViews.PasswordTextFields
import com.example.psrandroid.ui.commonViews.PhoneTextField
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isValidPassword
import com.example.psrandroid.utils.Utils.isValidPhone
import com.example.psrandroid.utils.Utils.isValidText
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignupScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(authVM.isLoading)
    //register api response
    val authData = authVM.loginData
    val locationList = authVM.userPreferences.getLocationList()?.data ?: listOf()
    if (isNetworkAvailable(context)) {
        if (locationList.isEmpty())
            authVM.getLocation()
    }
    var selectedCity by rememberSaveable { mutableStateOf("") }

    if (authData != null) {
        if (authData.status) {
            Toasty.success(context, authData.message, Toast.LENGTH_SHORT, true).show()
            navController.popBackStack()
        } else
            Toasty.error(context, authData.message, Toast.LENGTH_SHORT, true).show()
        authVM.loginData = null
    }

    SignupScreen(
        locationList,
        backClick = { navController.popBackStack() },
        onLoginClick = {
            navController.popBackStack()
        },
        onRegisterButtonClick = { phone, name, password, confirmPass ->
            if (isNetworkAvailable(context)) {
                if (isValidText(name).isNotEmpty())
                    Toasty.error(context, isValidText(name), Toast.LENGTH_SHORT, true).show()
                else if (isValidPhone(phone).isNotEmpty())
                    Toasty.error(context, isValidPhone("+92$phone"), Toast.LENGTH_SHORT, true)
                        .show()
                else if (selectedCity.isEmpty() || selectedCity == "City")
                    Toasty.error(context, "Please select a city", Toast.LENGTH_SHORT, true).show()
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
                        true
                    ).show()
                else {
                    val phoneNumber = phone.takeLast(10)
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
                    "No internet connection. Please check your network settings.",
                    Toast.LENGTH_SHORT,
                    true
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
                        "No internet connection. Please check your network settings.",
                        Toast.LENGTH_SHORT,
                        true
                    )
                        .show()
            } else
                selectedCity = selectedLocation
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignupScreen(
    locationList: List<LocationData>,
    backClick: () -> Unit, onLoginClick: () -> Unit,
    onRegisterButtonClick: (String, String, String, String) -> Unit,
    onCitySelect: (String) -> Unit,
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var fullName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("City") }
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
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = R.drawable.lang_city_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop, // Adjust this as needed
            modifier = Modifier.fillMaxSize(),
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.signup),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(50.dp))

            Column {
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

                PhoneTextField(
                    value = phoneNumber,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    placeholder = stringResource(id = R.string.phone),
                    onValueChange = { phoneNumber = it },
                    imageId = R.drawable.baseline_phone_24
                )

                Spacer(modifier = Modifier.padding(top = 20.dp))

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
                    Text(
                        text = city,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.baseline_location_pin_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
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
                    text = stringResource(id = R.string.signup)
                ) {
                    onRegisterButtonClick(phoneNumber, fullName, password, confirmPassword)
                }
                Row(modifier = Modifier.align(CenterHorizontally)) {
                    Text(
                        text = stringResource(id = R.string.already_acc), modifier = Modifier
                            .padding(vertical = 20.dp),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.text_grey),
                        fontFamily = regularFont,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.login_in), modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp, start = 4.dp)
                            .clickable {
                                onLoginClick()
                            },
                        color = Color.White,
                        fontFamily = mediumFont,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
//        BottomNavigationButtons()

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewSignupScreen() {
    PSP_AndroidTheme {
        SignupScreen(listOf(LocationData.mockup),
            backClick = {},
            onLoginClick = {},
            onRegisterButtonClick = { _, _, _, _ -> },
            onCitySelect = {})
    }
}
