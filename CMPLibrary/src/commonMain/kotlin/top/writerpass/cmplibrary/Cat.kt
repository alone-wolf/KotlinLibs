package top.writerpass.cmplibrary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BlackCatCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val headRadius = size.minDimension * 0.3f
        val earSize = headRadius * 0.6f
        val eyeRadius = headRadius * 0.15f

        // Head
        drawCircle(
            color = Color.Black,
            radius = headRadius,
            center = Offset(centerX, centerY)
        )

        // Ears (triangles)
        val earOffsetX = headRadius * 0.7f
        val earOffsetY = headRadius * 0.8f
        drawPath(
            path = Path().apply {
                moveTo(centerX - earOffsetX, centerY - earOffsetY)
                lineTo(centerX - earOffsetX - earSize / 2, centerY - earOffsetY - earSize)
                lineTo(centerX - earOffsetX + earSize / 2, centerY - earOffsetY - earSize)
                close()
            },
            color = Color.Black
        )
        drawPath(
            path = Path().apply {
                moveTo(centerX + earOffsetX, centerY - earOffsetY)
                lineTo(centerX + earOffsetX - earSize / 2, centerY - earOffsetY - earSize)
                lineTo(centerX + earOffsetX + earSize / 2, centerY - earOffsetY - earSize)
                close()
            },
            color = Color.Black
        )

        // Eyes
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(centerX - headRadius * 0.4f, centerY - headRadius * 0.2f)
        )
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(centerX + headRadius * 0.4f, centerY - headRadius * 0.2f)
        )

        // Pupils
        drawCircle(
            color = Color.Black,
            radius = eyeRadius / 2,
            center = Offset(centerX - headRadius * 0.4f, centerY - headRadius * 0.2f)
        )
        drawCircle(
            color = Color.Black,
            radius = eyeRadius / 2,
            center = Offset(centerX + headRadius * 0.4f, centerY - headRadius * 0.2f)
        )

        // Nose
        drawCircle(
            color = Color.DarkGray,
            radius = eyeRadius / 2.5f,
            center = Offset(centerX, centerY)
        )

        // Whiskers (left)
        for (i in -1..1) {
            drawLine(
                color = Color.DarkGray,
                strokeWidth = 3f,
                start = Offset(centerX - headRadius * 0.2f, centerY + i * 10f),
                end = Offset(centerX - headRadius * 1.1f, centerY + i * 10f)
            )
        }

        // Whiskers (right)
        for (i in -1..1) {
            drawLine(
                color = Color.DarkGray,
                strokeWidth = 3f,
                start = Offset(centerX + headRadius * 0.2f, centerY + i * 10f),
                end = Offset(centerX + headRadius * 1.1f, centerY + i * 10f)
            )
        }
    }
}

@Composable
fun BlackCatHead(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // 比例参考尺寸
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val headRadius = size.minDimension / 2.2f

        // ----- 头部 -----
        drawCircle(
            color = Color.Black,
            radius = headRadius,
            center = Offset(centerX, centerY)
        )

        // ----- 耳朵 -----
        val earHeight = headRadius * 1.2f
        val earWidth = headRadius * 0.6f

        // 左耳
        drawPath(
            path = Path().apply {
                moveTo(centerX - headRadius * 0.9f, centerY - headRadius * 0.6f)
                lineTo(centerX - headRadius * 1.3f, centerY - earHeight)
                lineTo(centerX - headRadius * 0.5f, centerY - headRadius * 0.9f)
                close()
            },
            color = Color.Black
        )
        drawPath(
            path = Path().apply {
                moveTo(centerX - headRadius * 0.85f, centerY - headRadius * 0.65f)
                lineTo(centerX - headRadius * 1.1f, centerY - earHeight * 0.9f)
                lineTo(centerX - headRadius * 0.6f, centerY - headRadius * 0.85f)
                close()
            },
            color = Color(0xFF663333)
        )

        // 右耳
        drawPath(
            path = Path().apply {
                moveTo(centerX + headRadius * 0.9f, centerY - headRadius * 0.6f)
                lineTo(centerX + headRadius * 1.3f, centerY - earHeight)
                lineTo(centerX + headRadius * 0.5f, centerY - headRadius * 0.9f)
                close()
            },
            color = Color.Black
        )
        drawPath(
            path = Path().apply {
                moveTo(centerX + headRadius * 0.85f, centerY - headRadius * 0.65f)
                lineTo(centerX + headRadius * 1.1f, centerY - earHeight * 0.9f)
                lineTo(centerX + headRadius * 0.6f, centerY - headRadius * 0.85f)
                close()
            },
            color = Color(0xFF663333)
        )

        // ----- 眼睛 -----
        val eyeRadiusOuter = headRadius * 0.4f
        val eyeRadiusInner = headRadius * 0.3f
        val pupilRadius = headRadius * 0.15f
        val eyeOffsetX = headRadius * 0.55f

        listOf(-1, 1).forEach { dir ->
            val eyeCenter = Offset(centerX + eyeOffsetX * dir, centerY)
            drawCircle(
                color = Color(0xFFFFF9C4),
                radius = eyeRadiusOuter,
                center = eyeCenter
            ) // outer yellow-white
            drawCircle(
                color = Color(0xFFFFD740),
                radius = eyeRadiusInner,
                center = eyeCenter
            ) // inner yellow

            drawCircle(
                color = Color.Black,
                radius = pupilRadius,
                center = eyeCenter
            ) // pupil

            drawCircle(
                color = Color.White,
                radius = pupilRadius / 3,
                center = Offset(
                    eyeCenter.x - pupilRadius / 2,
                    eyeCenter.y - pupilRadius / 2
                )
            ) // highlight
        }

        // ----- 鼻子 -----
        drawOval(
            color = Color(0xFF3E2723),
            topLeft = Offset(centerX - headRadius * 0.07f, centerY + headRadius * 0.1f),
            size = Size(headRadius * 0.14f, headRadius * 0.1f)
        )

        // ----- 胡须 -----
        val whiskerLength = headRadius * 0.7f
        val whiskerYOffset = listOf(-0.15f, 0f, 0.15f)
        whiskerYOffset.forEach { yOffset ->
            val y = centerY + headRadius * yOffset
            drawLine(Color(0xFF2E2E2E), Offset(centerX - headRadius * 0.2f, y), Offset(centerX - whiskerLength, y), strokeWidth = 4f)
            drawLine(Color(0xFF2E2E2E), Offset(centerX + headRadius * 0.2f, y), Offset(centerX + whiskerLength, y), strokeWidth = 4f)
        }
    }
}


suspend fun animateEarBounce(onUpdate: (Float) -> Unit) {
    val duration = 300
    val bounceHeight = 0.3f // 弹动高度比例

    val steps = 30
    val delayPerStep = duration / steps

    // 向上弹
    for (i in 0 until steps / 2) {
        val value = (i.toFloat() / (steps / 2)) * bounceHeight
        onUpdate(value)
        delay(delayPerStep.toLong())
    }

    // 向下收
    for (i in 0 until steps / 2) {
        val value = bounceHeight - (i.toFloat() / (steps / 2)) * bounceHeight
        onUpdate(value)
        delay(delayPerStep.toLong())
    }

    onUpdate(0f) // 归位
}


@Composable
fun BlackCatCanvasWithEarAnimation(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var earBounce by remember { mutableStateOf(0f) }

    // 定时触发动画
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            animateEarBounce {
                earBounce = it
            }
        }
    }

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val headRadius = size.minDimension * 0.3f
        val earSize = headRadius * 0.6f
        val eyeRadius = headRadius * 0.15f

        val earOffsetX = headRadius * 0.7f
        val earOffsetY = headRadius * 0.8f

        val earBounceOffset = earBounce * earSize // 动画偏移值

        // --- Head ---
        drawCircle(
            color = Color.Black,
            radius = headRadius,
            center = Offset(centerX, centerY)
        )

        // --- Ears ---
        fun drawEar(left: Boolean) {
            val sign = if (left) -1 else 1
            val baseX = centerX + sign * earOffsetX
            val baseY = centerY - earOffsetY

            drawPath(
                path = Path().apply {
                    moveTo(baseX, baseY)
                    lineTo(baseX - earSize / 2, baseY - earSize + earBounceOffset)
                    lineTo(baseX + earSize / 2, baseY - earSize + earBounceOffset)
                    close()
                },
                color = Color.Black
            )
        }

        drawEar(left = true)
        drawEar(left = false)

        // --- Eyes ---
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(centerX - headRadius * 0.4f, centerY - headRadius * 0.2f)
        )
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(centerX + headRadius * 0.4f, centerY - headRadius * 0.2f)
        )

        drawCircle(
            color = Color.Black,
            radius = eyeRadius / 2,
            center = Offset(centerX - headRadius * 0.4f, centerY - headRadius * 0.2f)
        )
        drawCircle(
            color = Color.Black,
            radius = eyeRadius / 2,
            center = Offset(centerX + headRadius * 0.4f, centerY - headRadius * 0.2f)
        )

        // --- Nose ---
        drawCircle(
            color = Color.DarkGray,
            radius = eyeRadius / 2.5f,
            center = Offset(centerX, centerY)
        )

        // --- Whiskers ---
        for (i in -1..1) {
            drawLine(
                color = Color.DarkGray,
                strokeWidth = 3f,
                start = Offset(centerX - headRadius * 0.2f, centerY + i * 10f),
                end = Offset(centerX - headRadius * 1.1f, centerY + i * 10f)
            )
            drawLine(
                color = Color.DarkGray,
                strokeWidth = 3f,
                start = Offset(centerX + headRadius * 0.2f, centerY + i * 10f),
                end = Offset(centerX + headRadius * 1.1f, centerY + i * 10f)
            )
        }
    }
}


@Composable
fun CatScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BlackCatHead(modifier = Modifier.size(300.dp))
//        BlackCatCanvasWithEarAnimation(
//            modifier = Modifier.size(300.dp)
//        )
//        BlackCatCanvas(
//            modifier = Modifier
//                .size(300.dp)
//        )
    }
}

