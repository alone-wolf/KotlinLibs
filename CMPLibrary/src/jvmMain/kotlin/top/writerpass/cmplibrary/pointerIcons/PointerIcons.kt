package top.writerpass.cmplibrary.pointerIcons

import androidx.compose.ui.input.pointer.PointerIcon
import java.awt.Cursor
import kotlin.invoke

val PointerIcon.Companion.XResize
    get() = PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR))