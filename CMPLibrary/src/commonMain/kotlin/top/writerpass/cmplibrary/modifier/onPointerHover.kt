package top.writerpass.cmplibrary.modifier

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import top.writerpass.cmplibrary.utils.Mutable

fun Modifier.onPointerHover(onHover: () -> Unit): Modifier = composed {
    var entered by Mutable.someBoolean()

    LaunchedEffect(entered) {
        if (entered) {
            onHover()
        }
    }
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                when (event.type) {
                    PointerEventType.Enter -> {
                        entered = true
                    }

                    PointerEventType.Exit -> {
                        entered = false
                    }
                }
            }
        }
    }
}

fun Modifier.onPointerHover(onHover: () -> Unit, onNotHover: () -> Unit): Modifier = composed {
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                when (event.type) {
                    PointerEventType.Enter -> {
                        onHover()
                    }

                    PointerEventType.Exit -> {
                        onNotHover()
                    }
                }
            }
        }
    }
}