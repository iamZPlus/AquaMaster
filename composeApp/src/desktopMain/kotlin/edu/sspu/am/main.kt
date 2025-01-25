package edu.sspu.am

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Crop54
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import aquamaster.composeapp.generated.resources.Res
import aquamaster.composeapp.generated.resources.am
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


object Information {
    object Application {
        const val NAME = "AquaMaster"
        const val VERSION = "内部测试版(鲲鹏A1)"
    }
}


@Composable
fun DesktopApp(
    ui: UI
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RefreshButton(
                        ui = ui,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterStart)
                    )

                    NewMachineAdder(
                        ui = ui,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
                TopWidget(ui = ui)
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxHeight()
            ) {
                QwenButton(
                    ui = ui,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                ),
            color = MaterialTheme.colorScheme.background
        ) {
            App(
                ui = ui
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val ui = UI()
    ui.init()

    ui setDevice DeviceType.Desktop

    ui.settings.developerMode.on()
    ui.settings.developerMode.fastJumper.one.target set "@app.Settings.other.DeveloperMode"

    // TODO: 正式上线时删除这部分
    ui.data.list.append(
        MachineCategory(
            name = "默认",
//                icon = MachineCategoryIcon.Heart,
            machines = listOf(
                MachineData(
                    type = MachineType.Blank,
                    name = "测试机1",
                    url = MachineUrl.Blank
                ),
                MachineData(
                    type = MachineType.Local,
                    name = "测试机2",
                    url = MachineUrl.Local(host = "127.0.0.1", port = 8080)
                ),
                MachineData(
                    type = MachineType.Cloud,
                    name = "测试机3",
                    url = MachineUrl.Cloud(
                        server = "am.sspu.edu.cn",
                        id = "test-java-pack-aabb-1234"
                    )
                ),
                MachineData(
                    type = MachineType.Remote,
                    name = "测试机4",
                    url = MachineUrl.Remote(
                        local = MachineUrl.Local(host = "127.0.0.2", port = 8080),
                        cloud = MachineUrl.Cloud(
                            server = "am.sspu.edu.cn",
                            id = "test-java-pack-aabb-1235"
                        )
                    )
                ),
                MachineData(
                    type = MachineType.Virtual,
                    name = "测试机5",
                    url = MachineUrl.Virtual
                ),
                MachineData(
                    type = MachineType.Virtual,
                    name = "测试机AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    url = MachineUrl.Virtual
                ),
                MachineData(
                    type = MachineType.Template,
                    name = "测试模板",
                    url = MachineUrl.Template(
                        server = "am.sspu.edu.cn",
                        id = "test-java-pack-aabb-0000",
                        author = "RustGod"
                    )
                ),
                MachineData(
                    type = MachineType.Group,
                    name = "测试组",
                    url = MachineUrl.Group(
                        server = "am.sspu.edu.cn",
                        id = "test-java-pack-aabb-6666",
                    )
                )
            )
        )
    )

    application {
        ui.initUI()

        LaunchedEffect(Unit) {
            ui.scope.launch {
                ui.snack.showSnackbar("开发阶段自动启用开发者模式, 若此时非开发阶段, 请联系开发团队, 关闭产品默认启用的开发者模式")
            }
        }

        val undecoratedState = remember { mutableStateOf(true) }
        val transparentState = remember { mutableStateOf(true) }
        val windowState = rememberWindowState(
//        position = WindowPosition(
//            x = 490.dp,
//            y = 270.dp
//        ),
//        width = 960.dp,
//        height = 540.dp
        )
        val undecorated by undecoratedState
        val transparent by transparentState

        var inMinimize by remember { mutableStateOf(false) }
        var inMaximize by remember { mutableStateOf(false) }
        var inExit by remember { mutableStateOf(false) }

        val screen by ui.screen.collectAsState()
        val theme by ui.settings.theme.current.collectAsState()
        val colorScheme by ui.theme.collectAsState()

        val font by ui.font.collectAsState()

        if (screen.meta == MetaPages.exit) exitApplication()

        Window(
            onCloseRequest = ::exitApplication,
            title = "${Information.Application.NAME}·${Information.Application.VERSION}(${theme.text})",
            icon = painterResource(Res.drawable.am),
            state = windowState,
            undecorated = undecorated,
            transparent = transparent
        ) {
            MaterialTheme(
                colorScheme = colorScheme
            ) {
                Card(
                    shape = if (undecorated)
                        if (windowState.placement == WindowPlacement.Fullscreen) MaterialTheme.shapes.extraSmall
                        else MaterialTheme.shapes.extraLarge
                    else MaterialTheme.shapes.extraSmall
                ) {
                    Column {
                        WindowDraggableArea(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            window.placement = if (windowState.placement == WindowPlacement.Floating)
                                                WindowPlacement.Fullscreen
                                            else
                                                WindowPlacement.Floating
                                        }
                                    )
                                }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(24.dp, 6.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.am),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .size(25.dp)
                                        .align(Alignment.CenterStart)
                                )

                                Text(
                                    text = "${Information.Application.NAME}·${Information.Application.VERSION}",
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = font,
                                    overflow = TextOverflow.Visible,
                                    softWrap = false
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .background(MaterialTheme.colorScheme.background)
                                ) {
                                    Row {
                                        Box(
                                            modifier = Modifier
                                                .padding(6.dp, 0.dp)
                                                .align(Alignment.CenterVertically)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    windowState.isMinimized = true
                                                },
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(CircleShape)
                                                    .onPointerEvent(PointerEventType.Enter) {
                                                        inMinimize = true
                                                    }
                                                    .onPointerEvent(PointerEventType.Exit) {
                                                        inMinimize = false
                                                    },
                                                colors = IconButtonDefaults.iconButtonColors(
                                                    containerColor = Color(0xFF3DE1AD),
                                                )
                                            ) {
                                                if (inMinimize) {
                                                    Icon(
                                                        Icons.Outlined.Remove,
                                                        contentDescription = "Minimize",
                                                        modifier = Modifier
                                                            .size(12.dp),
                                                        tint = MaterialTheme.colorScheme.inversePrimary
                                                    )
                                                }
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .padding(6.dp, 0.dp)
                                                .align(Alignment.CenterVertically)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    if (windowState.placement == WindowPlacement.Floating) {
                                                        windowState.placement = WindowPlacement.Fullscreen
                                                    } else {
                                                        windowState.placement = WindowPlacement.Floating
                                                    }
                                                },
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(CircleShape)
                                                    .onPointerEvent(PointerEventType.Enter) {
                                                        inMaximize = true
                                                    }
                                                    .onPointerEvent(PointerEventType.Exit) {
                                                        inMaximize = false
                                                    },
                                                colors = IconButtonDefaults.iconButtonColors(
                                                    containerColor = Color(0xFFFF7500),
                                                )
                                            ) {
                                                if (inMaximize) {
                                                    Icon(
                                                        Icons.Outlined.Crop54,
                                                        contentDescription = "Maximize",
                                                        modifier = Modifier
                                                            .size(12.dp),
                                                        tint = MaterialTheme.colorScheme.inversePrimary
                                                    )
                                                }
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .padding(6.dp, 0.dp)
                                                .align(Alignment.CenterVertically)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    exitApplication()
                                                },
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(CircleShape)
                                                    .onPointerEvent(PointerEventType.Enter) {
                                                        inExit = true
                                                    }
                                                    .onPointerEvent(PointerEventType.Exit) {
                                                        inExit = false
                                                    },
                                                colors = IconButtonDefaults.iconButtonColors(
                                                    containerColor = Color(0xFFFF00056),
                                                )
                                            ) {
                                                if (inExit) {
                                                    Icon(
                                                        Icons.Outlined.Close,
                                                        contentDescription = "Exit",
                                                        modifier = Modifier
                                                            .size(12.dp),
                                                        tint = MaterialTheme.colorScheme.inversePrimary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Row {
                            DesktopNavigationBar(
                                ui = ui,
                                modifier = Modifier.fillMaxHeight(),
                                labelVisibility = remember { mutableStateOf(true) }
                            )

                            Scaffold(
                                snackbarHost = {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        SnackbarHost(
                                            hostState = ui.snack,
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .alpha(0.8f)
                                        )
                                    }
                                }
                            ) {
                                DesktopApp(
                                    ui = ui
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}