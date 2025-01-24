package edu.sspu.am

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
expect fun Settings(ui: UI, modifier: Modifier = Modifier)

@Composable
fun SettingsSwitch(
    ui: UI,
    modifier: Modifier = Modifier,
    text: String = "",
    checked: Boolean = false,
    enabled: Boolean = true,
    thumbContent: @Composable() (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val font by ui.font.collectAsState()

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterStart),
            fontSize = 18.sp,
            color = if (enabled)
                MaterialTheme.colorScheme.inversePrimary
            else
                MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium,
            fontFamily = font,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Switch(
            modifier = Modifier.align(Alignment.CenterEnd),
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbContent = thumbContent,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                checkedIconColor = MaterialTheme.colorScheme.inversePrimary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onTertiaryContainer,
                uncheckedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                uncheckedBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
                uncheckedIconColor = MaterialTheme.colorScheme.onTertiary,
                disabledCheckedThumbColor = MaterialTheme.colorScheme.onTertiaryContainer,
                disabledCheckedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledCheckedBorderColor = MaterialTheme.colorScheme.onTertiaryContainer,
                disabledCheckedIconColor = MaterialTheme.colorScheme.onTertiary,
                disabledUncheckedThumbColor = MaterialTheme.colorScheme.onTertiaryContainer,
                disabledUncheckedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledUncheckedBorderColor = MaterialTheme.colorScheme.onTertiaryContainer,
                disabledUncheckedIconColor = MaterialTheme.colorScheme.onTertiary
            )
        )
    }
}


