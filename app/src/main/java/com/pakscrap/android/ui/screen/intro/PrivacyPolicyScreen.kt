package com.pakscrap.android.ui.screen.intro

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pakscrap.R
import com.pakscrap.android.navigation.Screen
import com.pakscrap.android.ui.commonViews.WhiteButton
import com.pakscrap.android.ui.screen.auth.AuthVM
import com.pakscrap.android.ui.theme.DarkBlue
import com.pakscrap.android.ui.theme.PSP_AndroidTheme
import com.pakscrap.android.ui.theme.boldFont
import com.pakscrap.android.ui.theme.regularFont
import es.dmoral.toasty.Toasty

@Composable
fun PrivacyPolicyScreen(navController: NavController, authVM: AuthVM) {
    val context = LocalContext.current

    PrivacyPolicyViews(onAgreeClick = { isSelected ->
        if (!isSelected) {
            Toasty.error(
                context,
                context.getString(R.string.agree_terms),
                Toasty.LENGTH_SHORT,
                false
            ).show()
            return@PrivacyPolicyViews
        }
        authVM.userPreferences.isTermsAccept = true
        navController.popBackStack()
        navController.navigate(Screen.LoginScreen.route)
    },
        onTermsClick = {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://pakscrap.com/privacypolicy.html"))
            context.startActivity(intent)
        })
}

@Composable
fun PrivacyPolicyViews(
    onAgreeClick: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
) {
    var isSelected by rememberSaveable { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.pak_scrap_copy), contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(horizontal = 20.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                CustomRadioButton(isSelected = isSelected, onClick = {
                    isSelected = !isSelected
                })

                Spacer(modifier = Modifier.width(8.dp))
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp, fontFamily = regularFont, color = Color.Black
                        )
                    ) {
                        append("By Continuing, you agree to PakScrapRate's ")
                    }

                    // Start annotation before adding the clickable text
                    pushStringAnnotation(tag = "terms", annotation = "terms")
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp, fontFamily = boldFont, color = Color.White
                        )
                    ) {
                        append(" terms & privacy policy")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "terms", start = offset, end = offset
                        ).firstOrNull()?.let {
                            onTermsClick()
                        }
                    },
                )

            }

            WhiteButton(
                modifier = Modifier.padding(vertical = 20.dp),
                btnName = stringResource(id = R.string.agree_continue),
                onButtonClick = { onAgreeClick(isSelected) }
            )
            Spacer(modifier = Modifier.padding(bottom = 40.dp))
        }
    }
}

@Composable
fun CustomRadioButton(isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.Green else Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
@Preview
fun PrivacyPolicyPreview() {
    PSP_AndroidTheme {
        PrivacyPolicyViews(onAgreeClick = {},
            onTermsClick = {})
    }
}
