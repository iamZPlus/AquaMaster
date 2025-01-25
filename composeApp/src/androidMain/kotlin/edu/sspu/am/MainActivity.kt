package edu.sspu.am

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        context = this

        val ui = UI()
        ui.init()

        ui setDevice DeviceType.MobilePhone

        ui.settings.developerMode.on()
        ui.settings.developerMode.fastJumper.one.target set "@app.Settings.other.DeveloperMode"

        // TODO: 正式上线时删除这部分
        ui.data.list.append(
            MachineCategory(
                name = "测试",
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


        setContent {
            ui.initUI()

            LaunchedEffect(Unit) {
                ui.scope.launch {
                    ui.snack.showSnackbar("开发阶段自动启用开发者模式, 若此时非开发阶段, 请联系开发团队, 关闭产品默认启用的开发者模式")
                }
            }

            val screen by ui.screen.collectAsState()
            val theme by ui.settings.theme.current.collectAsState()
            val colorScheme by ui.theme.collectAsState()

            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = when (theme.theme) {
                Themes.Light -> true
                Themes.Dark -> false
                Themes.Auto -> !isSystemInDarkTheme()
            }

            BackHandler { ui.back() }

            if (screen.meta == MetaPages.exit) (LocalContext.current as? Activity)?.finish()

            MaterialTheme(
                colorScheme = colorScheme
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Surface(
                        modifier = Modifier
                            .statusBarsPadding()
                            .systemBarsPadding()
                            .navigationBarsPadding()
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            floatingActionButton = {
                                Box(
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    QwenButton(
                                        ui = ui,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        ) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
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
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    ) {
                                        AndroidNavigationBar(ui = ui, modifier = Modifier.fillMaxWidth())
                                    }
                                },
                                snackbarHost = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding()
                                    ) {
                                        SnackbarHost(
                                            hostState = ui.snack,
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .alpha(0.8f)
                                        )
                                    }
                                },
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            top = it.calculateTopPadding(),
                                            bottom = it.calculateBottomPadding(),
                                        )
                                ) {
                                    App(
                                        ui = ui
                                    )
                                }
                            }
                        }

                        // 启动加载页面
                        SplashPage(ui = ui, duration = 2000)
                    }
                }
            }
        }
    }
}

