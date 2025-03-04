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
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

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

fun loadRewardedAd(context: Context, adUnitId: String, onAdLoaded: (RewardedAd) -> Unit) {
    val adRequest = AdRequest.Builder().build()
    RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
        override fun onAdLoaded(ad: RewardedAd) {
            Log.d("lsjag", "onAdLoaded: ")
            onAdLoaded(ad)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            // Handle the error
            Log.d("lsjag", "onAdFailedToLoad: ${loadAdError.message}")
        }
    })
}

fun showRewardedAd(
    activity: Activity,
    rewardedAd: RewardedAd?,
    onAdClick: () -> Unit
) {
    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() {
            onAdClick()
            Log.d("RewardedAd", "Ad is shown in full screen.")
        }

        override fun onAdClicked() {
            onAdClick()
            Log.d("RewardedAd", "User clicked the rewarded ad.")
        }

        override fun onAdDismissedFullScreenContent() {
            onAdClick()
            Log.d("RewardedAd", "User closed the rewarded ad without completing it.")
            // You can reload a new ad here if needed
        }

    }

    rewardedAd?.show(activity) { rewardItem: RewardItem ->
        // This callback is invoked when the user earns the reward.
        Log.d("RewardedAd", "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
        // Here, you can update your app state to reflect the reward.
    }
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
        Log.d("dgjls", "The interstitial ad wasn't ready yet.")
    }
}