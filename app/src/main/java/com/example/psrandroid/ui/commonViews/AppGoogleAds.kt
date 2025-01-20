package com.example.psrandroid.ui.commonViews

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.psp_android.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun GoogleAdBanner() {
    val adSize = AdSize.BANNER
    val testAdUnitId =
        stringResource(id = R.string.banner_ad_unit_id) // Replace with your actual Ad Unit ID
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

var interstitialAd: InterstitialAd? = null

@Composable
fun GoogleInterstitialAd(context: Context, onAdClick: () -> Unit) {
    val adUnitId =
        stringResource(id = R.string.interstitial_ad_unit_id) // Replace with your actual Ad Unit ID
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(
        context,
        adUnitId,
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                println("Ad successfully loaded.")
                // Attach listeners to the ad
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        onAdClick()
                        println("Ad was dismissed (canceled or closed).")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        println("Ad failed to show: ${adError.message}")
                    }

                    override fun onAdShowedFullScreenContent() {
                        println("Ad is showing.")
                        interstitialAd = null // Clear the reference after the ad is shown
                    }

                    override fun onAdClicked() {
                        onAdClick()
                        println("Ad was clicked.")
                    }
                }
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                interstitialAd = null
                println("Failed to load ad: ${error.message}")
            }
        }
    )
}

fun showInterstitialAd(context: Context) {
    if (interstitialAd != null) {
        interstitialAd?.show(context as Activity)
    } else {
        Log.d("TAG", "The interstitial ad wasn't ready yet.")
    }
}