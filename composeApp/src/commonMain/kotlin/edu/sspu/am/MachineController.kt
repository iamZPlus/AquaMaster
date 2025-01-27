package edu.sspu.am

import KottieAnimation
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.ModeFanOff
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WifiTetheringOff
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material.icons.outlined.Api
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material.icons.outlined.HideSource
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WindPower
import androidx.compose.material.icons.outlined._5g
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
import kotlin.math.roundToInt

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

    val floatingMachineBaseInfoCardState = remember { mutableStateOf(false) }
    val floatingMachineConnectionInfoCardState = remember { mutableStateOf(false) }
    val floatingMachineCoreVersionInfoCardState = remember { mutableStateOf(false) }
    val floatingMachineSensorDataCardState = remember { mutableStateOf(false) }
    val floatingMachineControlCardState = remember { mutableStateOf(false) }
    val floatingMachineAlarmToAdministerSettingCardState = remember { mutableStateOf(false) }
    val floatingMachineWarningToAdministerSettingCardState = remember { mutableStateOf(false) }
    val floatingMachineAlarmToAISettingCardState = remember { mutableStateOf(false) }
    val floatingMachineWarningToAISettingCardState = remember { mutableStateOf(false) }

    val floatingMachineBaseInfoCard by floatingMachineBaseInfoCardState
    val floatingMachineConnectionInfoCard by floatingMachineConnectionInfoCardState
    val floatingMachineCoreVersionInfoCard by floatingMachineCoreVersionInfoCardState
    val floatingMachineSensorDataCard by floatingMachineSensorDataCardState
    val floatingMachineControlCard by floatingMachineControlCardState
    val floatingMachineAlarmToAdministerSettingCard by floatingMachineAlarmToAdministerSettingCardState
    val floatingMachineWarningToAdministerSettingCard by floatingMachineWarningToAdministerSettingCardState
    val floatingMachineAlarmToAISettingCard by floatingMachineAlarmToAISettingCardState
    val floatingMachineWarningToAISettingCard by floatingMachineWarningToAISettingCardState

    var enableControl by remember { mutableStateOf(true) }
    var enableGetData by remember { mutableStateOf(true) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            ui.settings.machine.refreshing.on()
            ui.scope.launch {
                delay(3000)
                ui.settings.machine.refreshing.off()
                ui.settings.machine.sensor.air.temperature set 33.33f
                ui.settings.machine.sensor.air.humidity set 45.5f
                ui.settings.machine.sensor.soil.temperature set 101f
                ui.settings.machine.sensor.soil.humidity set -1f
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
        var halfHeight by remember { mutableStateOf(0) }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { halfHeight = it.size.height / 3 },
            topBar = {
                if (
                    setOf(
                        floatingMachineBaseInfoCard,
                        floatingMachineConnectionInfoCard,
                        floatingMachineCoreVersionInfoCard,
                        floatingMachineSensorDataCard,
                        floatingMachineControlCard,
                        floatingMachineAlarmToAdministerSettingCard,
                        floatingMachineWarningToAdministerSettingCard,
                        floatingMachineAlarmToAISettingCard,
                        floatingMachineWarningToAISettingCard
                    ).any { it }
                ) {
                    if (machine != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 24.dp),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = with(LocalDensity.current) { halfHeight.toDp() }),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                if (floatingMachineBaseInfoCard) {
                                    item {
                                        MachineBaseInfoCard(
                                            ui = ui,
                                            modifier = Modifier.fillMaxWidth(),
                                            machine = machine,
                                            floatingState = floatingMachineBaseInfoCardState
                                        )
                                    }
                                }

                                if (floatingMachineConnectionInfoCard) {
                                    item {
                                        MachineConnectionInfoCard(
                                            ui = ui,
                                            modifier = Modifier.fillMaxWidth(),
                                            url = machine.url,
                                            floatingState = floatingMachineConnectionInfoCardState
                                        )
                                    }
                                }

                                if (machine.url != null) {
                                    if (floatingMachineCoreVersionInfoCard) {
                                        item {
                                            MachineCoreVersionInfoCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                floatingState = floatingMachineCoreVersionInfoCardState
                                            )
                                        }
                                    }

                                    if (floatingMachineSensorDataCard) {
                                        item {
                                            MachineSensorDataCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                enable = enableGetData,
                                                floatingState = floatingMachineSensorDataCardState
                                            )
                                        }
                                    }

                                    if (floatingMachineControlCard) {
                                        item {
                                            MachineControlCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                enable = enableControl,
                                                floatingState = floatingMachineControlCardState
                                            )
                                        }
                                    }

                                    if (floatingMachineAlarmToAdministerSettingCard) {
                                        item {
                                            MachineAlarmToAdministerSettingCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                enable = enableControl,
                                                floatingState = floatingMachineAlarmToAdministerSettingCardState
                                            )
                                        }
                                    }

                                    if (floatingMachineWarningToAdministerSettingCard) {
                                        item {
                                            MachineWarningToAdministerSettingCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                enable = enableControl,
                                                floatingState = floatingMachineWarningToAdministerSettingCardState
                                            )
                                        }
                                    }

                                    if (floatingMachineAlarmToAISettingCard) {
                                        item {
                                            MachineAlarmToAISettingCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                enable = enableControl,
                                                floatingState = floatingMachineAlarmToAISettingCardState
                                            )
                                        }
                                    }

                                    if (floatingMachineWarningToAISettingCard) {
                                        item {
                                            MachineWarningToAISettingCard(
                                                ui = ui,
                                                modifier = Modifier.fillMaxWidth(),
                                                url = machine.url,
                                                enable = enableControl,
                                                floatingState = floatingMachineWarningToAISettingCardState
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) {
            if (machine != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    if (!floatingMachineBaseInfoCard) {
                        item {
                            MachineBaseInfoCard(
                                ui = ui,
                                modifier = Modifier.fillMaxWidth(),
                                machine = machine,
                                floatingState = floatingMachineBaseInfoCardState
                            )
                        }
                    }

                    if (!floatingMachineConnectionInfoCard) {
                        item {
                            MachineConnectionInfoCard(
                                ui = ui,
                                modifier = Modifier.fillMaxWidth(),
                                url = machine.url,
                                floatingState = floatingMachineConnectionInfoCardState
                            )
                        }
                    }

                    if (machine.url != null) {
                        if (!floatingMachineCoreVersionInfoCard) {
                            item {
                                MachineCoreVersionInfoCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    floatingState = floatingMachineCoreVersionInfoCardState
                                )
                            }
                        }

                        if (!floatingMachineSensorDataCard) {
                            item {
                                MachineSensorDataCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    enable = enableGetData,
                                    floatingState = floatingMachineSensorDataCardState
                                )
                            }
                        }

                        if (!floatingMachineControlCard) {
                            item {
                                MachineControlCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    enable = enableControl,
                                    floatingState = floatingMachineControlCardState
                                )
                            }
                        }

                        if (!floatingMachineAlarmToAdministerSettingCard) {
                            item {
                                MachineAlarmToAdministerSettingCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    enable = enableControl,
                                    floatingState = floatingMachineAlarmToAdministerSettingCardState
                                )
                            }
                        }

                        if (!floatingMachineWarningToAdministerSettingCard) {
                            item {
                                MachineWarningToAdministerSettingCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    enable = enableControl,
                                    floatingState = floatingMachineWarningToAdministerSettingCardState
                                )
                            }
                        }

                        if (!floatingMachineAlarmToAISettingCard) {
                            item {
                                MachineAlarmToAISettingCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    enable = enableControl,
                                    floatingState = floatingMachineAlarmToAISettingCardState
                                )
                            }
                        }

                        if (!floatingMachineWarningToAISettingCard) {
                            item {
                                MachineWarningToAISettingCard(
                                    ui = ui,
                                    modifier = Modifier.fillMaxWidth(),
                                    url = machine.url,
                                    enable = enableControl,
                                    floatingState = floatingMachineWarningToAISettingCardState
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MachineBaseInfoCard(
    ui: UI,
    modifier: Modifier = Modifier,
    machine: MachineData,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    Card(
        modifier = modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onDoubleClick = { floatingState.value = !floatingState.value }
            ) {},
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MachineConnectionInfoCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl? = null,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
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
        modifier = modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onDoubleClick = { floatingState.value = !floatingState.value }
            ) {},
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MachineCoreVersionInfoCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
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
        modifier = modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onDoubleClick = { floatingState.value = !floatingState.value }
            ) {},
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MachineSensorDataCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: Boolean = true,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "传感器数据",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enable)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = { floatingState.value = !floatingState.value }
                ) {},
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (enable)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            val airTemperatureAlarmLower by ui.settings.machine.running.limit.alarm.administer.air.temperature.lower.current.collectAsState()
            val airTemperatureAlarmUpper by ui.settings.machine.running.limit.alarm.administer.air.temperature.upper.current.collectAsState()
            val airTemperatureWarningLower by ui.settings.machine.running.limit.warning.administer.air.temperature.lower.current.collectAsState()
            val airTemperatureWarningUpper by ui.settings.machine.running.limit.warning.administer.air.temperature.upper.current.collectAsState()
            val airHumidityAlarmLower by ui.settings.machine.running.limit.alarm.administer.air.humidity.lower.current.collectAsState()
            val airHumidityAlarmUpper by ui.settings.machine.running.limit.alarm.administer.air.humidity.upper.current.collectAsState()
            val airHumidityWarningLower by ui.settings.machine.running.limit.warning.administer.air.humidity.lower.current.collectAsState()
            val airHumidityWarningUpper by ui.settings.machine.running.limit.warning.administer.air.humidity.upper.current.collectAsState()
            val soilTemperatureAlarmLower by ui.settings.machine.running.limit.alarm.administer.soil.temperature.lower.current.collectAsState()
            val soilTemperatureAlarmUpper by ui.settings.machine.running.limit.alarm.administer.soil.temperature.upper.current.collectAsState()
            val soilTemperatureWarningLower by ui.settings.machine.running.limit.warning.administer.soil.temperature.lower.current.collectAsState()
            val soilTemperatureWarningUpper by ui.settings.machine.running.limit.warning.administer.soil.temperature.upper.current.collectAsState()
            val soilHumidityAlarmLower by ui.settings.machine.running.limit.alarm.administer.soil.humidity.lower.current.collectAsState()
            val soilHumidityAlarmUpper by ui.settings.machine.running.limit.alarm.administer.soil.humidity.upper.current.collectAsState()
            val soilHumidityWarningLower by ui.settings.machine.running.limit.warning.administer.soil.humidity.lower.current.collectAsState()
            val soilHumidityWarningUpper by ui.settings.machine.running.limit.warning.administer.soil.humidity.upper.current.collectAsState()

            val airTemperature by ui.settings.machine.sensor.air.temperature.current.collectAsState()
            val airHumidity by ui.settings.machine.sensor.air.humidity.current.collectAsState()
            val soilTemperature by ui.settings.machine.sensor.soil.temperature.current.collectAsState()
            val soilHumidity by ui.settings.machine.sensor.soil.humidity.current.collectAsState()

            val airTemperatureProgress by animateFloatAsState(
                targetValue = when {
                    airTemperature < 0f -> -0.001f
                    airTemperature > 100f -> 100.001f
                    else -> airTemperature
                },
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            val airHumidityProgress by animateFloatAsState(
                targetValue = when {
                    airHumidity < 0f -> -0.001f
                    airHumidity > 100f -> 100.001f
                    else -> airHumidity
                },
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            val soilTemperatureProgress by animateFloatAsState(
                targetValue = when {
                    soilTemperature < 0f -> -0.001f
                    soilTemperature > 100f -> 100.001f
                    else -> soilTemperature
                },
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            val soilHumidityProgress by animateFloatAsState(
                targetValue = when {
                    soilHumidity < 0f -> -0.001f
                    soilHumidity > 100f -> 100.001f
                    else -> soilHumidity
                },
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )

            Text(
                text = "空气温度",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            Card(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = if (enable)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.onBackground,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (enable)
                                    when {
                                        airTemperatureProgress < airTemperatureAlarmLower -> Color(0xFFCCA4E3)
                                        airTemperatureProgress > airTemperatureAlarmUpper -> Color(0xFFf47983)
                                        airTemperatureProgress < airTemperatureWarningLower -> Color(0xFF70F3FF)
                                        airTemperatureProgress > airTemperatureWarningUpper -> Color(0xFFFFA400)
                                        else -> MaterialTheme.colorScheme.primary
                                    }
                                else
                                    MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .fillMaxWidth(airTemperatureProgress / 100f)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeviceThermostat,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            tint = if (enable)
                                when {
                                    airTemperatureProgress < airTemperatureAlarmLower -> Color(0xFF801DAE)
                                    airTemperatureProgress > airTemperatureAlarmUpper -> Color(0xFFF00056)
                                    airTemperatureProgress < airTemperatureWarningLower -> Color(0xFF44CEF6)
                                    airTemperatureProgress > airTemperatureWarningUpper -> Color(0xFFFF7500)
                                    else -> MaterialTheme.colorScheme.onPrimary
                                }
                            else
                                MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }

                    Text(
                        text = when {
                            airTemperature < 0f -> "超出下限"
                            airTemperature > 100f -> "超出上限"
                            else -> "${(airTemperature * 100f).roundToInt() / 100.0}℃"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 8.dp),
                        fontSize = 20.sp,
                        color = if (enable)
                            when {
                                airTemperatureProgress < airTemperatureAlarmLower -> Color(0xFF801DAE)
                                airTemperatureProgress > airTemperatureAlarmUpper -> Color(0xFFF00056)
                                airTemperatureProgress < airTemperatureWarningLower -> Color(0xFF44CEF6)
                                airTemperatureProgress > airTemperatureWarningUpper -> Color(0xFFFF7500)
                                else -> MaterialTheme.colorScheme.onPrimary
                            }
                        else
                            MaterialTheme.colorScheme.tertiaryContainer,
                        fontWeight = FontWeight.Black,
                        fontFamily = font
                    )
                }
            }

            Text(
                text = "空气湿度",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            Card(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = if (enable)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.onBackground,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (enable)
                                    when {
                                        airHumidityProgress < airHumidityAlarmLower -> Color(0xFFCCA4E3)
                                        airHumidityProgress > airHumidityAlarmUpper -> Color(0xFFf47983)
                                        airHumidityProgress < airHumidityWarningLower -> Color(0xFF70F3FF)
                                        airHumidityProgress > airHumidityWarningUpper -> Color(0xFFFFA400)
                                        else -> MaterialTheme.colorScheme.primary
                                    }
                                else
                                    MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .fillMaxWidth(airHumidityProgress / 100f)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            tint = if (enable)
                                when {
                                    airHumidityProgress < airHumidityAlarmLower -> Color(0xFF801DAE)
                                    airHumidityProgress > airHumidityAlarmUpper -> Color(0xFFF00056)
                                    airHumidityProgress < airHumidityWarningLower -> Color(0xFF44CEF6)
                                    airHumidityProgress > airHumidityWarningUpper -> Color(0xFFFF7500)
                                    else -> MaterialTheme.colorScheme.onPrimary
                                }
                            else
                                MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }

                    Text(
                        text = when {
                            airHumidity < 0f -> "超出下限"
                            airHumidity > 100f -> "超出上限"
                            else -> "${(airHumidity * 100f).roundToInt() / 100.0}%RH"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 8.dp),
                        fontSize = 20.sp,
                        color = if (enable)
                            when {
                                airHumidityProgress < airHumidityAlarmLower -> Color(0xFF801DAE)
                                airHumidityProgress > airHumidityAlarmUpper -> Color(0xFFF00056)
                                airHumidityProgress < airHumidityWarningLower -> Color(0xFF44CEF6)
                                airHumidityProgress > airHumidityWarningUpper -> Color(0xFFFF7500)
                                else -> MaterialTheme.colorScheme.onPrimary
                            }
                        else
                            MaterialTheme.colorScheme.tertiaryContainer,
                        fontWeight = FontWeight.Black,
                        fontFamily = font
                    )
                }
            }

            Text(
                text = "土壤温度",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            Card(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = if (enable)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.onBackground,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (enable)
                                    when {
                                        soilTemperatureProgress < soilTemperatureAlarmLower -> Color(0xFFCCA4E3)
                                        soilTemperatureProgress > soilTemperatureAlarmUpper -> Color(0xFFf47983)
                                        soilTemperatureProgress < soilTemperatureWarningLower -> Color(0xFF70F3FF)
                                        soilTemperatureProgress > soilTemperatureWarningUpper -> Color(0xFFFFA400)
                                        else -> MaterialTheme.colorScheme.primary
                                    }
                                else
                                    MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .fillMaxWidth(soilTemperatureProgress / 100f)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeviceThermostat,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            tint = if (enable)
                                when {
                                    soilTemperatureProgress < soilTemperatureAlarmLower -> Color(0xFF801DAE)
                                    soilTemperatureProgress > soilTemperatureAlarmUpper -> Color(0xFFF00056)
                                    soilTemperatureProgress < soilTemperatureWarningLower -> Color(0xFF44CEF6)
                                    soilTemperatureProgress > soilTemperatureWarningUpper -> Color(0xFFFF7500)
                                    else -> MaterialTheme.colorScheme.onPrimary
                                }
                            else
                                MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }

                    Text(
                        text = when {
                            soilTemperature < 0f -> "超出下限"
                            soilTemperature > 100f -> "超出上限"
                            else -> "${(soilTemperature * 100f).roundToInt() / 100.0}℃"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 8.dp),
                        fontSize = 20.sp,
                        color = if (enable)
                            when {
                                soilTemperatureProgress < soilTemperatureAlarmLower -> Color(0xFF801DAE)
                                soilTemperatureProgress > soilTemperatureAlarmUpper -> Color(0xFFF00056)
                                soilTemperatureProgress < soilTemperatureWarningLower -> Color(0xFF44CEF6)
                                soilTemperatureProgress > soilTemperatureWarningUpper -> Color(0xFFFF7500)
                                else -> MaterialTheme.colorScheme.onPrimary
                            }
                        else
                            MaterialTheme.colorScheme.tertiaryContainer,
                        fontWeight = FontWeight.Black,
                        fontFamily = font
                    )
                }
            }

            Text(
                text = "土壤湿度",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            Card(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = if (enable)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.onBackground,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (enable)
                                    when {
                                        soilHumidityProgress < soilHumidityAlarmLower -> Color(0xFFCCA4E3)
                                        soilHumidityProgress > soilHumidityAlarmUpper -> Color(0xFFf47983)
                                        soilHumidityProgress < soilHumidityWarningLower -> Color(0xFF70F3FF)
                                        soilHumidityProgress > soilHumidityWarningUpper -> Color(0xFFFFA400)
                                        else -> MaterialTheme.colorScheme.primary
                                    }
                                else
                                    MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .fillMaxWidth(soilHumidityProgress / 100f)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            tint = if (enable)
                                when {
                                    soilHumidityProgress < soilHumidityAlarmLower -> Color(0xFF801DAE)
                                    soilHumidityProgress > soilHumidityAlarmUpper -> Color(0xFFF00056)
                                    soilHumidityProgress < soilHumidityWarningLower -> Color(0xFF44CEF6)
                                    soilHumidityProgress > soilHumidityWarningUpper -> Color(0xFFFF7500)
                                    else -> MaterialTheme.colorScheme.onPrimary
                                }
                            else
                                MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }

                    Text(
                        text = when {
                            soilHumidity < 0f -> "超出下限"
                            soilHumidity > 100f -> "超出上限"
                            else -> "${(soilHumidity * 100).roundToInt() / 100.0}%RH"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 8.dp),
                        fontSize = 20.sp,
                        color = if (enable)
                            when {
                                soilHumidityProgress < soilHumidityAlarmLower -> Color(0xFF801DAE)
                                soilHumidityProgress > soilHumidityAlarmUpper -> Color(0xFFF00056)
                                soilHumidityProgress < soilHumidityWarningLower -> Color(0xFF44CEF6)
                                soilHumidityProgress > soilHumidityWarningUpper -> Color(0xFFFF7500)
                                else -> MaterialTheme.colorScheme.onPrimary
                            }
                        else
                            MaterialTheme.colorScheme.tertiaryContainer,
                        fontWeight = FontWeight.Black,
                        fontFamily = font
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MachineControlCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: Boolean = true,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "设备操控",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enable)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = { floatingState.value = !floatingState.value }
                ) {},
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (enable)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            val windState = remember {
                SliderState(
                    value = ui.settings.machine.running.power.wind.current.value.toFloat(),
                    valueRange = 0f..100f
                )
            }
            windState.onValueChangeFinished = {
                ui.settings.machine.running.power.wind set windState.value.toInt()
            }

            val waterState = remember {
                SliderState(
                    value = ui.settings.machine.running.power.water.current.value.toFloat(),
                    valueRange = 0f..100f
                )
            }
            waterState.onValueChangeFinished = {
                ui.settings.machine.running.power.water set waterState.value.toInt()
            }

            Slider(
                state = windState,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                thumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.background
                            else
                                MaterialTheme.colorScheme.onBackground,
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.value / 100f)
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                            ) {
                                Icon(
                                    imageVector = when (it.value) {
                                        0f -> Icons.Default.ModeFanOff
                                        100f -> Icons.Filled.WindPower
                                        else -> Icons.Outlined.WindPower
                                    },
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(32.dp),
                                    tint = if (enable)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.tertiaryContainer
                                )
                            }

                            Text(
                                text = "${it.value.toInt()}%",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Slider(
                state = waterState,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                thumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.background
                            else
                                MaterialTheme.colorScheme.onBackground,
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.value / 100f)
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                            ) {
                                Icon(
                                    imageVector = when (it.value) {
                                        0f -> Icons.Default.FormatColorReset
                                        100f -> Icons.Filled.WaterDrop
                                        else -> Icons.Outlined.WaterDrop
                                    },
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(32.dp),
                                    tint = if (enable)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.tertiaryContainer
                                )
                            }

                            Text(
                                text = "${it.value.toInt()}%",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MachineAlarmToAdministerSettingCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: Boolean = true,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "管理员警报设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enable)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = { floatingState.value = !floatingState.value }
                ) {},
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (enable)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            val airTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.administer.air.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.administer.air.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.administer.air.temperature.lower set airTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.administer.air.temperature.upper set airTemperatureState.activeRangeEnd.toInt()
            }

            val airHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.administer.air.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.administer.air.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.administer.air.humidity.lower set airHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.administer.air.humidity.upper set airHumidityState.activeRangeEnd.toInt()
            }

            val soilTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.administer.soil.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.administer.soil.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.administer.soil.temperature.lower set soilTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.administer.soil.temperature.upper set soilTemperatureState.activeRangeEnd.toInt()
            }

            val soilHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.administer.soil.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.administer.soil.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.administer.soil.humidity.lower set soilHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.administer.soil.humidity.upper set soilHumidityState.activeRangeEnd.toInt()
            }

            Text(
                text = "空气温度区间",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "空气湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤温度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MachineWarningToAdministerSettingCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: Boolean = true,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "管理员提醒设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enable)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = { floatingState.value = !floatingState.value }
                ) {},
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (enable)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            val airTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.administer.air.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.administer.air.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.administer.air.temperature.lower set airTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.administer.air.temperature.upper set airTemperatureState.activeRangeEnd.toInt()
            }

            val airHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.administer.air.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.administer.air.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.administer.air.humidity.lower set airHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.administer.air.humidity.upper set airHumidityState.activeRangeEnd.toInt()
            }

            val soilTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.administer.soil.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.administer.soil.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.administer.soil.temperature.lower set soilTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.administer.soil.temperature.upper set soilTemperatureState.activeRangeEnd.toInt()
            }

            val soilHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.administer.soil.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.administer.soil.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.administer.soil.humidity.lower set soilHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.administer.soil.humidity.upper set soilHumidityState.activeRangeEnd.toInt()
            }

            Text(
                text = "空气温度区间",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "空气湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤温度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enable,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MachineAlarmToAISettingCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: Boolean = true,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableAIOperateWhenAlarm by ui.settings.machine.ai.operateWhenAlarm.enable.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "AI警报介入设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enable and enableAIOperateWhenAlarm)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = { floatingState.value = !floatingState.value }
                ) {},
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (enable and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            val airTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.ai.air.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.ai.air.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.ai.air.temperature.lower set airTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.ai.air.temperature.upper set airTemperatureState.activeRangeEnd.toInt()
            }

            val airHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.ai.air.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.ai.air.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.ai.air.humidity.lower set airHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.ai.air.humidity.upper set airHumidityState.activeRangeEnd.toInt()
            }

            val soilTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.ai.soil.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.ai.soil.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.ai.soil.temperature.lower set soilTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.ai.soil.temperature.upper set soilTemperatureState.activeRangeEnd.toInt()
            }

            val soilHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.alarm.ai.soil.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.alarm.ai.soil.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.alarm.ai.soil.humidity.lower set soilHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.alarm.ai.soil.humidity.upper set soilHumidityState.activeRangeEnd.toInt()
            }

            Text(
                text = "空气温度区间",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenAlarm)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "空气湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenAlarm)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤温度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenAlarm)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenAlarm)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenAlarm)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenAlarm)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MachineWarningToAISettingCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: Boolean = true,
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableAIOperateWhenWarning by ui.settings.machine.ai.operateWhenWarning.enable.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "AI提醒介入设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enable and enableAIOperateWhenWarning)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = { floatingState.value = !floatingState.value }
                ) {},
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (enable and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            val airTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.ai.air.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.ai.air.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.ai.air.temperature.lower set airTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.ai.air.temperature.upper set airTemperatureState.activeRangeEnd.toInt()
            }

            val airHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.ai.air.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.ai.air.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            airHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.ai.air.humidity.lower set airHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.ai.air.humidity.upper set airHumidityState.activeRangeEnd.toInt()
            }

            val soilTemperatureState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.ai.soil.temperature.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.ai.soil.temperature.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilTemperatureState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.ai.soil.temperature.lower set soilTemperatureState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.ai.soil.temperature.upper set soilTemperatureState.activeRangeEnd.toInt()
            }

            val soilHumidityState = remember {
                RangeSliderState(
                    activeRangeStart = ui.settings.machine.running.limit.warning.ai.soil.humidity.lower.current.value.toFloat(),
                    activeRangeEnd = ui.settings.machine.running.limit.warning.ai.soil.humidity.upper.current.value.toFloat(),
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                )
            }
            soilHumidityState.onValueChangeFinished = {
                ui.settings.machine.running.limit.warning.ai.soil.humidity.lower set soilHumidityState.activeRangeStart.toInt()
                ui.settings.machine.running.limit.warning.ai.soil.humidity.upper set soilHumidityState.activeRangeEnd.toInt()
            }

            Text(
                text = "空气温度区间",
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenWarning)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "空气湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenWarning)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤温度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenWarning)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}℃",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )

            Text(
                text = "土壤湿度区间",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
                fontSize = 12.sp,
                color = if (enable and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enable and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enable and enableAIOperateWhenWarning)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .fillMaxWidth(it.activeRangeStart / 100f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (enable and enableAIOperateWhenWarning)
                                            MaterialTheme.colorScheme.background
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    )
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f - it.activeRangeEnd / 100f)
                            )

                            Text(
                                text = "${it.activeRangeStart.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                            Text(
                                text = "${it.activeRangeEnd.roundToInt()}%RH",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(horizontal = 8.dp),
                                fontSize = 20.sp,
                                color = if (enable and enableAIOperateWhenWarning)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Black,
                                fontFamily = font
                            )
                        }
                    }
                }
            )
        }
    }
}
