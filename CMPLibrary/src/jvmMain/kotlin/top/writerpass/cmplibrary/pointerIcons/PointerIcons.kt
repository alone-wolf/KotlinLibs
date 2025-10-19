package top.writerpass.cmplibrary.pointerIcons

import androidx.compose.ui.input.pointer.PointerIcon
import java.awt.Cursor

val PointerIcon.Companion.XResize
    get() = PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR))

val PointerIcon.Companion.YResize
    get() = PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))