package top.writerpass.cmplibrary.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

actual fun Modifier.tiltOnHover(): Modifier = composed {
    val rotationX = remember { Animatable(0f) }
    val rotationY = remember { Animatable(0f) }

    var inBox by remember { mutableStateOf(false) }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val scope = rememberCoroutineScope()

    var updateJob by remember { mutableStateOf<Job?>(null) }

    return@composed onSizeChanged { boxSize = it }
        .pointerInput(inBox) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val type = event.type
                    val offset = event.changes.firstOrNull()?.position ?: Offset.Zero

                    when (type) {
                        PointerEventType.Enter -> {
                            inBox = true
                        }

                        PointerEventType.Exit -> {
                            inBox = false
                            // 鼠标离开时，回弹动画
                            scope.launch {
                                rotationX.animateTo(0f, tween(300))
                                rotationY.animateTo(0f, tween(300))
                            }
                        }

                        PointerEventType.Move -> {
                            val centerX = boxSize.width / 2f
                            val centerY = boxSize.height / 2f
                            val dx = offset.x - centerX
                            val dy = offset.y - centerY

                            val isInBox = offset.x in 0f..boxSize.width.toFloat() &&
                                    offset.y in 0f..boxSize.height.toFloat()

                            if (!isInBox && inBox) {
                                inBox = false
                                scope.launch {
                                    rotationX.animateTo(0f, tween(300))
                                    rotationY.animateTo(0f, tween(300))
                                }
                                return@awaitPointerEventScope
                            } else if (isInBox && !inBox) {
                                inBox = true
                            }

                            val maxRotation = 10f
                            val normX = (dx / centerX).coerceIn(-1f, 1f)
                            val normY = (dy / centerY).coerceIn(-1f, 1f)

                            updateJob?.cancel()
                            updateJob = scope.launch {
                                rotationY.snapTo(normX * maxRotation)
                                rotationX.snapTo(-normY * maxRotation)
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
        .graphicsLayer(
            rotationX = rotationX.value,
            rotationY = rotationY.value,
            cameraDistance = 12 * LocalDensity.current.density
        )
}