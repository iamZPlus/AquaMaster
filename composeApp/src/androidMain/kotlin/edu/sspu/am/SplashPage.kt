package edu.sspu.am

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kottieAnimation.KottieAnimation
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

@Composable
fun SplashPage(
    ui: UI,
    modifier: Modifier = Modifier,
    duration: Long = 0
) {
    val screen by ui.screen.collectAsState()
    val font by ui.font.collectAsState()

    var showing by remember { mutableStateOf(true) }

    val loadingComposition = rememberKottieComposition(spec = KottieCompositionSpec.JsonString(LOADING_0))
    val loadingAnimationState by animateKottieCompositionAsState(
        composition = loadingComposition,
        isPlaying = showing,
        iterations = KottieConstants.IterateForever
    )

    LaunchedEffect(Unit) {
        ui goto MetaScreens.splash
        delay(duration)
        showing = false
        ui goto MetaScreens.app
    }

    AnimatedVisibility(ui.childMatch(MetaScreens.splash)) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            KottieAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(128.dp),
                composition = loadingComposition,
                progress = { loadingAnimationState.progress }
            )
            Image(
                painter = painterResource(R.drawable.sspu),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )
        }
    }
}
