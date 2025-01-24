package edu.sspu.am.surprise

import KottieAnimation
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.sspu.am.MetaScreens
import edu.sspu.am.UI
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

@Composable
fun Surprise(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val screen by ui.screen.collectAsState()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            screen == MetaScreens.surprise -> SurpriseHome(ui = ui)
        }
    }
}

@Composable
fun SurpriseHome(
    ui: UI,
    modifier: Modifier = Modifier
) {
    val screen by ui.screen.collectAsState()
    val font by ui.font.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "温馨提示: 彩蛋是存在于本软件中的, 所述软件为AquaMaster远程控制软件, 是浦东第一名校的专业团队所开发!!!",
            modifier = Modifier
                .align(Alignment.TopCenter),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = font
        )

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            val colorfulElectronicWaveComposition = rememberKottieComposition(
                spec = KottieCompositionSpec.JsonString(COLORFUL_ELECTRONIC_WAVE)
            )

            val colorfulElectronicWaveAnimationState by animateKottieCompositionAsState(
                composition = colorfulElectronicWaveComposition,
                isPlaying = screen == MetaScreens.surprise,
                iterations = KottieConstants.IterateForever
            )

            val colorfulShakingPlanetComposition = rememberKottieComposition(
                spec = KottieCompositionSpec.JsonString(COLORFUL_SHAKING_PLANET)
            )

            val colorfulShakingPlanetAnimationState by animateKottieCompositionAsState(
                composition = colorfulShakingPlanetComposition,
                isPlaying = true,
                iterations = KottieConstants.IterateForever
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                KottieAnimation(
                    composition = colorfulShakingPlanetComposition,
                    progress = { colorfulShakingPlanetAnimationState.progress },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(50.dp)
                )
                Text(
                    text = "恭喜你找到了彩蛋页",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF3DE1AD),
                                Color(0xFF1772B4)
                            )
                        )
                    ),
                    fontSize = 32.sp,
                    fontFamily = font,
                    fontWeight = FontWeight.Black
                )
                KottieAnimation(
                    composition = colorfulShakingPlanetComposition,
                    progress = { colorfulShakingPlanetAnimationState.progress },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(50.dp)
                )
            }


            KottieAnimation(
                composition = colorfulElectronicWaveComposition,
                progress = { colorfulElectronicWaveAnimationState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

