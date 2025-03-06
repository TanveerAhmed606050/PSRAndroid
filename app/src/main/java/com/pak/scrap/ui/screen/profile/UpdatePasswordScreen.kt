package com.pak.scrap.ui.screen.profile

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
import androidx.navigation.NavHostController
import com.pak.scrap.R
import com.pak.scrap.ui.commonViews.AppButton
import com.pak.scrap.ui.commonViews.Header
import com.pak.scrap.ui.commonViews.LoadingDialog
import com.pak.scrap.ui.commonViews.PasswordTextFields
import com.pak.scrap.ui.screen.profile.models.UpdateUserData
import com.pak.scrap.ui.theme.AppBG
import com.pak.scrap.ui.theme.DarkBlue
import com.pak.scrap.ui.theme.PSP_AndroidTheme
import com.pak.scrap.ui.theme.regularFont
import com.pak.scrap.utils.Utils.isValidPassword
import es.dmoral.toasty.Toasty

@Composable
fun UpdatePasswordScreen(navController: NavHostController, profileVM: ProfileVM) {
    val context = LocalContext.current
    var showProgress by remember { mutableStateOf(false) }
    showProgress = profileVM.isLoading
    if (showProgress)
        LoadingDialog()

    val updateResponse = profileVM.infoResponse
    if (updateResponse != null) {
        if (updateResponse.status) {
            Toasty.success(context, updateResponse.message, Toast.LENGTH_SHORT, false).show()
            navController.popBackStack()
        } else
            Toasty.error(context, updateResponse.message, Toast.LENGTH_SHORT, false).show()
        profileVM.infoResponse = null
    }

    UpdatePasswordScreen(backClick = {
        navController.popBackStack()
    }, updateBtnClick = { oldPassword, newPassword, confirmPassword ->
        if (isValidPassword(oldPassword).isNotEmpty())
            Toasty.error(context, isValidPassword(oldPassword), Toast.LENGTH_SHORT, false).show()
        else if (isValidPassword(newPassword).isNotEmpty())
            Toasty.error(context, isValidPassword(newPassword), Toast.LENGTH_SHORT, false).show()
        else if (isValidPassword(confirmPassword).isNotEmpty())
            Toasty.error(context, isValidPassword(confirmPassword), Toast.LENGTH_SHORT, false).show()
        else if (confirmPassword != newPassword)
            Toasty.error(
                context,
                context.getString(R.string.confirm_pass_err),
                Toast.LENGTH_SHORT,
                false
            ).show()
        else {
            profileVM.updatePassword(
                UpdateUserData(
                    userId = "${profileVM.userPreferences.getUserPreference()?.id}",
                    currentPassword = oldPassword,
                    newPassword = newPassword,
                )
            )
        }
    })
}

@Composable
fun UpdatePasswordScreen(
    backClick: () -> Unit,
    updateBtnClick: (String, String, String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }

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
                stringResource(id = R.string.update_pass),
                backClick = { backClick() })

            Spacer(modifier = Modifier.padding(top = 20.dp))
            Text(
                text = stringResource(R.string.change_pass_detail),
                color = DarkBlue,
                fontFamily = regularFont,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            PasswordTextFields(
                value = oldPassword,
                KeyboardType.Password,
                ImeAction.Next,
                placeholder = stringResource(id = R.string.old_pass),
                onValueChange = { newText ->
                    oldPassword = newText
                }
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            PasswordTextFields(
                value = newPassword,
                KeyboardType.Password,
                ImeAction.Next,
                placeholder = stringResource(id = R.string.password),
                onValueChange = { newText ->
                    newPassword = newText
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.Bottom)
                    .padding(bottom = 40.dp)
            ) {
                AppButton(modifier = Modifier,
                    text = stringResource(id = R.string.update_pass),
                    onButtonClick = { updateBtnClick(oldPassword, newPassword, confirmPassword) })
            }
        }
    }
}

@Preview
@Composable
fun UpdatePasswordScreenPreview() {
    PSP_AndroidTheme {
        UpdatePasswordScreen(backClick = {}, updateBtnClick = { _, _, _ -> })
    }
}