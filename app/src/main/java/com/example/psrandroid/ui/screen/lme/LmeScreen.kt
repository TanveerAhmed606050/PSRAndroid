package com.example.psrandroid.ui.screen.lme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.response.SubMetalData
import com.example.psrandroid.ui.screen.dashboard.ProductItem
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont

@Composable
fun LmeScreen(navController: NavController) {
    LmeScreenView()
}

@Composable
fun LmeScreenView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue))),
        Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.lme), fontSize = 16.sp,
                fontFamily = mediumFont,
                color = Color.White
            )
        }
        Text(
            text = "Coming Soon", color = colorResource(id = R.color.white),
            fontFamily = boldFont,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun LmeList(dashboardData: List<SubMetalData>?) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(dashboardData?.size ?: 0) { index ->
            LmeItem()
            HorizontalDivider()
        }
    }
}
@Preview
@Composable
fun LmeItem() {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Iron",
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                fontFamily = regularFont,
                modifier = Modifier.weight(2f),
                overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
            )
            Box(
                modifier = Modifier
                    .background(DarkBlue, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rs.",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 14.sp,
                    fontFamily = regularFont,
                    color = Color.White,
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "-1.8%", modifier = Modifier
                    .background(Color.Red, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                color = Color.White,
                fontFamily = boldFont,
                fontSize = 14.sp
            )
            Text(
                text = "Expire 07 FEB 2025",
                modifier = Modifier
                    .background(DarkBlue, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                fontSize = 14.sp,
                fontFamily = regularFont,
                color = Color.White,
            )
        }
    }
}

//@Preview
@Composable
fun LmeScreenPreview() {
    PSP_AndroidTheme {
        LmeScreenView()
    }
}