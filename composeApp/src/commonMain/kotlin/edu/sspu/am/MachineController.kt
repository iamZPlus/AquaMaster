package edu.sspu.am

import KottieAnimation
import androidx.compose.animation.Crossfade
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
    val floatingMachineControlCardState = remember { mutableStateOf(false) }
    val floatingMachineAlarmToAdministerSettingCardState = remember { mutableStateOf(false) }
    val floatingMachineWarningToAdministerSettingCardState = remember { mutableStateOf(false) }
    val floatingMachineAlarmToAISettingCardState = remember { mutableStateOf(false) }
    val floatingMachineWarningToAISettingCardState = remember { mutableStateOf(false) }

    val floatingMachineBaseInfoCard by floatingMachineBaseInfoCardState
    val floatingMachineConnectionInfoCard by floatingMachineConnectionInfoCardState
    val floatingMachineCoreVersionInfoCard by floatingMachineCoreVersionInfoCardState
    val floatingMachineControlCard by floatingMachineControlCardState
    val floatingMachineAlarmToAdministerSettingCard by floatingMachineAlarmToAdministerSettingCardState
    val floatingMachineWarningToAdministerSettingCard by floatingMachineWarningToAdministerSettingCardState
    val floatingMachineAlarmToAISettingCard by floatingMachineAlarmToAISettingCardState
    val floatingMachineWarningToAISettingCard by floatingMachineWarningToAISettingCardState

    var enableControl = remember { mutableStateOf(true) }

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MachineControlCard(
    ui: UI,
    modifier: Modifier = Modifier,
    url: MachineUrl,
    enable: MutableState<Boolean> = remember { mutableStateOf(true) },
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableControl by enable

    Column(
        modifier = modifier
    ) {
        Text(
            text = "设备操控",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enableControl)
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
                containerColor = if (enableControl)
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
                enabled = enableControl,
                thumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                    tint = if (enableControl)
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
                                color = if (enableControl)
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
                enabled = enableControl,
                thumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                    tint = if (enableControl)
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
                                color = if (enableControl)
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
    enable: MutableState<Boolean> = remember { mutableStateOf(true) },
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableControl by enable

    Column(
        modifier = modifier
    ) {
        Text(
            text = "管理员警报设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enableControl)
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
                containerColor = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
    enable: MutableState<Boolean> = remember { mutableStateOf(true) },
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableControl by enable

    Column(
        modifier = modifier
    ) {
        Text(
            text = "管理员提醒设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enableControl)
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
                containerColor = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
                color = if (enableControl)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl)
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
                                        if (enableControl)
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
                                        if (enableControl)
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
                                color = if (enableControl)
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
                                color = if (enableControl)
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
    enable: MutableState<Boolean> = remember { mutableStateOf(true) },
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableControl by enable
    val enableAIOperateWhenAlarm by ui.settings.machine.ai.operateWhenAlarm.enable.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "AI警报介入设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enableControl and enableAIOperateWhenAlarm)
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
                containerColor = if (enableControl and enableAIOperateWhenAlarm)
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
                color = if (enableControl and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                color = if (enableControl and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                color = if (enableControl and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                color = if (enableControl and enableAIOperateWhenAlarm)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenAlarm,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                        if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
                                color = if (enableControl and enableAIOperateWhenAlarm)
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
    enable: MutableState<Boolean> = remember { mutableStateOf(true) },
    floatingState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val font by ui.font.collectAsState()

    val enableControl by enable
    val enableAIOperateWhenWarning by ui.settings.machine.ai.operateWhenWarning.enable.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "AI提醒介入设置",
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            fontSize = 16.sp,
            color = if (enableControl and enableAIOperateWhenWarning)
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
                containerColor = if (enableControl and enableAIOperateWhenWarning)
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
                color = if (enableControl and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                color = if (enableControl and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = airHumidityState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                color = if (enableControl and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilTemperatureState,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                color = if (enableControl and enableAIOperateWhenWarning)
                    MaterialTheme.colorScheme.inversePrimary
                else
                    MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Medium,
                fontFamily = font
            )

            RangeSlider(
                state = soilHumidityState,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = enableControl and enableAIOperateWhenWarning,
                startThumb = {},
                endThumb = {},
                track = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                        if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
                                color = if (enableControl and enableAIOperateWhenWarning)
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
