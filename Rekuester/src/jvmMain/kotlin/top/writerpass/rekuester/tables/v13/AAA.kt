@file:OptIn(ExperimentalComposeUiApi::class)

package top.writerpass.rekuester.tables.v13

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Stable
class LazySheetState(
    initialScrollX: Float = 0f,
    initialScrollY: Float = 0f
) {
    var scrollX by mutableStateOf(initialScrollX)
        internal set
    var scrollY by mutableStateOf(initialScrollY)
        internal set

    internal var maxScrollX by mutableStateOf(0f)
        private set
    internal var maxScrollY by mutableStateOf(0f)
        private set

    internal fun updateBounds(
        viewportWidthPx: Int, viewportHeightPx: Int, contentWidthPx: Int, contentHeightPx: Int
    ) {
        maxScrollX = max(0, contentWidthPx - viewportWidthPx).toFloat()
        maxScrollY = max(0, contentHeightPx - viewportHeightPx).toFloat()
        scrollX = scrollX.coerceIn(0f, maxScrollX)
        scrollY = scrollY.coerceIn(0f, maxScrollY)
    }

    fun scrollBy(dx: Float, dy: Float) {
        scrollX = (scrollX + dx).coerceIn(0f, maxScrollX)
        scrollY = (scrollY + dy).coerceIn(0f, maxScrollY)
    }

    suspend fun fling(vx: Float, vy: Float) {
        val decay = exponentialDecay<Float>()
        val anim = Animatable(0f)
        // fling X
        var last = 0f
        anim.animateTo(decay.calculateTargetValue(0f, vx), initialVelocity = vx) {
            val delta = value - last
            last = value
            val before = scrollX
            scrollBy(delta, 0f)
            if (scrollX == before) return@animateTo
        }
        // fling Y
        last = 0f
        anim.animateTo(decay.calculateTargetValue(0f, vy), initialVelocity = vy) {
            val delta = value - last
            last = value
            val before = scrollY
            scrollBy(0f, delta)
            if (scrollY == before) return@animateTo
        }
    }
}

@Composable
fun rememberLazySheetState(): LazySheetState = remember { LazySheetState() }

@Composable
fun LazySheet(
    rows: UInt,
    columns: UInt,
    cellSize: DpSize = DpSize(100.dp, 40.dp),
    overscan: Int = 2,
    state: LazySheetState = rememberLazySheetState(),
    modifier: Modifier = Modifier,
    content: @Composable (row: Int, col: Int) -> Unit
) {
    val rows = remember { rows.toInt() }
    val columns = remember { columns.toInt() }
    val density = LocalDensity.current
    val cellW = with(density) { cellSize.width.roundToPx() }
    val cellH = with(density) { cellSize.height.roundToPx() }

    val coroutineScope = rememberCoroutineScope()

    SubcomposeLayout(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures(onDrag = { change, drag ->
                change.consume()
                state.scrollBy(-drag.x, -drag.y)
            }, onDragEnd = {
                // fling 简易实现（常数速度）
                coroutineScope.launch {
                    state.fling(0f, 0f)
                }
            })
        }// === 新增滚轮与触摸板支持 ===
            .onPointerEvent(PointerEventType.Scroll) { event ->
                val scroll = event.changes.first().scrollDelta
                // scrollDelta.y: 向上为正 → 我们希望向上滚时内容上移 → scrollBy(0, +scroll.y)
                // 但视觉上滚轮“向上滚”应让内容上移，所以我们直接使用 scroll.y
                // 如果滚动方向反了，可以改成 state.scrollBy(-scroll.x, -scroll.y)
                state.scrollBy(scroll.x * 10, scroll.y * 10)
            }) { constraints ->
        val vw = constraints.maxWidth
        val vh = constraints.maxHeight
        val cw = columns * cellW
        val ch = rows * cellH

        state.updateBounds(vw, vh, cw, ch)

        val startCol = floor(state.scrollX / cellW).toInt().coerceAtLeast(0)
        val startRow = floor(state.scrollY / cellH).toInt().coerceAtLeast(0)
        val visCols = ceil((vw + (state.scrollX % cellW)) / cellW).toInt()
        val visRows = ceil((vh + (state.scrollY % cellH)) / cellH).toInt()
        val endCol = min(columns - 1, startCol + visCols + overscan)
        val endRow = min(rows - 1, startRow + visRows + overscan)
        val drawStartCol = max(0, startCol - overscan)
        val drawStartRow = max(0, startRow - overscan)

        val placeables = mutableListOf<Triple<Int, Int, Placeable>>()
        for (r in drawStartRow..endRow) {
            for (c in drawStartCol..endCol) {
                val meas = subcompose("cell_${r}_$c") {
                    content(r, c)
                }.first().measure(Constraints.fixed(cellW, cellH))
                placeables += Triple(r, c, meas)
            }
        }

        layout(vw, vh) {
            val offsetX = -(state.scrollX % cellW).toInt()
            val offsetY = -(state.scrollY % cellH).toInt()
            for ((r, c, p) in placeables) {
                val x = (c - startCol) * cellW + offsetX
                val y = (r - startRow) * cellH + offsetY
                p.placeRelative(x, y)
            }
        }
    }
}

/* ---------- 示例 ---------- */

@Composable
fun LazySheetDemo() {
    val state = rememberLazySheetState()

    LazySheet(
        rows = 2000u, columns = 2000u, cellSize = DpSize(96.dp, 40.dp), overscan = 1, state = state
    ) { r, c ->
        val isHeader = r == 0 || c == 0
        val bg = when {
            isHeader -> Color(0xFF2D2D2D)
            (r + c) % 2 == 0 -> Color(0xFF1E1E1E)
            else -> Color(0xFF232323)
        }
        val fg = if (isHeader) Color.Cyan else Color.White
        Box(Modifier.background(bg)) {
            Text(
                text = "R$r C$c",
                color = fg,
                fontSize = 12.sp,
                fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

fun main() = singleWindowApplication {
    LazySheetDemo()
}