package top.writerpass.animates.d

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize

//data class FlipZoomNavState(
//    val sourceBounds: Rect = Rect.Companion.Zero,
//    val sourceSize: IntSize = IntSize.Companion.Zero,
//    val isAnimating: Boolean = false,
//    val destination: String? = null,
//)

sealed class FlipZoomNavState {
    object Idle : FlipZoomNavState()
    class Enter(val destination: String) : FlipZoomNavState()
    object Idle1 : FlipZoomNavState()
    object Exit : FlipZoomNavState()
}

// Idle -> Enter -> Idle1 -> Exit -> Idle