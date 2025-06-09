package io.github.kotlin.fibonacci.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.tiltOnHover(): Modifier = composed {
    val rotationX = remember { Animatable(0f) }
    val rotationY = remember { Animatable(0f) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val scope = rememberCoroutineScope()
    val maxRotation = 10f

    return@composed this.onSizeChanged { boxSize = it }
        .onPointerEvent(PointerEventType.Move) { event ->
            val offset = event.changes.first().position

            val centerX = boxSize.width / 2f
            val centerY = boxSize.height / 2f
            val dx = offset.x - centerX
            val dy = offset.y - centerY

            val normX = (dx / centerX).coerceIn(-1f, 1f)
            val normY = (dy / centerY).coerceIn(-1f, 1f)

            scope.launch {
                rotationY.snapTo(normX * maxRotation)
                rotationX.snapTo(-normY * maxRotation)
            }
        }
        // 鼠标进入区域
        .onPointerEvent(PointerEventType.Enter) {
            // 可用于状态切换
        }
        // 鼠标离开区域时复位
        .onPointerEvent(PointerEventType.Exit) {
            scope.launch {
                rotationX.animateTo(0f, tween(300))
                rotationY.animateTo(0f, tween(300))
            }
        }
        .graphicsLayer(
            rotationX = rotationX.value,
            rotationY = rotationY.value,
            cameraDistance = 12 * LocalDensity.current.density
        )
}