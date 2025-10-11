@file:OptIn(ExperimentalComposeUiApi::class)

package top.writerpass.cmplibrary.modifier

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent

fun Modifier.onPointerRightClick(block:(Offset)-> Unit): Modifier = composed {
    onPointerEvent(PointerEventType.Press) { e ->
        if (e.buttons.isSecondaryPressed) {
            val pos = e.changes.first().position
            block(pos)
        }
    }
}