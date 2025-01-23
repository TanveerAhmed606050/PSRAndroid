package com.example.psrandroid.ui.commonViews

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.AppBG
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.mediumFont
import com.example.psrandroid.ui.theme.regularFont
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun LogoutDialog(onDismissRequest: () -> Unit, onOkClick: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .background(AppBG, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.logout),
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp,
                    ),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = mediumFont,
                    color = DarkBlue,
                )
                Text(
                    text = stringResource(id = R.string.account_logout_warning),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    fontSize = 14.sp,
                    fontFamily = regularFont,
                    color = DarkBlue,
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.White)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismissRequest, modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(id = R.string.cancel),
                            fontSize = 14.sp,
                            fontFamily = regularFont,
                            color = DarkBlue
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .width(0.5.dp)
                            .height(50.dp)
                            .background(Color.White)
                    )
                    TextButton(
                        onClick = onOkClick, modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(id = R.string.logout),
                            color = DarkBlue,
                            fontSize = 16.sp,
                            fontFamily = mediumFont
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(AppBG, shape = RoundedCornerShape(10.dp))
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.demo_scrap), contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.loading), color = Color.DarkGray,
                    fontFamily = regularFont, fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun FullScreenImageDialog(
    onDismissRequest: () -> Unit,
    imageList: List<Uri>,
) {
    val pagerState = rememberPagerState()
    Dialog(
        onDismissRequest = {onDismissRequest()}, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp)
                .background(AppBG, shape = RoundedCornerShape(10.dp))
        ) {
            HorizontalPager(
                count = imageList.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val uri = imageList[page]
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image $page",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { },
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}