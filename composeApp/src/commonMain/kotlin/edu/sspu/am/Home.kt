package edu.sspu.am

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun Home(
    ui: UI,
    modifier: Modifier = Modifier
)
