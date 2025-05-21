package com.pakscrap.android.ui.screen.lme

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.MobileAds
import com.pakscrap.R
import com.pakscrap.android.response.PrimeUserData
import com.pakscrap.android.ui.commonViews.AppBlueButton
import com.pakscrap.android.ui.commonViews.LinearProgress
import com.pakscrap.android.ui.commonViews.MyAsyncImage
import com.pakscrap.android.ui.commonViews.loadRewardedAd
import com.pakscrap.android.ui.commonViews.showRewardedAd
import com.pakscrap.android.ui.screen.adPost.SearchBar
import com.pakscrap.android.ui.screen.adPost.openCallApp
import com.pakscrap.android.ui.screen.adPost.openWhatsApp
import com.pakscrap.android.ui.screen.rate.NoProductView
import com.pakscrap.android.ui.screen.rate.RateVM
import com.pakscrap.android.ui.theme.AppBG
import com.pakscrap.android.ui.theme.DarkBlue
import com.pakscrap.android.ui.theme.LightRed40
import com.pakscrap.android.ui.theme.PSP_AndroidTheme
import com.pakscrap.android.ui.theme.boldFont
import com.pakscrap.android.ui.theme.regularFont
import com.pakscrap.android.utils.Utils.isRtlLocale
import es.dmoral.toasty.Toasty
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrimeUserScreen(rateVm: RateVM) {
    val context = LocalContext.current
    var searchUser by remember { mutableStateOf(TextFieldValue("")) }
    MobileAds.initialize(context)
    loadRewardedAd(context = context,
        context.getString(R.string.rewarded_ad_unit_id),
        onAdLoaded = {
            rateVm.rewardedAd = it
        })
    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toasty.error(
                context,
                context.getString(R.string.call_permission_error),
                Toast.LENGTH_SHORT,
                false
            ).show()
        }
    }

    val primeUserData = rateVm.searchPrimeUserData

    LaunchedEffect(Unit) {
        rateVm.getPremiumUser()
    }
    PrimeUserScreen(primeUserData, onShowContact = { userData ->
        if (!userData.watchAd) {
            if (rateVm.rewardedAd != null) {
                showRewardedAd(context as Activity, rewardedAd = rateVm.rewardedAd!!, onAdClick = {
                    rateVm.updatePrimeUser(userData)
                })
            } else {
                Log.d("lsjag", "Ad not loaded yet")
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCallApp(context, userData.whatsapp)
            } else {
                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }, search = searchUser, onSearch = {
        searchUser = it
        rateVm.searchPrimeUser(searchUser.text)
    }, onBecomePremiumClick = {
        openWhatsApp(context, "+923244400343")
    })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrimeUserScreen(
    primeUserData: List<PrimeUserData>?,
    onShowContact: (PrimeUserData) -> Unit,
    search: TextFieldValue,
    onSearch: (TextFieldValue) -> Unit,
    onBecomePremiumClick: () -> Unit,
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
                fontFamily = boldFont,
                color = DarkBlue
            )
            if (primeUserData == null) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                LinearProgress(modifier = Modifier.padding(vertical = 3.dp))
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            SearchBar(search, onSearchClick = {
                onSearch(it)
            })
            Spacer(modifier = Modifier.padding(top = 10.dp))
            AppBlueButton(modifier = Modifier,
                text = stringResource(id = R.string.become_premium_user),
                onButtonClick = {
                    onBecomePremiumClick()
                })
            if (primeUserData?.isNotEmpty() == true) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                ) {
                    items(primeUserData.size) { index ->
                        UserItemData(primeUserData[index], onShowContact = { onShowContact(it) })
                    }

                }
            } else NoProductView(
                msg = stringResource(id = R.string.no_prime_user),
                color = DarkBlue
            )
        }
    }
}

@Composable
fun UserItemData(
    primeUserData: PrimeUserData, onShowContact: (PrimeUserData) -> Unit
) {
    val currentLocale = Locale.getDefault()
    isRtlLocale(currentLocale)
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
                imageUrl = primeUserData.profileImage, 45.dp, true
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
                    fontFamily = regularFont,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.business_name),
                    fontSize = 14.sp,
                    fontFamily = regularFont,
                    color = Color.DarkGray,
                )
                Text(
                    text = stringResource(id = R.string.address),
                    fontFamily = regularFont,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.metals),
                    fontFamily = regularFont,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.show_no),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = regularFont,
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
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = boldFont,
                )
                Text(
                    text = primeUserData.businessName,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontFamily = boldFont,
                )
                Text(
                    text = primeUserData.location,
                    color = Color.DarkGray,
                    fontFamily = boldFont,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = primeUserData.type,
                    color = Color.DarkGray,
                    fontFamily = boldFont,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    modifier = Modifier
                        .background(
                            LightRed40, RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp, 5.dp)
                ) {
                    Text(text = if (primeUserData.watchAd) primeUserData.whatsapp else stringResource(
                        id = R.string.watch_ad
                    ),
                        color = Color.White,
                        fontFamily = boldFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            onShowContact(primeUserData)
                        })
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = primeUserData.businessDetails,
                    color = Color.DarkGray,
                    fontFamily = boldFont,
                    fontSize = 14.sp,
                    maxLines = 3)
            }

        }
        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            text = primeUserData.businessDetails,
            color = Color.DarkGray,
            fontFamily = regularFont,
            fontSize = 14.sp,
            maxLines = 3)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PrimeUserScreenPreview() {
    PSP_AndroidTheme {
        PrimeUserScreen(primeUserData = listOf(),
            onShowContact = {},
            search = TextFieldValue(""),
            onSearch = {},
            onBecomePremiumClick = {})
    }
}