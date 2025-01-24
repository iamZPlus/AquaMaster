package edu.sspu.am

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RefreshButton(
    ui: UI,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(25.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable {
                when {
                    ui.matchUnComposable("@app.List") -> {
                        ui.settings.list.refreshing.on()
                        ui.scope.launch {
                            delay(3000)
                            ui.settings.list.refreshing.off()
                        }
                    }

                    ui.matchUnComposable("@app.List.MachineController") -> {
                        ui.settings.machine.refreshing.on()
                        ui.scope.launch {
                            delay(3000)
                            ui.settings.machine.refreshing.off()
                        }
                    }
                }
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}