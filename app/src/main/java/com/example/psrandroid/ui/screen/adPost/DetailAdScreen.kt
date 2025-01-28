package com.example.psrandroid.ui.screen.adPost

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.psp_android.R
import com.example.psrandroid.ui.commonViews.FullScreenImageDialog
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
import es.dmoral.toasty.Toasty

@Composable
fun DetailAdScreen(
    navController: NavController,
    homeVM: AdPostVM,
    adData: AdData?
) {
    val context = LocalContext.current
    var showContact by remember { mutableStateOf(context.getText(R.string.show_no)) }
    GoogleInterstitialAd(context = context, onAdClick = {
        showContact = "+923456982288"
    })
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog)
        FullScreenImageDialog(
            serverImageList = null, onDismissRequest = {
                showDialog = false
            },
            imageList = listOf()
        )
    DetailAdScreenViews(
        showContact = showContact.toString(),
        adsData = adData ?: AdData.mockup, backClick = {
            navController.popBackStack()
        },
        onShowNoClick = { number ->
            if (showContact == context.getText(R.string.show_no))
                showInterstitialAd(context)
            else
                openCallApp(context, number)
        },
        onSMSClick = {
            if (showContact == context.getText(R.string.show_no))
                showInterstitialAd(context)
            else
                openSMSApp(context, phoneNumber = showContact.toString(), message = "")
        },
        onWhatsAppCallClick = {
            if (showContact == context.getText(R.string.show_no))
                showInterstitialAd(context)
            else
                openWhatsApp(context, showContact.toString())
        },
        onWhatsAppChatClick = {
            if (showContact == context.getText(R.string.show_no))
                showInterstitialAd(context)
            else
                openWhatsApp(context, showContact.toString())

        },
        onImageClick = {
            showDialog = true
        }
    )
}

@Composable
fun DetailAdScreenViews(
    showContact: String,
    adsData: AdData,
    backClick: () -> Unit,
    onShowNoClick: (String) -> Unit,
    onSMSClick: () -> Unit,
    onWhatsAppChatClick: () -> Unit,
    onWhatsAppCallClick: () -> Unit,
    onImageClick: () -> Unit,
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
                    .height(400.dp)
                    .clickable { onImageClick() },
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color.DarkGray.copy(0.6f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(32.dp) // Set the size for the background container
                    .padding(8.dp)
                    .clickable { backClick() },
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
                color = Color.DarkGray, fontSize = 14.sp,
                fontFamily = mediumFont,
            )
            Text(
                text = adsData.city,
                color = Color.DarkGray, fontSize = 14.sp,
                fontFamily = regularFont,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(DarkBlue, RoundedCornerShape(10.dp))
                    .clickable { onShowNoClick(showContact) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_phone_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = showContact,
                    fontSize = 16.sp,
                    fontFamily = regularFont,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (showContact != stringResource(id = R.string.show_no)) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onSMSClick() }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sms_ic),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.sms),
                            fontSize = 14.sp, fontFamily = regularFont,
                            modifier = Modifier.padding(start = 4.dp),
                            color = DarkBlue
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onWhatsAppChatClick() }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.chat_ic),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.chat),
                            fontSize = 14.sp, fontFamily = regularFont,
                            modifier = Modifier.padding(start = 4.dp),
                            color = DarkBlue
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onWhatsAppCallClick() }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.whatsapp_ic),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.whats_app),
                            fontSize = 14.sp, fontFamily = regularFont,
                            modifier = Modifier.padding(start = 4.dp),
                            color = DarkBlue
                        )
                    }
                }
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
            Toasty.error(
                context,
                context.getText(R.string.no_whatsApp_error),
                Toast.LENGTH_SHORT,
                true
            )
                .show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toasty.error(
            context,
            context.getText(R.string.open_whatsApp_error),
            Toast.LENGTH_SHORT,
            true
        )
            .show()
    }
}

fun openSMSApp(context: Context, phoneNumber: String, message: String) {
    val smsUri = Uri.parse("smsto:$phoneNumber")
    val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
        putExtra("sms_body", message)
    }

    // Check if there's an app to handle the intent
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toasty.error(context, context.getText(R.string.no_sms_error), Toast.LENGTH_SHORT, true)
            .show()
    }
}

fun openCallApp(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }

    // Start the phone dialer
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toasty.error(context, context.getText(R.string.no_call_app), Toast.LENGTH_SHORT, true)
            .show()
    }
}

@Preview
@Composable
fun DetailAdScreenPreview() {
    PSP_AndroidTheme {
        DetailAdScreenViews(
            adsData = AdData.mockup, backClick = {},
            onShowNoClick = {},
            showContact = "",
            onSMSClick = {},
            onWhatsAppCallClick = {},
            onWhatsAppChatClick = {},
            onImageClick = {}
        )
    }
}