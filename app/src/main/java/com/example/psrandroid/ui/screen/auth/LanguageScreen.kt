package com.example.psrandroid.ui.screen.auth

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.navigation.Screen
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.screen.home.HomeVM
import com.example.psrandroid.ui.screen.profile.switchLanguage
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme

@Composable
fun LanguageScreen(
    navController: NavController,
    homeVM: HomeVM,
) {
    val context = LocalContext.current
    var language by remember { mutableStateOf(context.getText(R.string.language)) }
    LanguageScreen(language.toString(),
        selectedLanguage = { selectedLanguage ->
            language = selectedLanguage
            if (language == "English") {
                switchLanguage(context, "en")
                homeVM.userPreferences.isUrduSelected = false
            } else {
                switchLanguage(context, "ur")
                homeVM.userPreferences.isUrduSelected = true
            }
        },
        nextClick = {
            navController.popBackStack()
            navController.navigate(Screen.LoginScreen.route)
        },
        backClick = {
            navController.popBackStack()
        })
}

@Composable
fun LanguageScreen(
    language: String,
    selectedLanguage: (String) -> Unit,
    nextClick: () -> Unit,
    backClick: () -> Unit,
) {
    val languageList = listOf("اردو", "English")
    var expandedLang by remember { mutableStateOf(false) }
    if (expandedLang) {
        ListDialog(dataList = languageList, onDismiss = { expandedLang = false },
            onConfirm = {
                selectedLanguage(it)
                expandedLang = false
            })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Header(
                modifier = null,
                stringResource(id = R.string.enter_lang),
                backClick = { backClick() })
            Spacer(modifier = Modifier.height(100.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                HorizontalDivider(color = DarkBlue, thickness = 2.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedLang = true }
                        .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.globe_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp),
                        colorFilter = ColorFilter.tint(DarkBlue)
                    )
                    Text(
                        text = language,
                        color = DarkBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 0.dp),
                        colorFilter = ColorFilter.tint(DarkBlue)
                    )
                }
                HorizontalDivider(color = DarkBlue, thickness = 2.dp)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable { nextClick() }
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

@Preview
@Composable
fun PreviewLanguageScreen() {
    PSP_AndroidTheme {
        LanguageScreen(language = "Language", selectedLanguage = {}, nextClick = {},
            backClick = {})
    }
}
