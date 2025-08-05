package top.writerpass.animates.b

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun FlipOverlayPage(
    startBounds: Rect,
    onDismiss: () -> Unit,
    front: @Composable () -> Unit,
    back: @Composable (() -> Unit) -> Unit
) {
    val density = LocalDensity.current
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val progress = remember { Animatable(0f) }
    var flipped by remember { mutableStateOf(false) }

    // 是否请求退出
    var dismissRequested by remember { mutableStateOf(false) }

    // 启动进入动画
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(6000))
        flipped = true
    }

    // 启动退出动画
    LaunchedEffect(dismissRequested) {
        if (dismissRequested) {
            flipped = false
            progress.animateTo(0f, tween(6000))
            onDismiss()
        }
    }

    val rotationY = progress.value * 180f

    val startWidth = startBounds.width
    val startHeight = startBounds.height
    val startOffsetX = startBounds.left
    val startOffsetY = startBounds.top

    val targetWidth = with(density) { screenWidth.toPx() }
    val targetHeight = with(density) { screenHeight.toPx() }

    val animatedWidth = lerp(startWidth, targetWidth, progress.value)
    val animatedHeight = lerp(startHeight, targetHeight, progress.value)
    val animatedOffsetX = lerp(startOffsetX, 0f, progress.value)
    val animatedOffsetY = lerp(startOffsetY, 0f, progress.value)

    val cameraDistance = 40 * density.density

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f * progress.value))
//            .clipToBounds()

    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt())
                }
                .size(
                    width = with(density) { animatedWidth.toDp() },
                    height = with(density) { animatedHeight.toDp() }
                )
//                .clipToBounds()
                .graphicsLayer {
                    clip = true
                    this.rotationY = rotationY
                    this.cameraDistance = cameraDistance
                }
//                .background(Color.Black)

        ) {
            if (rotationY <= 90f) {
                front()
            } else {
                Box(modifier = Modifier.graphicsLayer {
                    this.rotationY = 180f
                    clip = true

                }) {
                    back {
                        dismissRequested = true
                    }
                }
            }
        }
    }
}

//fun Float.ceilToInt(): Int = ceil(this).toInt()

