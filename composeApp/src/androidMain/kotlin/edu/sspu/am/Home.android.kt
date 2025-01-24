package edu.sspu.am

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun Home(ui: UI, modifier: Modifier) {
    val machine by ui.settings.machine.current.collectAsState()
    when {
        ui.match("@app.List") -> MachineListInterface(ui, modifier)
        ui.match("@app.List.MachineController") -> MachineController(
            ui = ui,
            modifier = modifier.padding(horizontal = 12.dp),
            machine = machine
        )
    }
}

@Composable
fun MachineListInterface(
    ui: UI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MachinesList(
            ui = ui,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        )
    }
}

