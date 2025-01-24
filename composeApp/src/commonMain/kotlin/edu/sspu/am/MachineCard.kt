package edu.sspu.am

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Api
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material.icons.outlined.HideSource
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.sspu.am.MachineType.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachinesList(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val font by ui.font.collectAsState()
    val categories by ui.data.list.categories.collectAsState()

    val isRefreshing by ui.settings.list.refreshing.enable.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            ui.settings.list.refreshing.on()
            ui.scope.launch {
                delay(3000)
                ui.settings.list.refreshing.off()
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            categories.forEachIndexed { index, category ->
                item {
                    Row {
                        Box(
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .align(Alignment.CenterVertically)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    ui.data.list.set(index, category.change(unfold = !category.unfold))
                                }
                        ) {
                            Icon(
                                imageVector = if (category.unfold)
                                    Icons.Filled.KeyboardArrowDown
                                else
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(20.dp),
                                tint = MaterialTheme.colorScheme.inversePrimary
                            )
                        }

                        if (category.icon != null) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 2.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    imageVector = machineCategoryIconToImageVector[category.icon]!!,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(15.dp),
                                    tint = MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                        }

                        Text(
                            text = category.name ?: "未命名",
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontWeight = FontWeight.Medium,
                            fontFamily = font
                        )
                    }
                }

                if (category.unfold) {
                    items(category.machines) { machine ->
                        MachineCard(
                            ui = ui,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(bottom = 16.dp),
                            machine = machine,
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "总要有个底线~",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font,
                    )
                }
            }
        }
    }
}

@Composable
fun MachineCard(
    ui: UI,
    modifier: Modifier = Modifier,
    machine: MachineData
) {
    val font by ui.font.collectAsState()

    Card(
        modifier = modifier
            .clickable {
                ui.settings.machine.set(machine)
                ui goto RootScreens.List.child(
                    Page(
                        id = "MachineController",
                        title = "MachineController",
                        url = "/MachineController"
                    )
                )
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.8f)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = when(machine.type) {
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
                        text = machine.name ?: "未命名设备",
                        modifier = Modifier
                            .align(Alignment.Start),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Black,
                        fontFamily = font,
                        lineHeight = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = machine.url?.display ?: "null",
                        modifier = Modifier
                            .align(Alignment.Start),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = font,
                        lineHeight = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 8.dp)
                    .clickable {  }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}