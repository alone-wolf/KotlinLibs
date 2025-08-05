package top.writerpass.animates.c

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun FlipZoomAnimationLayer(
    sourceBounds: Rect,         // 起始位置（全局坐标）
    sourceSize: IntSize,        // 起始尺寸
    onAnimationEnd: () -> Unit
) {
    val density = LocalDensity.current
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val targetSize = with(density) {
        IntSize(screenWidth.roundToPx(), screenHeight.roundToPx())
    }

    // 动画进度（0f ~ 1f）
    var progress by remember { mutableFloatStateOf(0f) }

    // 启动动画
    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ) { value, _ ->
            progress = value
        }
        onAnimationEnd()
    }

    // 插值大小
    val currentWidth = lerp(sourceSize.width, targetSize.width, progress)
    val currentHeight = lerp(sourceSize.height, targetSize.height, progress)

    // 插值位置
    val currentOffsetX = lerp(sourceBounds.left.toInt(), 0, progress)
    val currentOffsetY = lerp(sourceBounds.top.toInt(), 0, progress)

    // 插值旋转
    val rotationY = lerp(0f, 180f, progress)

    Box(
        modifier = Modifier
            .offset { IntOffset(currentOffsetX, currentOffsetY) }
            .size(
                width = with(density) { currentWidth.toDp() },
                height = with(density) { currentHeight.toDp() }
            )
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 12 * density.density
            }
            .background(Color.White, shape = RoundedCornerShape(8.dp)) // 示例内容
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
    ) {
        // 可以在这里放一个 loading 或缩略图
    }
}
fun lerp(start: Int, end: Int, progress: Float): Int {
    return (start + (end - start) * progress).toInt()
}

fun lerp(start: Float, end: Float, progress: Float): Float {
    return start + (end - start) * progress
}