package top.writerpass.animates.c

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AnimatedNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
) {
    val controller = navController.flipZoomController
    val state = controller.navState

    Box(modifier = modifier.fillMaxSize()) {
        // 动画层
        if (state.isAnimating) {
            FlipZoomAnimationLayer(
                sourceBounds = state.sourceBounds,
                sourceSize = state.sourceSize,
                onAnimationEnd = {
                    controller.navState = state.copy(isAnimating = false)
                }
            )
        } else {
            state.destination?.let {
                controller.navController.navigate(it)
            }
        }

        // 真正的 NavHost
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            builder = builder
        )
    }
}