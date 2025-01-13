package com.example.psrandroid.ui.commonViews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.psp_android.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun GoogleAdBanner() {
    val adSize = AdSize.BANNER
    val testAdUnitId = stringResource(id = R.string.banner_ad_unit_id) // Replace with your actual Ad Unit ID
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = testAdUnitId // Replace with your actual Ad Unit ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )

}