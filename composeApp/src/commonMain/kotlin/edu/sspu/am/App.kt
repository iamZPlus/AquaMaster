package edu.sspu.am

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.sspu.am.surprise.Surprise


@Composable
@Preview
fun App(
    ui: UI
) {
    val screen by ui.screen.collectAsState()
    val first by ui.settings.first.current.collectAsState()
    val font by ui.font.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                // 主页
                ui childMatch RootScreens.List -> Home(
                    ui = ui,
                    modifier = Modifier.fillMaxSize()
                )

                ui childMatch RootScreens.Personal -> Personal(
                    ui = ui,
                    modifier = Modifier.fillMaxSize()
                )

                // 设置
                ui childMatch RootScreens.Settings -> Settings(
                    ui = ui,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // 彩蛋页面
        Surprise(ui = ui, modifier = Modifier.fillMaxSize())
    }
}


@Composable
fun TopWidget(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val screen by ui.screen.collectAsState()
    val screenJumperVisible by ui.settings.developerMode.screenJumper.visible.collectAsState()
    val font by ui.font.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        DeveloperTopWidget(ui = ui)
        AnimatedVisibility(screenJumperVisible) {
            ScreenJumper(
                ui = ui
            )
        }
    }
}


