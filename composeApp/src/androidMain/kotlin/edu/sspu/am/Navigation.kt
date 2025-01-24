package edu.sspu.am

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AndroidNavigationBar(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val screen by ui.screen.collectAsState()
    val first by ui.settings.first.current.collectAsState()
    val font by ui.font.collectAsState()

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onLongClick = {
                            if (ui.settings.developerMode.enable.value) {
                                when (ui.settings.developerMode.fastJumper.one.mode.current.value) {
                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Screen ->
                                        ui goto ui.settings.developerMode.fastJumper.one.target.current.value

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.WebUrl ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.TinySoftware ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Command ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }
                                }
                            } else {
                                ui goto RootScreens.List
                            }
                        }
                    ) { ui goto RootScreens.List },
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = if (ui.childMatch(RootScreens.List))
                            Icons.AutoMirrored.Filled.ListAlt
                        else
                            Icons.AutoMirrored.Outlined.ListAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = if (ui.childMatch(RootScreens.List))
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "列表",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font,
                        lineHeight = 16.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onLongClick = {
                            if (ui.settings.developerMode.enable.value) {
                                when (ui.settings.developerMode.fastJumper.two.mode.current.value) {
                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Screen ->
                                        ui goto ui.settings.developerMode.fastJumper.two.target.current.value

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.WebUrl ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.TinySoftware ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Command ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }
                                }
                            } else {
                                ui goto RootScreens.Cloud
                            }
                        }
                    ) { ui goto RootScreens.Cloud },
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = if (ui.childMatch(RootScreens.Cloud))
                            Icons.Filled.Cloud
                        else
                            Icons.Outlined.Cloud,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = if (ui.childMatch(RootScreens.Cloud))
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "云端",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font,
                        lineHeight = 16.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onLongClick = {
                            if (ui.settings.developerMode.enable.value) {
                                when (ui.settings.developerMode.fastJumper.three.mode.current.value) {
                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Screen ->
                                        ui goto ui.settings.developerMode.fastJumper.three.target.current.value

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.WebUrl ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.TinySoftware ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Command ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }
                                }
                            } else {
                                ui goto RootScreens.Community
                            }
                        }
                    ) { ui goto RootScreens.Community },
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = if (ui.childMatch(RootScreens.Community))
                            Icons.Filled.OfflineBolt
                        else
                            Icons.Outlined.OfflineBolt,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = if (ui.childMatch(RootScreens.Community))
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "社区",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font,
                        lineHeight = 16.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onLongClick = {
                            if (ui.settings.developerMode.enable.value) {
                                when (ui.settings.developerMode.fastJumper.four.mode.current.value) {
                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Screen ->
                                        ui goto ui.settings.developerMode.fastJumper.four.target.current.value

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.WebUrl ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.TinySoftware ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }

                                    UI.Settings.DeveloperMode.FastJumper.FastJumperMode.Command ->
                                        ui.scope.launch { ui.snack.showSnackbar("这个功能暂未开发呢") }
                                }
                            } else {
                                ui goto RootScreens.Personal
                            }
                        }
                    ) { ui goto RootScreens.Personal },
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = if (ui.childMatch(RootScreens.Personal))
                            Icons.Filled.AccountCircle
                        else
                            Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = if (ui.childMatch(RootScreens.Personal))
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "我的",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}





