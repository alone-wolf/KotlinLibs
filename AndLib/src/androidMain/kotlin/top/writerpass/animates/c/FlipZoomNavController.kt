package top.writerpass.animates.c

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavHostController

class FlipZoomNavController(
    val navController: NavHostController
) {
    var navState by mutableStateOf(FlipZoomNavState())
}

val NavHostController.flipZoomController: FlipZoomNavController
    get() = FlipZoomControllerHolder.getOrCreate(this)

fun NavHostController.navigateWithFlipZoom(
    route: String,
    sourceBounds: Rect,
    sourceSize: IntSize
) {
    this.flipZoomController.navState = FlipZoomNavState(
        sourceBounds = sourceBounds,
        sourceSize = sourceSize,
        isAnimating = true,
        destination = route
    )

//    this.navigate(route)
}
