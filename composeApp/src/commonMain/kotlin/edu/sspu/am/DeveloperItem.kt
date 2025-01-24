package edu.sspu.am

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideSource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DeveloperTopWidget(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val developerMode by ui.settings.developerMode.enable.collectAsState()
    val screenIndicatorVisible by ui.settings.developerMode.developerItems.screenIndicator.visible.collectAsState()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        AnimatedVisibility(developerMode and screenIndicatorVisible) {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                ScreenIndicator(
                    ui = ui,
                    modifier = modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun DeveloperModeSettings(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val font by ui.font.collectAsState()
    val enableDeveloperMode by ui.settings.developerMode.enable.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)
        ) {
            Text(
                text = "开发者选项",
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.inversePrimary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SettingsSwitch(
                            ui = ui,
                            text = "开发者模式",
                            checked = enableDeveloperMode
                        ) { ui.settings.developerMode set it }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)
        ) {
            Text(
                text = "增强选项",
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 16.sp,
                color = if (enableDeveloperMode)
                    MaterialTheme.colorScheme.inversePrimary
                    else
                        MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = if (enableDeveloperMode)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                    MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val enable by ui.settings.developerMode.screenJumper.visible.collectAsState()
                        SettingsSwitch(
                            ui = ui,
                            text = "界面跳转装置",
                            checked = enable,
                            enabled = enableDeveloperMode
                        ) { ui.settings.developerMode.screenJumper.switch() }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val enable by ui.settings.developerMode.developerItems.screenIndicator.visible.collectAsState()
                        SettingsSwitch(
                            ui = ui,
                            text = "显示当前界面信息",
                            checked = enable,
                            enabled = enableDeveloperMode
                        ) { ui.settings.developerMode.developerItems.screenIndicator set it }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenIndicator(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val font by ui.font.collectAsState()
    var detail by remember { mutableStateOf(false) }

    val screen by ui.screen.collectAsState()

    Card(
        modifier = modifier.combinedClickable {
            detail = !detail
        },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Screen: ${screen}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.inversePrimary,
                fontWeight = FontWeight.Medium,
                fontFamily = font,
            )
            if (detail) {
                for (
                text in listOf(
                    "current.Size: ${screen.size}",
                    "current.Meta: ${screen.meta}",
                    "current.Page: ${screen.page}",
                    "current.Page.id: ${screen.page.id}",
                    "current.Page.title: ${screen.page.title}",
                )
                ) {
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Normal,
                        fontFamily = font,
                    )
                }
                Row {
                    Text(
                        text = "current.Page.icon: ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Normal,
                        fontFamily = font,
                    )

                    Icon(
                        imageVector = if (screen.page.icon != null)
                            screen.page.icon!!
                        else
                            Icons.Filled.HideSource,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
                Row {
                    Text(
                        text = "current.Page.url: ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Normal,
                        fontFamily = font,
                    )

                    Text(
                        text = screen.page.url.toString(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Normal,
                        fontFamily = font,
                    )
                }
            }
        }
    }
}