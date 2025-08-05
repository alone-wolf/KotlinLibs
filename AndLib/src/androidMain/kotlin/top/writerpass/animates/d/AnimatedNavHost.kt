package top.writerpass.animates.d

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import top.writerpass.cmplibrary.LaunchedEffectOdd

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
        when (state) {
            is FlipZoomNavState.Idle -> {}
            is FlipZoomNavState.Enter -> {
                LaunchedEffectOdd {
                    val destination = state.destination
                    controller.navController.navigate(destination)
                    controller.toIdle1()
                }
            }

            is FlipZoomNavState.Idle1 -> {}

            is FlipZoomNavState.Exit -> {
                LaunchedEffectOdd {
                    controller.navController.popBackStack()
                    controller.returnToIdle()
                }
            }
        }


//        if (state.isAnimating) {
//            FlipZoomAnimationLayer(
//                sourceBounds = state.sourceBounds,
//                sourceSize = state.sourceSize,
//                onAnimationEnd = {
//                    controller.navState = state.copy(isAnimating = false)
//                }
//            )
//        } else {
//            state.destination?.let {
//                controller.navController.navigate(it)
//            }
//        }

        // 真正的 NavHost
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            builder = builder
        )
    }
}