package com.example.psrandroid.ui.screen.lme

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.response.LmeData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.GoogleAdBanner
import com.example.psrandroid.ui.commonViews.LinearProgress
import com.example.psrandroid.ui.screen.rate.RateVM
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.formatDateDisplay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeScreen(navController: NavController, dashboardVM: RateVM) {
    var isRefreshing by remember { mutableStateOf(false) }
    if (!dashboardVM.isLoading)
        isRefreshing = false
    val lmeData = dashboardVM.lmeMetalData

    LaunchedEffect(Unit) {
        dashboardVM.getLMEMetals()
    }
    LmeScreenView(lmeData?.data)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeScreenView(lmeData: List<LmeData>?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBlue, DarkBlue))),
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
            if (lmeData == null) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                LinearProgress(modifier = Modifier.padding(vertical = 5.dp))
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            LmeList(lmeData = lmeData)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeList(lmeData: List<LmeData>?) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(lmeData?.size ?: 0) { index ->
            if (index % 3 == 0)
                GoogleAdBanner()
            else
                LmeItem(lmeData?.get(index) ?: LmeData.mockup)
            HorizontalDivider()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeItem(lmeData: LmeData) {

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
                text = lmeData.name,
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
                    text = "Rs. ${lmeData.price}",
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
            Row(
                modifier = Modifier
                    .background(
                        if (lmeData.changeInRate.toFloat() < 0) Color.Red else
                            Color.Green,
                        RoundedCornerShape(10.dp)
                    )
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
//                    contentDescription = "",
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
                Text(
                    text = "${lmeData.changeInRate}%", modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    color = Color.White,
                    fontFamily = boldFont,
                    fontSize = 14.sp
                )
            }
            Text(
                text = "Expire ${formatDateDisplay(lmeData.expiryDate)}",
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun LmeScreenPreview() {
    PSP_AndroidTheme {
        LmeScreenView(listOf())
    }
}