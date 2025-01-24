package edu.sspu.am

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import aquamaster.composeapp.generated.resources.Res
import aquamaster.composeapp.generated.resources.tongyi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QwenButton(
    ui: UI,
    modifier: Modifier = Modifier,
) {
    val enable by ui.settings.ai.enable.collectAsState()

    Card(
        modifier = modifier
            .size(50.dp)
            .alpha(0.7f)
            .clip(CircleShape)
            .combinedClickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null,
                onLongClick = { ui.settings.developerMode.screenJumper.switch() },
                onDoubleClick = {}
            ) {
                ui.settings.ai.switch()
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Icon(
            painter = painterResource(Res.drawable.tongyi),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally),
            tint = if (enable)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.primary
        )
    }
}