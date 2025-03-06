package com.pak.scrap.ui.screen.adPost

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pak.scrap.R
import com.pak.scrap.ui.commonViews.FullScreenImageDialog
import com.pak.scrap.ui.commonViews.showRewardedAd
import com.pak.scrap.ui.screen.adPost.models.AdsData
import com.pak.scrap.ui.screen.adPost.models.mockup
import com.pak.scrap.ui.screen.rate.RateVM
import com.pak.scrap.ui.theme.AppBG
import com.pak.scrap.ui.theme.DarkBlue
import com.pak.scrap.ui.theme.LightBlue
import com.pak.scrap.ui.theme.PSP_AndroidTheme
import com.pak.scrap.ui.theme.boldFont
import com.pak.scrap.ui.theme.mediumFont
import com.pak.scrap.ui.theme.regularFont
import com.pak.scrap.utils.Constant
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import es.dmoral.toasty.Toasty

@Composable
fun DetailAdScreen(
    navController: NavController,
    rateVM: RateVM,
    adData: AdsData?,
    isMyAd: Boolean?
) {
    val context = LocalContext.current
    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, make the call
            openCallApp(context, adData?.description ?: "")
        } else {
            Toasty.error(
                context,
                context.getString(R.string.call_permission_error),
                Toast.LENGTH_SHORT,
                false
            ).show()
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog)
        FullScreenImageDialog(
            serverImageList = adData?.photos ?: listOf(), onDismissRequest = {
                showDialog = false
            },
            imageList = listOf()
        )
    DetailAdScreenViews(
        watchAd = rateVM.watchAd,
        adsData = adData ?: AdsData.mockup,
        isMyAd = isMyAd ?: false, backClick = {
            navController.popBackStack()
        },
        onShowNoClick = { number ->
            if (!rateVM.watchAd) {
                showRewardedAd(context as Activity, rewardedAd = rateVM.rewardedAd,
                    onAdClick = {
                        rateVM.watchAd = true
                    })
            } else {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    openCallApp(context, number)
                } else {
                    callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            }
        },
        onSMSClick = {
            if (!rateVM.watchAd)
                showRewardedAd(context as Activity, rewardedAd = rateVM.rewardedAd!!,
                    onAdClick = {})
            else
                openSMSApp(context, phoneNumber = adData?.description ?: "", message = "")
        },
        onWhatsAppCallClick = {
            if (!rateVM.watchAd)
                showRewardedAd(context as Activity, rewardedAd = rateVM.rewardedAd!!,
                    onAdClick = {})
            else
                openWhatsApp(context, adData?.phoneNumber ?: "")
        },
        onImageClick = {
            showDialog = true
        }
    )
}

@Composable
fun DetailAdScreenViews(
    watchAd: Boolean,
    adsData: AdsData,
    isMyAd: Boolean,
    backClick: () -> Unit,
    onShowNoClick: (String) -> Unit,
    onSMSClick: () -> Unit,
    onWhatsAppCallClick: () -> Unit,
    onImageClick: () -> Unit,
) {
    val pagerState = rememberPagerState()
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
                .fillMaxHeight()
        ) {
            HorizontalPager(
                count = adsData.photos.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .height(400.dp)
            ) { page ->
                val uri = if (adsData.photos.isNotEmpty()) Constant.MEDIA_BASE_URL + adsData.photos[page] else ""
                Box(modifier = Modifier) {
                    AsyncImage(
                        model = uri, contentDescription = "",
                        error = painterResource(id = R.drawable.demo_scrap),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clickable { onImageClick() },
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)  // Align to bottom left
                    .padding(start = 10.dp, bottom = 10.dp) // Adjust padding
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_photo_24),
                    contentDescription = "Gallery",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = adsData.photos.size.toString(),
                    fontSize = 14.sp,
                    fontFamily = boldFont,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // This will show "..." for truncated text
                )
            }

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
                text = "Rs. ${adsData.price}",
                color = Color.DarkGray, fontSize = 14.sp,
                fontFamily = mediumFont,
            )
            Text(
                text = adsData.city,
                color = Color.DarkGray, fontSize = 14.sp,
                fontFamily = regularFont,
            )

            if (!isMyAd) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(DarkBlue, RoundedCornerShape(10.dp))
                        .clickable { onShowNoClick(adsData.phoneNumber) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_phone_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = if (watchAd) adsData.phoneNumber else
                            stringResource(id = R.string.show_no),
                        fontSize = 16.sp,
                        fontFamily = regularFont,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            if (watchAd) {
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
                false
            )
                .show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toasty.error(
            context,
            context.getText(R.string.open_whatsApp_error),
            Toast.LENGTH_SHORT,
            false
        ).show()
    }
}

fun openSMSApp(context: Context, phoneNumber: String, message: String) {
    val smsUri = Uri.parse("smsto:$phoneNumber")
    val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
        putExtra("sms_body", message)
    }
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        context.startActivity(intent)
    } else {
        // Request permission
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(Manifest.permission.SEND_SMS), 1122
        )
    }
}

fun openCallApp(context: Context, phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        context.startActivity(callIntent)
    } else {
        // Request permission
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1223
        )
    }

}

@Preview
@Composable
fun DetailAdScreenPreview() {
    PSP_AndroidTheme {
        DetailAdScreenViews(
            adsData = AdsData.mockup, backClick = {},
            onShowNoClick = {},
            watchAd = false,
            onSMSClick = {},
            onWhatsAppCallClick = {},
            onImageClick = {},
            isMyAd = true
        )
    }
}