package com.example.psrandroid.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.commonViews.MyTextFieldWithoutBorder
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.isValidPhone
import com.example.psrandroid.utils.Utils.isValidText
import com.example.psrandroid.utils.Utils.showToast
import com.example.psrandroid.utils.isVisible
import com.example.psrandroid.utils.progressBar
import io.github.rupinderjeet.kprogresshud.KProgressHUD

@Composable
fun SignupScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val progressBar: KProgressHUD = remember { context.progressBar() }
    progressBar.isVisible(authVM.isLoading)
    //register api response
    val authData = authVM.loginData
    if (authData != null) {
        showToast(context, authData.message)
        if (authData.status) {
            navController.popBackStack()
            navController.navigate(Screen.OTPScreen.route)
        }
        authVM.loginData = null
    }

    SignupScreen(
        backClick = { navController.popBackStack() },
        onLoginClick = {
            navController.popBackStack()
        },
        onRegisterButtonClick = { phone, name ->
            if (isValidText(name).isNotEmpty())
                showToast(context, isValidText(name))
            else if (isValidPhone(phone).isNotEmpty())
                showToast(context, isValidPhone("+92$phone"))
            else
                authVM.register(UserCredential(phone = "+92$phone", name = name))
        })
}

@Composable
fun SignupScreen(
    backClick: () -> Unit, onLoginClick: () -> Unit,
    onRegisterButtonClick: (String, String) -> Unit
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var fullName by rememberSaveable { mutableStateOf("") }
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
            Spacer(modifier = Modifier.height(40.dp))
            Header(
                modifier = null,
                stringResource(id = R.string.signup),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(100.dp))

            Column(modifier = Modifier.padding(0.dp)) {
                HorizontalDivider(color = Color.White, thickness = 2.dp)
                MyTextFieldWithoutBorder(
                    value = fullName,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    placeholder = stringResource(id = R.string.full_name),
                    onValueChange = { value ->
                        fullName = value
                    },
                    imageId = R.drawable.baseline_person_24
                )
                HorizontalDivider(color = Color.White, thickness = 2.dp)
                Spacer(modifier = Modifier.padding(top = 30.dp))
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
                        imeAction = ImeAction.Done,
                        placeholder = stringResource(id = R.string.number),
                        onValueChange = { value ->
                            phoneNumber = value
                        },
                        imageId = null
                    )
                }
                HorizontalDivider(color = Color.White, thickness = 2.dp)

                Spacer(modifier = Modifier.padding(top = 20.dp))
                AppButton(text = stringResource(id = R.string.signup), DarkBlue, Color.White) {
                    onRegisterButtonClick(phoneNumber, fullName)
                }
                Row(modifier = Modifier.align(CenterHorizontally)) {
                    Text(
                        text = stringResource(id = R.string.already_acc), modifier = Modifier
                            .padding(top = 30.dp, bottom = 20.dp),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.text_grey),
                        fontFamily = regularFont,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.login_in), modifier = Modifier
                            .padding(top = 30.dp, bottom = 20.dp, start = 4.dp)
                            .clickable(indication = null, // Remove the ripple effect
                                interactionSource = remember { MutableInteractionSource() } // Add this line to handle interactions without the ripple effect
                            ) {
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
        BottomNavigationButtons()

    }
}

@Preview
@Composable
fun PreviewSignupScreen() {
    PSP_AndroidTheme {
        SignupScreen(backClick = {}, onLoginClick = {}, onRegisterButtonClick = { _, _ -> })
    }
}
