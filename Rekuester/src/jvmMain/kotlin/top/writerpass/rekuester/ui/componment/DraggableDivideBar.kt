package top.writerpass.rekuester.ui.componment

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.modifier.onPointerHover
import top.writerpass.cmplibrary.utils.Mutable

@Composable
fun DraggableDivideBar(value: Dp, onDrag: (Dp) -> Unit) {
    val density = LocalDensity.current
    val draggableState = rememberDraggableState(onDelta = { delta ->
        val result = with(density) {
            (value.toPx() + delta).toDp()
        }.coerceIn(150.dp, 300.dp)
        onDrag(result)
    })

    val onBarHover = Mutable.someBoolean()
    val barWidth by animateDpAsState(if (onBarHover.value) 5.dp else 2.dp)
    Box(
        modifier = Modifier.fillMaxHeight().width(barWidth).background(Color.Gray).onPointerHover(
            onHover = { onBarHover.value = true },
            onNotHover = { onBarHover.value = false }).pointerHoverIcon(
            PointerIcon.Hand
        ).draggable(
            state = draggableState, orientation = Orientation.Horizontal
        )
    )
}