package com.pakscrap.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pakscrap.R
import com.pakscrap.network.isNetworkAvailable
import com.pakscrap.ui.commonViews.AppBlueButton
import com.pakscrap.ui.commonViews.Header
import com.pakscrap.ui.commonViews.LoadingDialog
import com.pakscrap.ui.commonViews.PasswordTextFields
import com.pakscrap.ui.screen.profile.models.UpdateUserData
import com.pakscrap.ui.theme.AppBG
import com.pakscrap.ui.theme.DarkBlue
import com.pakscrap.ui.theme.PSP_AndroidTheme
import com.pakscrap.ui.theme.regularFont
import com.pakscrap.utils.Utils.isValidPassword
import es.dmoral.toasty.Toasty

@Composable
fun ResetPasswordScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current
    val connectivityError = stringResource(id = R.string.network_error)
    var showProgress by remember { mutableStateOf(false) }
    showProgress = authVM.isLoading
    if (showProgress) {
        LoadingDialog()
    }
    val resetPasswordResponse = authVM.resetPasswordResponse
    if (resetPasswordResponse != null) {
        if (resetPasswordResponse.status) {
            Toasty.success(context, resetPasswordResponse.message, Toast.LENGTH_SHORT, false).show()
            navController.popBackStack()
            navController.popBackStack()
            authVM.phone = ""
        } else
            Toasty.error(context, resetPasswordResponse.message, Toast.LENGTH_SHORT, false).show()
        authVM.resetPasswordResponse = null
    }

    ResetPasswordDesign(backClick = {
        navController.popBackStack()
    },
        resetPasswordClick = { password, confirmPassword ->
            if (isValidPassword(password).isNotEmpty())
                Toasty.error(context, isValidPassword(password), Toast.LENGTH_SHORT, false).show()
            else if (isValidPassword(confirmPassword).isNotEmpty())
                Toasty.error(context, isValidPassword(confirmPassword), Toast.LENGTH_SHORT, false)
                    .show()
            else if (confirmPassword != password)
                Toasty.error(
                    context,
                    context.getString(R.string.confirm_pass_err),
                    Toast.LENGTH_SHORT,
                    false
                ).show()
            else {
                if (isNetworkAvailable(context)) {
                    authVM.resetPassword(
                        UpdateUserData(
                            phone = "+92${authVM.phone}",
                            newPassword = password
                        )
                    )
                } else {
                    Toasty.error(
                        context,
                        connectivityError,
                        Toast.LENGTH_SHORT,
                        false
                    )
                        .show()
                }
            }
        })
}

@Composable
fun ResetPasswordDesign(
    backClick: () -> Unit,
    resetPasswordClick: (String, String) -> Unit,
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                stringResource(id = R.string.reset_pass),
                backClick = { backClick() })
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Text(
                text = stringResource(R.string.reset_details),
                color = DarkBlue,
                fontFamily = regularFont,
                fontSize = 14.sp
            )
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
                ImeAction.Next,
                placeholder = stringResource(id = R.string.confirm_pass),
                onValueChange = { value ->
                    confirmPassword = value
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(Alignment.Bottom)
                .padding(bottom = 40.dp, start = 20.dp, end = 20.dp)
        ) {
            AppBlueButton(modifier = Modifier,
                text = stringResource(id = R.string.reset),
                onButtonClick = { resetPasswordClick(password, confirmPassword) })
        }
    }
}

@Preview
@Composable
fun ResetPasswordPreview() {
    PSP_AndroidTheme {
        ResetPasswordDesign(backClick = {},
            resetPasswordClick = { _, _ -> })
    }
}