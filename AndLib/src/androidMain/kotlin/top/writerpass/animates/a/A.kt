package top.writerpass.animates.a

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlin.math.roundToInt

@Composable
fun CardFlipDemo() {
    FlippableCard(
        modifier = Modifier
            .size(200.dp, 300.dp)
            .padding(16.dp),
        front = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text("Front", color = Color.White, fontSize = 24.sp)
            }
        },
        back = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Text("Back", color = Color.White, fontSize = 24.sp)
            }
        }
    )
}

@Composable
fun FullScreenFlipCard(
    front: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text("Front", fontSize = 24.sp, color = Color.White)
        }
    },
    back: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.rotationY = 180f
                } // 翻转后还原方向
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text("Back", fontSize = 24.sp, color = Color.White)
        }
    }
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current

    var flipped by remember { mutableStateOf(false) }

    // 控制动画进度（0f 到 1f）
    val progress by animateFloatAsState(
        targetValue = if (flipped) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "flipProgress"
    )

    // 衍生出角度、宽高、偏移
    val rotationY = progress * 180f

    // 小方块起始大小
    val startSize = 100.dp
    val animatedWidth = lerp(startSize, screenWidth, progress)
    val animatedHeight = lerp(startSize, screenHeight, progress)

    // 小方块起始位置：屏幕居中
    val offsetX = with(density) { ((screenWidth - startSize) / 2).toPx() }
    val offsetY = with(density) { ((screenHeight - startSize) / 2).toPx() }

    val animatedOffsetX = lerp(offsetX, 0f, progress)
    val animatedOffsetY = lerp(offsetY, 0f, progress)

    val cameraDistance = 12 * density.density

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { flipped = !flipped },
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = animatedOffsetX.roundToInt(),
                        y = animatedOffsetY.roundToInt()
                    )
                }
                .size(width = animatedWidth, height = animatedHeight)
                .graphicsLayer {
                    this.rotationY = rotationY
                    this.cameraDistance = cameraDistance
                }
                .background(Color.Transparent)
        ) {
            if (rotationY <= 90f) {
                front()
            } else {
                back()
            }
        }
    }
}


@Composable
fun FlippableCard(
    modifier: Modifier = Modifier,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
) {
    var flipped by remember { mutableStateOf(false) }
    val rotation = animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "flipAnimation"
    )

    val cameraDistance = 12f * LocalDensity.current.density

    Box(
        modifier = modifier
            .clickable { flipped = !flipped }
            .graphicsLayer {
                rotationY = rotation.value
                this.cameraDistance = cameraDistance
            }
    ) {
        if (rotation.value <= 90f) {
            Box(modifier = Modifier.fillMaxSize()) {
                front()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f
                    }
            ) {
                back()
            }
        }
    }
}