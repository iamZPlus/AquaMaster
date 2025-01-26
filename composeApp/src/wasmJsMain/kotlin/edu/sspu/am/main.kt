package edu.sspu.am

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val ui = UI()
    ui.init()

    ui setDevice getDeviceType()

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


    ComposeViewport(document.body!!) {
        ui.initUI()
        val theme by ui.settings.theme.current.collectAsState()
        val colorScheme by ui.theme.collectAsState()
        val device by ui.device.collectAsState()

        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Row {
                    if (device == DeviceType.Desktop) {
                        DesktopNavigationBar(
                            ui = ui,
                            modifier = Modifier.fillMaxHeight(),
                            labelVisibility = remember { mutableStateOf(true) }
                        )
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
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
                            bottomBar = {
                                if (device == DeviceType.MobilePhone) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    ) {
                                        MobilePhoneNavigationBar(ui = ui, modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            },
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = it.calculateTopPadding(),
                                        bottom = it.calculateBottomPadding()
                                    )
                            ) {
                                App(
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

fun getDeviceType(): DeviceType = when {
    setOf(
        "Android",
        "webOS",
        "iPhone",
        "iPad",
        "iPod",
        "BlackBerry",
        "IEMobile",
        "Opera Mini"
    ).map { window.navigator.userAgent.lowercase().contains(it.lowercase()) }.any { it } -> DeviceType.MobilePhone

    else -> DeviceType.Desktop
}



