package com.example.psrandroid.ui.screen.adPost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.ui.commonViews.Header
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme

@Composable
fun DetailAdScreen(navController: NavController, homeVM: AdPostVM) {
    DetailAdScreenViews (backClick = {
        navController.popBackStack()
    })
}

@Composable
fun DetailAdScreenViews(backClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Header(
                modifier = null,
                stringResource(id = R.string.ad_detail),
                backClick = { backClick() })

            Spacer(modifier = Modifier.padding(top = 20.dp))
        }
    }
}

@Preview
@Composable
fun DetailAdScreenPreview() {
    PSP_AndroidTheme {
        DetailAdScreenViews(backClick = {})
    }
}