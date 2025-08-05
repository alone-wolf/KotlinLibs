package top.writerpass.animates.d

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavHostController

class FlipZoomNavController(
    val navController: NavHostController
) {
    var navState: FlipZoomNavState by mutableStateOf(FlipZoomNavState.Idle)
        private set
    private var bounds: Rect by mutableStateOf(Rect.Zero)
    private var size: IntSize by mutableStateOf(IntSize.Zero)

    fun toIdle() {
        navState = FlipZoomNavState.Idle
        bounds = Rect.Zero
        size = IntSize.Zero
    }

    fun toEnter(
        sourceBounds: Rect,
        sourceSize: IntSize,
        destination: String
    ) {
        bounds = sourceBounds
        size = sourceSize
        navState = FlipZoomNavState.Enter(
            destination = destination
        )
    }

    fun toIdle1() {
        navState = FlipZoomNavState.Idle1
    }

    fun toExit() {
        navState = FlipZoomNavState.Exit
    }

    fun returnToIdle() {
        toIdle()
    }
}

val NavHostController.flipZoomController: FlipZoomNavController
    get() = FlipZoomControllerHolder.getOrCreate(this)

fun NavHostController.navigateWithFlipZoom(
    route: String,
    sourceBounds: Rect,
    sourceSize: IntSize
) {
    flipZoomController.toEnter(
        sourceBounds = sourceBounds,
        sourceSize = sourceSize,
        destination = route
    )
}

fun NavHostController.popWithFlipZoom(
) {
    flipZoomController.toExit()
}
