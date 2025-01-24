package edu.sspu.am

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Settings(ui: UI, modifier: Modifier) {
    when {
        ui.childMatch("@app.Settings.other.DeveloperMode") -> DeveloperModeSettings(ui, modifier)
    }
}