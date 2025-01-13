package com.example.psrandroid.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.network.isNetworkAvailable
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.commonViews.PasswordTextFields
import com.example.psrandroid.ui.commonViews.PhoneTextField
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isValidPassword
import com.example.psrandroid.utils.Utils.isValidPhone
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import es.dmoral.toasty.Toasty
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@Composable
fun LoginScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(authVM.isLoading)
    val locationList = authVM.userPreferences.getLocationList()?.data ?: listOf()
    if (isNetworkAvailable(context)) {
        if (locationList.isEmpty())
            authVM.getLocation()
    }
    //login api response
    val authData = authVM.loginData
    if (authData != null) {
        if (authData.status) {
            Toasty.success(context, authData.message, Toast.LENGTH_SHORT, true).show()
            authVM.userPreferences.isFirstLaunch = false
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(navController.graph.id)
            }
        } else
            Toasty.error(context, authData.message, Toast.LENGTH_SHORT, true).show()
        authVM.loginData = null
    }

    LoginScreen(onLoginButtonClick = { phoneNumber, password ->
        if (isNetworkAvailable(context)) {
            if (isValidPhone("+92$phoneNumber").isNotEmpty())
                Toasty.error(context, isValidPhone("+92$phoneNumber"), Toast.LENGTH_SHORT, true)
                    .show()
            else if (isValidPassword(password).isNotEmpty())
                Toasty.error(context, isValidPassword(password), Toast.LENGTH_SHORT, true).show()
            else {
                val phone = phoneNumber.takeLast(10)
                authVM.login(UserCredential(phone = "+92$phone", password = password, deviceId = "ksdgha"))
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
        onSignup = {
            navController.navigate(Screen.RegisterScreen.route)
        },
        onForgotPasswordClick = {})
}

@Composable
fun LoginScreen(
    onLoginButtonClick: (String, String) -> Unit,
    onSignup: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lang_city_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop, // Adjust this as needed
            modifier = Modifier.fillMaxSize(),
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.login_in), color = Color.White,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(80.dp))

            PhoneTextField(
                value = phoneNumber,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                placeholder = stringResource(id = R.string.phone),
                onValueChange = { phoneNumber = it },
                imageId = R.drawable.baseline_phone_24
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            PasswordTextFields(
                value = password,
                KeyboardType.Password,
                ImeAction.Done,
                placeholder = stringResource(id = R.string.password),
                onValueChange = { newText ->
                    password = newText
                }
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.forgot_pass),
                    modifier = Modifier
                        .clickable {
                            onForgotPasswordClick()
                        },
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = mediumFont,
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            AppButton(
                modifier = Modifier
                    .widthIn(min = 300.dp, max = 600.dp)
                    .padding(bottom = 30.dp),
                text = stringResource(id = R.string.login_in)
            ) {
                onLoginButtonClick(phoneNumber, password)
            }
            Row(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(vertical = 80.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.dont_acc),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_grey),
                    fontFamily = regularFont,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.signup), modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            onSignup()
                        },
                    color = Color.White,
                    fontFamily = mediumFont,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    PSP_AndroidTheme {
        LoginScreen(onLoginButtonClick = { _, _ -> }, onSignup = {},
            onForgotPasswordClick = {})
    }
}