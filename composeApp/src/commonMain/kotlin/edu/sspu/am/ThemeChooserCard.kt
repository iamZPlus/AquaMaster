package edu.sspu.am

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Themes { Light, Dark, Auto }

data class Theme(val text: String, val theme: Themes, val icon: ImageVector, val iconFilled: ImageVector)

val NameToTheme = mapOf(
    Themes.Light to Theme(
        text = "日间",
        theme = Themes.Light,
        icon = Icons.Outlined.LightMode,
        iconFilled = Icons.Filled.LightMode
    ),
    Themes.Auto to Theme(
        text = "自动",
        theme = Themes.Auto,
        icon = Icons.Outlined.Contrast,
        iconFilled = Icons.Filled.Contrast
    ),
    Themes.Dark to Theme(
        text = "夜间",
        theme = Themes.Dark,
        icon = Icons.Outlined.DarkMode,
        iconFilled = Icons.Filled.DarkMode
    ),
)

val themes = listOf(
    Theme(
        text = "日间",
        theme = Themes.Light,
        icon = Icons.Outlined.LightMode,
        iconFilled = Icons.Filled.LightMode
    ),
    Theme(
        text = "自动",
        theme = Themes.Auto,
        icon = Icons.Outlined.Contrast,
        iconFilled = Icons.Filled.Contrast
    ),
    Theme(
        text = "夜间",
        theme = Themes.Dark,
        icon = Icons.Outlined.DarkMode,
        iconFilled = Icons.Filled.DarkMode
    ),
)

@Composable
@Preview
fun ThemeChooser(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val font by ui.font.collectAsState()
    val current by ui.settings.theme.current.collectAsState()

    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        LazyRow {
            items(themes) { theme ->
                val bg by animateColorAsState(
                    targetValue = MaterialTheme.colorScheme.onPrimaryContainer,
                    animationSpec = tween(durationMillis = 300)
                )
                Card(
                    modifier = Modifier
                        .clickable { ui.settings.theme set theme },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (theme == current) bg else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = if (theme == current) theme.iconFilled else theme.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                        Text(
                            text = theme.text,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .align(Alignment.CenterVertically),
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = font
                        )
                    }
                }
            }
        }
    }
}




