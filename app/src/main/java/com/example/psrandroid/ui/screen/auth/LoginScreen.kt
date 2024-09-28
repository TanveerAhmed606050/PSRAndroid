package com.example.psrandroid.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.commonViews.AppButton
import com.example.psrandroid.ui.commonViews.MyTextFieldWithoutBorder
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isValidPhone
import com.example.psrandroid.utils.Utils.showToast
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@Composable
fun LoginScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(authVM.isLoading)
    //login api response
    val authData = authVM.loginData
    if (authData != null) {
        showToast(context, authData.message)
        if (authData.status) {
            navController.navigate(Screen.OTPScreen.route)
//            {
//                popUpTo(navController.graph.id)
//            }
        }
        authVM.loginData = null
    }

    LoginScreen(onLoginButtonClick = {phoneNumber->
        if (isValidPhone(phoneNumber).isNotEmpty())
            showToast(context, isValidPhone("+92$phoneNumber"))
        else
            authVM.login(UserCredential(phone = "+92$phoneNumber"))
    },
        onSignup = {
            navController.navigate(Screen.RegisterScreen.route)
        })
}

@Composable
fun LoginScreen(
    onLoginButtonClick: (String) -> Unit,
    onSignup: () -> Unit
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
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
        Column(modifier = Modifier.padding(horizontal = 30.dp)) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = stringResource(id = R.string.enter_num), color = Color.White,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(120.dp))

            Column(
                modifier = Modifier.padding(0.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(color = Color.White, thickness = 2.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = "+92", color = DarkBlue, modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    MyTextFieldWithoutBorder(
                        value = phoneNumber,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                        placeholder = stringResource(id = R.string.number),
                        onValueChange = { value ->
                            phoneNumber = value
                        },
                        imageId = null
                    )

                }
                HorizontalDivider(color = Color.White, thickness = 2.dp)
            }
//            Spacer(modifier = Modifier.padding(top = 30.dp))
//            HorizontalDivider(color = Color.White, thickness = 2.dp)
//            PasswordTextFields(
//                value = password,
//                KeyboardType.Password,
//                ImeAction.Done,
//                placeholder = stringResource(id = R.string.password),
//                onValueChange = { newText ->
//                    password = newText
//                }
//            )
//            HorizontalDivider(color = Color.White, thickness = 2.dp)
//            Spacer(modifier = Modifier.padding(top = 15.dp))
//            Row(modifier = Modifier.fillMaxWidth()) {
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = stringResource(id = R.string.forgot_pass),
//                    modifier = Modifier
//                        .clickable(indication = null, // Remove the ripple effect
//                            interactionSource = remember { MutableInteractionSource() } // Add this line to handle interactions without the ripple effect
//                        ) {
////                            onForgotPasswordClick()
//                        },
//                    color = Color.White,
//                    fontSize = 12.sp,
//                    fontFamily = mediumFont,
//                    textAlign = TextAlign.End
//                )
//            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            AppButton(text = stringResource(id = R.string.login_in), DarkBlue, Color.White) {
                onLoginButtonClick(phoneNumber)
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
        BottomNavigationButtons()
    }
}

@Composable
fun BottomNavigationButtons() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.character_ic), contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    PSP_AndroidTheme {
        LoginScreen( onLoginButtonClick = {}, onSignup = {})
    }
}