package com.pak.scrap.ui.screen.lme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pak.scrap.R
import com.pak.scrap.response.PrimeUserData
import com.pak.scrap.ui.commonViews.LinearProgress
import com.pak.scrap.ui.commonViews.MyAsyncImage
import com.pak.scrap.ui.commonViews.showRewardedAd
import com.pak.scrap.ui.screen.adPost.SearchBar
import com.pak.scrap.ui.screen.rate.NoProductView
import com.pak.scrap.ui.screen.rate.RateVM
import com.pak.scrap.ui.theme.AppBG
import com.pak.scrap.ui.theme.DarkBlue
import com.pak.scrap.ui.theme.LightRed40
import com.pak.scrap.ui.theme.PSP_AndroidTheme
import com.pak.scrap.ui.theme.mediumFont
import com.pak.scrap.utils.Constant
import com.pak.scrap.utils.Utils.isRtlLocale
import com.google.android.gms.ads.MobileAds
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrimeUserScreen(rateVm: RateVM) {
    val context = LocalContext.current
    var search by remember { mutableStateOf(TextFieldValue("")) }
    MobileAds.initialize(context) { initializationStatus ->
        Log.d("RewardedAd", "Mobile Ads initialized: $initializationStatus")
    }
    val primeUserData = rateVm.searchPrimeUserData

    LaunchedEffect(Unit) {
        rateVm.getPremiumUser()
    }
    PrimeUserScreen(rateVm.watchAd, primeUserData,
        onShowContact = { contact ->
            if (contact == context.getString(R.string.show_no))
                showRewardedAd(context as Activity, rewardedAd = rateVm.rewardedAd,
                    onAdClick = {
                        rateVm.watchAd = true
                    })
        },
        search = search,
        onSearch = {
            search = it
            rateVm.searchPrimeUser(search.text)
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrimeUserScreen(
    watchAd: Boolean, primeUserData: List<PrimeUserData>?,
    onShowContact: (String) -> Unit,
    search: TextFieldValue,
    onSearch: (TextFieldValue) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
            .padding(bottom = 120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = stringResource(id = R.string.prime_user),
                fontSize = 16.sp,
                fontFamily = mediumFont,
                color = DarkBlue
            )
            if (primeUserData == null) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                LinearProgress(modifier = Modifier.padding(vertical = 3.dp))
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            SearchBar(search,
                onSearchClick = {
                    onSearch(it)
                })
            Spacer(modifier = Modifier.padding(top = 10.dp))
            if (primeUserData?.isNotEmpty() == true) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                )
                {
                    items(primeUserData.size) { index ->
                        UserItemData(
                            watchAd = watchAd,
                            primeUserData[index],
                            onShowContact = { onShowContact(it) }
                        )
                    }

                }
            } else
                NoProductView(msg = stringResource(id = R.string.no_prime_user), color = DarkBlue)
        }
    }
}

@Composable
fun UserItemData(
    watchAd: Boolean, primeUserData: PrimeUserData,
    onShowContact: (String) -> Unit
) {
    val currentLocale = Locale.getDefault()
    isRtlLocale(currentLocale)
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 0.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MyAsyncImage(
                imageUrl = primeUserData.profileImage,
                45.dp,
                true
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 0.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(end = 8.dp, start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.name),
                    color = Color.DarkGray,
                    fontFamily = mediumFont,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.business_name),
                    fontSize = 14.sp,
                    fontFamily = mediumFont,
                    color = Color.DarkGray,
                )
                Text(
                    text = stringResource(id = R.string.address),
                    fontFamily = mediumFont,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.metals),
                    fontFamily = mediumFont,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                )
                Text(
                    text =
                    stringResource(id = R.string.show_no),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = mediumFont,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, start = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = primeUserData.name,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                    fontFamily = mediumFont,
                )
                Text(
                    text = primeUserData.businessName,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                    fontSize = 14.sp,
                    fontFamily = mediumFont,
                )
                Text(
                    text = primeUserData.location,
                    color = Color.DarkGray,
                    fontFamily = mediumFont,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                )
                Text(
                    text = primeUserData.type,
                    color = Color.DarkGray,
                    fontFamily = mediumFont,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                )

                Row(
                    modifier = Modifier
                        .background(
                            LightRed40,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp, 5.dp)
                ) {
                    Text(
                        text = if (watchAd) primeUserData.whatsapp else stringResource(id = R.string.watch_ad),
                        color = Color.White,
                        fontFamily = mediumFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis, // This will show "..." for truncated text
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            onShowContact(
                                if (watchAd) primeUserData.whatsapp else context.getString(R.string.show_no)
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PrimeUserScreenPreview() {
    PSP_AndroidTheme {
        PrimeUserScreen(false, primeUserData = listOf(),
            onShowContact = {},
            search = TextFieldValue(""),
            onSearch = {})
    }
}