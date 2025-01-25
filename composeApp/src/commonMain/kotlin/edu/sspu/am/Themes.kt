package edu.sspu.am

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object ColorSchemes {
    val light = lightColorScheme(
        // 明色
        primary = Color(0xFF3DE1AD),
        onPrimary = Color(0xFF21A675),
        primaryContainer = Color(0xFFA4E2C6),
        onPrimaryContainer = Color(0xFF9ED048),
        background = Color(0xFFCCE8CF),
        inversePrimary = Color(0xFF000000), // Font Color

        // 灰色
        tertiary = Color(0xFF666666), // 按钮disable时的颜色
        onTertiary = Color(0xFF333333), // 文字disable时的颜色
        tertiaryContainer = Color(0xFFCCCCCC),
        onTertiaryContainer = Color(0xFF888888),
        onBackground = Color(0xFFDDDDDD), // 背景disable时的颜色
    )
    val dark = darkColorScheme(
        // 明色
        primary = Color(0xFF666666),
        onPrimary = Color(0xFF888888),
        primaryContainer = Color(0xFF333333),
        onPrimaryContainer = Color(0xFF444444),
        background = Color(0xFF111111),
        inversePrimary = Color(0xFFf3f9f1), // Font Color

        // 灰色
        tertiary = Color(0xFF808080), // 按钮disable时的颜色
        onTertiary = Color(0xFFE0E0E0), // 文字disable时的颜色
        tertiaryContainer = Color(0xFF333333),
        onTertiaryContainer = Color(0xFF666666),
        onBackground = Color(0xFF222222), // 背景disable时的颜色
    )
}