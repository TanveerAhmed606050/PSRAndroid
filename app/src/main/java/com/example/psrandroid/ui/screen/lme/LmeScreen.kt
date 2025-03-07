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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psp_android.R
import com.example.psrandroid.response.LmeData
import com.example.psrandroid.response.mockup
import com.example.psrandroid.ui.commonViews.GoogleAdBanner
import com.example.psrandroid.ui.commonViews.LinearProgress
import com.example.psrandroid.ui.screen.adPost.SearchBar
import com.example.psrandroid.ui.screen.rate.NoProductView
import com.example.psrandroid.ui.screen.rate.RateVM
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightGreen40
import com.example.psrandroid.ui.theme.LightRed40
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.Utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeScreen(dashboardVM: RateVM) {
    var isRefreshing by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf(TextFieldValue("")) }
    if (!dashboardVM.isLoading)
        isRefreshing = false
    val lmeData = dashboardVM.searchLmeMetalData

    LaunchedEffect(Unit) {
        dashboardVM.getLMEMetals()
    }
    LmeScreenView(lmeData,
        search = search,
        onSearch = {
            search = it
            dashboardVM.searchLME(search.text)
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeScreenView(
    lmeData: List<LmeData>?,
    search: TextFieldValue,
    onSearch: (TextFieldValue) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.lme), fontSize = 16.sp,
                fontFamily = mediumFont,
                color = DarkBlue
            )
            if (lmeData == null) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                LinearProgress(modifier = Modifier.padding(vertical = 3.dp))
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            SearchBar(search,
                onSearchClick = {
                    onSearch(it)
                })
            Spacer(modifier = Modifier.padding(top = 20.dp))
            if (lmeData?.isNotEmpty() == true)
                LmeList(lmeData = lmeData)
            else
                NoProductView(msg = stringResource(id = R.string.no_lme), color = DarkBlue)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeList(lmeData: List<LmeData>?) {
    GoogleAdBanner()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
        contentPadding = PaddingValues(vertical = 0.dp)
    ) {

        items(lmeData?.size ?: 0) { index ->
            Spacer(modifier = Modifier.padding(4.dp))
            LmeItem(lmeData?.get(index) ?: LmeData.mockup)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LmeItem(lmeData: LmeData) {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = lmeData.name,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 1,
                fontFamily = mediumFont,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
            )
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rs. ${lmeData.price}",
                    modifier = Modifier.padding(horizontal = 2.dp),
                    fontSize = 14.sp,
                    fontFamily = mediumFont,
                    color = Color.DarkGray,
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(
                        if (lmeData.changeInRate.toFloat() < 0) LightRed40 else
                            LightGreen40,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = "${lmeData.changeInRate}%", modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    color = Color.White,
                    fontFamily = regularFont,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.padding(end = 8.dp))
            Text(
                text = "Expiry ${formatDate(lmeData.expiryDate)}",
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                fontSize = 12.sp,
                fontFamily = regularFont,
                color = Color.DarkGray,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun LmeScreenPreview() {
    PSP_AndroidTheme {
        LmeScreenView(listOf(),
            search = TextFieldValue(""),
            onSearch = {})
    }
}