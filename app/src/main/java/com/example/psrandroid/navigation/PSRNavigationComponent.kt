package com.example.psrandroid.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import com.example.psp_android.R
import com.example.psrandroid.ui.theme.DarkBlue
import com.example.psrandroid.ui.theme.regularFont
import com.example.psrandroid.utils.PSRNavigationContentPosition
import kotlinx.coroutines.launch

@Composable
fun PermanentNavigationDrawerContent(
    bottomMenuList: List<BottomNavigationItem>,
    selectedDestination: String,
    navigationContentPosition: PSRNavigationContentPosition,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit,
    onLogOut: () -> Unit
) {
    //Drawer which show on Tablet or large device on landscape mode
    PermanentDrawerSheet(
        modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 250.dp),
        drawerContainerColor = colorResource(id = R.color.white),
    ) {
        Layout(
            modifier = Modifier.padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkBlue
                    )
                    ExtendedFloatingActionButton(
                        onClick = { onLogOut() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        containerColor = Color.White,
                        contentColor = DarkBlue
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.logout),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    bottomMenuList.forEach { psrDestination ->
                        NavigationDrawerItem(
                            selected = selectedDestination == psrDestination.route,
                            label = {
                                Text(
                                    text = psrDestination.name,
                                    color = colorResource(id = R.color.black),
                                    fontFamily = regularFont,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = psrDestination.icon),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color.White,
                                unselectedContainerColor = DarkBlue
                            ),
                            onClick = { navigateToTopLevelDestination(psrDestination) }
                        )
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition)
        )
    }
}

fun navigationMeasurePolicy(
    navigationContentPosition: PSRNavigationContentPosition,
): MeasurePolicy {
    return MeasurePolicy { measure, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measure.forEach {
            when (it.layoutId) {
                LayoutType.HEADER -> headerMeasurable = it
                LayoutType.CONTENT -> contentMeasurable = it
                else -> error("Unknown layoutId encountered!")
            }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
            constraints.offset(vertical = -headerPlaceable.height)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place the header, this goes at the top
            headerPlaceable.placeRelative(0, 0)

            // Determine how much space is not taken up by the content
            val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

            val contentPlaceableY = when (navigationContentPosition) {
                // Figure out the place we want to place the content, with respect to the
                // parent (ignoring the header for now)
                PSRNavigationContentPosition.TOP -> 0
                PSRNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
            }
                // And finally, make sure we don't overlap with the header.
                .coerceAtLeast(headerPlaceable.height)

            contentPlaceable.placeRelative(0, contentPlaceableY)
        }
    }
}

@Composable
fun PSRNavigationRail(
    bottomMenuList: List<BottomNavigationItem>,
    selectedDestination: String,
    navigationContentPosition: PSRNavigationContentPosition,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit,
    onDrawerClicked: () -> Unit = {},
    onLogOut: () -> Unit
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = colorResource(id = R.color.white),
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        Column(
            modifier = Modifier.layoutId(LayoutType.HEADER),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NavigationRailItem(
                selected = false,
                onClick = onDrawerClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = ""
                    )
                }
            )
            FloatingActionButton(
                onClick = { onLogOut() },
                modifier = Modifier.padding(vertical = 10.dp),
                containerColor = Color.White,
                contentColor = DarkBlue
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "",
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.height(8.dp)) // NavigationRailHeaderPadding
            Spacer(Modifier.height(4.dp)) // NavigationRailVerticalPadding
        }

        Column(
            modifier = Modifier.layoutId(LayoutType.CONTENT),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            bottomMenuList.forEach { psrDestination ->
                NavigationRailItem(
                    selected = selectedDestination == psrDestination.route,
                    onClick = { navigateToTopLevelDestination(psrDestination) },
                    icon = {
                        Icon(
                            painter = painterResource(id = psrDestination.icon),
                            contentDescription = ""
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun PSRBottomNavigationBar(
    bottomMenuList: List<BottomNavigationItem>,
    selectedDestination: String,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colorResource(id = R.color.white),
        contentColor = DarkBlue
    ) {
        bottomMenuList.forEach { fitmeBottomDestination ->
            NavigationBarItem(
                selected = selectedDestination == fitmeBottomDestination.route,
                onClick = { navigateToTopLevelDestination(fitmeBottomDestination) },
                icon = {
                    Icon(
                        painter = painterResource(id = fitmeBottomDestination.icon),
                        contentDescription = "",
                    )
                },
                label = {
                    Text(
                        text = fitmeBottomDestination.name,
                        fontSize =  9.sp,
                        fontFamily = regularFont,
                        lineHeight = 2.sp,
                        textAlign = TextAlign.Center
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIndicatorColor = Color.Transparent,
                    selectedIconColor = DarkBlue,
                    selectedTextColor = DarkBlue,
                    unselectedIconColor = colorResource(id = R.color.text_grey),
                    unselectedTextColor = colorResource(id = R.color.text_grey),
                    disabledIconColor = colorResource(id = R.color.text_grey),
                    disabledTextColor = colorResource(id = R.color.text_grey)
                )
            )
        }
    }
}

@Composable
fun ModalNavigationDrawerContent(
    bottomMenuList: List<BottomNavigationItem>,
    selectedDestination: String,
    navigationContentPosition: PSRNavigationContentPosition,
    navigateToTopLevelDestination: (BottomNavigationItem) -> Unit,
    onDrawerClicked: () -> Unit = {},
    onLogOut: () -> Unit
) {
    ModalDrawerSheet(
        drawerContentColor = Color.White,
        drawerContainerColor = DarkBlue,
        modifier = Modifier
            .wrapContentWidth() // Wrap the width of the drawer to its content
            .widthIn(
                min = 200.dp,
                max = 250.dp
            ) // Optionally limit the drawer's width within a range
        ,
    ) {
        Layout(
            modifier = Modifier.padding(16.dp),
            content = {
                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.HEADER)
                        .wrapContentWidth() // Wrap the width of the drawer to its content
                        .widthIn(
                            min = 200.dp,
                            max = 250.dp
                        ) // Optionally limit the drawer's width within a range
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PSR",
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.white)
                        )
                        IconButton(onClick = onDrawerClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }

                    ExtendedFloatingActionButton(
                        onClick = { onLogOut() },
                        modifier = Modifier
//                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 40.dp),
                        containerColor = Color.White,
                        contentColor = DarkBlue
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.logout),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .wrapContentWidth() // Wrap the width of the drawer to its content
                        .widthIn(
                            min = 200.dp,
                            max = 250.dp
                        ) // Optionally limit the drawer's width within a range
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    bottomMenuList.forEach { psrDestination ->
                        NavigationDrawerItem(
                            selected = selectedDestination == psrDestination.route,
                            label = {
                                Text(
                                    text = psrDestination.name,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = if (selectedDestination == psrDestination.route) DarkBlue else Color.White // Set text color conditionally
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = psrDestination.icon),
                                    contentDescription = "",
                                    tint = if (selectedDestination == psrDestination.route) DarkBlue else Color.White // Set icon color conditionally
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color.White, // Set blue background when selected
                                unselectedContainerColor = Color.Transparent, // Transparent when not selected
                                selectedTextColor = DarkBlue, // White text color when selected
                                unselectedTextColor = Color.White // Black text when unselected
                            ),
                            onClick = {
                                navigateToTopLevelDestination(psrDestination)
                            }
                        )
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition)
        )
    }
}

enum class LayoutType {
    HEADER, CONTENT
}