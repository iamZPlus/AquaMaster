package edu.sspu.am

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Api
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material.icons.outlined.HideSource
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material.icons.outlined._5g
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.sspu.am.MachineType.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MachineBaseInfoCard(
    ui: UI,
    modifier: Modifier = Modifier,
    machine: MachineData
) {
    val font by ui.font.collectAsState()

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = when (machine.type) {
                        Local -> Icons.Outlined.Print
                        Cloud -> Icons.Outlined.Cloud
                        Remote -> Icons.Outlined._5g
                        Blank -> Icons.Outlined.HideSource
                        Template -> Icons.Outlined.TableView
                        Group -> Icons.Outlined.GroupWork
                        Virtual -> Icons.Outlined.Api
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(machine.name ?: "未命名设备")
                        append(" ")
                        appendInlineContent("type")
                    },
                    modifier = Modifier
                        .align(Alignment.Start),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Black,
                    fontFamily = font,
                    lineHeight = 24.sp,
                    inlineContent = mapOf(
                        "type" to InlineTextContent(
                            placeholder = Placeholder(
                                width = 40.sp,
                                height = 24.sp,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) {
                            Card(
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = when (machine.type) {
                                        Local -> "本地"
                                        Cloud -> "云端"
                                        Remote -> "远程"
                                        Blank -> "空集"
                                        Template -> "模板"
                                        Group -> "组合"
                                        Virtual -> "虚拟"
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(4.dp),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = font,
                                    lineHeight = 12.sp,
                                )
                            }
                        }
                    )
                )

                Text(
                    text = "URL: " + (machine.url?.display ?: "null"),
                    modifier = Modifier
                        .align(Alignment.Start),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.Medium,
                    fontFamily = font,
                    lineHeight = 16.sp,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineController(
    ui: UI,
    modifier: Modifier = Modifier,
    machine: MachineData? = null
) {
    val font by ui.font.collectAsState()

    val isRefreshing by ui.settings.machine.refreshing.enable.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            ui.settings.machine.refreshing.on()
            ui.scope.launch {
                delay(3000)
                ui.settings.machine.refreshing.off()
            }
        },
        modifier = modifier,
        state = pullToRefreshState,
        indicator = {
            if ((ui.device.value != DeviceType.Desktop) or isRefreshing) {
                Card(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopCenter)
                        .pullToRefreshIndicator(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                        ),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Crossfade(
                            targetState = isRefreshing,
                            modifier = Modifier.align(Alignment.Center),
                            animationSpec = tween()
                        ) {
                            if (it) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.CloudDownload,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center)
                                        .graphicsLayer {
                                            val progress = pullToRefreshState.distanceFraction.coerceIn(0f, 1f)
                                            this.alpha = progress
                                            this.scaleX = progress
                                            this.scaleY = progress
                                        },
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        if (machine != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    MachineBaseInfoCard(
                        ui = ui,
                        modifier = Modifier.fillMaxWidth(),
                        machine = machine
                    )
                }
            }
        }
    }
}