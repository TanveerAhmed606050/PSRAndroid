package com.example.psrandroid.ui.screen.adPost

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.ui.commonViews.GoogleInterstitialAd
import com.example.psrandroid.ui.commonViews.showInterstitialAd
import com.example.psrandroid.ui.screen.adPost.models.AdData
import com.example.psrandroid.ui.screen.adPost.models.mockup
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.LightBlue
import com.example.psrandroid.ui.theme.PSP_AndroidTheme
import com.example.psrandroid.ui.theme.boldFont
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont

@Composable
fun DetailAdScreen(
    navController: NavController,
    homeVM: AdPostVM,
    adData: AdData?
) {
    val context = LocalContext.current
    var showContact by remember { mutableStateOf("Show Number") }
    GoogleInterstitialAd(context = context, onAdClick = {
        showContact = "+923456982288"
    })
    DetailAdScreenViews(
        showContact = showContact,
        adsData = adData ?: AdData.mockup, backClick = {
            navController.popBackStack()
        },
        onShowNoClick = { number ->
            if (showContact == "Show Number")
                showInterstitialAd(context)
            else
                openWhatsApp(context, number)
        })
}

@Composable
fun DetailAdScreenViews(
    showContact: String,
    adsData: AdData,
    backClick: () -> Unit,
    onShowNoClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.demo_scrap),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 24.dp, start = 12.dp)
                    .size(30.dp)
                    .padding(4.dp)
                    .clickable { backClick() },
                colorFilter = ColorFilter.tint(DarkBlue)
            )
        }
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = adsData.name,
                color = LightBlue, fontSize = 16.sp,
                fontFamily = boldFont,
            )
            Text(
                text = adsData.metalName,
                color = Color.Gray, fontSize = 14.sp,
                fontFamily = regularFont,
            )
            Text(
                text = adsData.price,
                color = Color.Black, fontSize = 14.sp,
                fontFamily = mediumFont,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .clickable { onShowNoClick(showContact) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_phone_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(LightBlue)
                )
                Text(
                    text = showContact,
                    fontSize = 16.sp,
                    fontFamily = regularFont,
                    color = LightBlue,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = adsData.description,
                color = Color.Gray, fontSize = 14.sp,
                fontFamily = mediumFont,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

fun openWhatsApp(context: Context, phoneNumber: String) {
    try {
        // Format the phone number
        val formattedNumber = phoneNumber.removePrefix("+").replace(" ", "")
        val url = "https://wa.me/$formattedNumber"

        // Create the intent
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        // Check if WhatsApp can handle the intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to open WhatsApp.", Toast.LENGTH_LONG).show()
    }
}

@Preview
@Composable
fun DetailAdScreenPreview() {
    PSP_AndroidTheme {
        DetailAdScreenViews(
            adsData = AdData.mockup, backClick = {},
            onShowNoClick = {},
            showContact = ""
        )
    }
}