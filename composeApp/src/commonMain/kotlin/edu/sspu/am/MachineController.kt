package edu.sspu.am

import KottieAnimation
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.WifiTetheringOff
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    MachineBaseInfoCard(
                        ui = ui,
                        modifier = Modifier.fillMaxWidth(),
                        machine = machine
                    )
                }

                item {
                    MachineConnectionInfoCard(
                        ui = ui,
                        modifier = Modifier.fillMaxWidth(),
                        url = machine.url
                    )
                }

                if (machine.url != null) {
                    item {
                        MachineCoreVersionInfoCard(
                            ui = ui,
                            modifier = Modifier.fillMaxWidth(),
                            url = machine.url
                        )
                    }

                    item {
                        MachineControlCard(
                            ui = ui,
                            modifier = Modifier.fillMaxWidth(),
                            url = machine.url
                        )
                    }
                }
            }
        }
    }
}

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
                        Virtual -> Icons.Outlined.Api // TODO: 正式上线时删除这部分
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
                                        Virtual -> "虚拟" // TODO: 正式上线时删除这部分
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
                    lineHeight = 12.sp,
                )
            }
        }
    }
}

@Composable
fun MachineConnectionInfoCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl? = null
) {
    val font by ui.font.collectAsState()

    var connected by remember { mutableStateOf(if (url != null) null else false) }
    var info by remember { mutableStateOf("") }

    // TODO: 获取连接状态
    connected = true
    info = "网络状态良好, 连接稳定"

//    connected = null
//    info = "正在尝试连接${url?.display ?: "null"}"

//    connected = false
//    info = "无法连接到${url?.display ?: "null"}"

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
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
            ) {

                when (connected) {
                    true -> {
                        val wifiConnectedComposition = rememberKottieComposition(
                            spec = KottieCompositionSpec.JsonString(WIFI_CONNECTED)
                        )
                        val wifiConnectedAnimationState by animateKottieCompositionAsState(
                            composition = wifiConnectedComposition,
                            isPlaying = true,
                            iterations = KottieConstants.IterateForever
                        )
                        KottieAnimation(
                            composition = wifiConnectedComposition,
                            progress = { wifiConnectedAnimationState.progress },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp)
                        )
                    }

                    false -> {
                        Icon(
                            imageVector = Icons.Default.WifiTetheringOff,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp),
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                    }

                    null -> {
                        val wifiConnectingComposition = rememberKottieComposition(
                            spec = KottieCompositionSpec.JsonString(WIFI_CONNECTING)
                        )
                        val wifiConnectingAnimationState by animateKottieCompositionAsState(
                            composition = wifiConnectingComposition,
                            isPlaying = true,
                            iterations = KottieConstants.IterateForever
                        )
                        KottieAnimation(
                            composition = wifiConnectingComposition,
                            progress = { wifiConnectingAnimationState.progress },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp)
                        )
                    }
                }
            }

            Column(
                Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Circle,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(18.dp),
                            tint = when (connected) {
                                true -> Color(0xFF3DE1AD)
                                false -> Color(0xFFF00056)
                                null -> Color(0xFFFFA400)
                            }
                        )
                    }

                    Text(
                        text = when (connected) {
                            true -> "连接成功"
                            false -> "连接失败"
                            null -> "连接中..."
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Black,
                        fontFamily = font
                    )
                }

                Text(
                    text = info,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.Medium,
                    fontFamily = font,
                    lineHeight = 12.sp
                )
            }
        }
    }
}

@Composable
fun MachineCoreVersionInfoCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl
) {
    val font by ui.font.collectAsState()

    val version = when (url) {
        is MachineUrl.Local -> "v1.0.0rc1" // TODO: 获取内核版本
        is MachineUrl.Cloud -> "v1.0.0rc1" // TODO: 获取内核版本
        is MachineUrl.Remote -> "v1.0.0rc1" // TODO: 获取内核版本
        is MachineUrl.Blank -> "Blank~NoV"
        is MachineUrl.Template -> "v1.0.0rc1~v1.0.0rc9" // TODO: 获取模板支持的版本
        is MachineUrl.Group -> "v1.0.0rc1" // TODO: 获取组合的内核版本
        is MachineUrl.Virtual -> "Virtual~NoV" // TODO: 正式上线时删除这部分
    }

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
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }

            Column(
                Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "内核版本",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Black,
                    fontFamily = font
                )

                Text(
                    text = version,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Black,
                    fontFamily = font
                )
            }
        }
    }
}

@Composable
fun MachineControlCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl
) {
}