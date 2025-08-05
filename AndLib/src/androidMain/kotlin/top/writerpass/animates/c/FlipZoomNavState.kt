package top.writerpass.animates.c

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize

data class FlipZoomNavState(
    val sourceBounds: Rect = Rect.Companion.Zero,
    val sourceSize: IntSize = IntSize.Companion.Zero,
    val isAnimating: Boolean = false,
    val destination: String? = null,
)