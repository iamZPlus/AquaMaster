package edu.sspu.am

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    val ui = UI()
    ui.init()
    ui.initUI()

    ui setDevice DeviceType.MobilePhone

    App(ui = ui)
}