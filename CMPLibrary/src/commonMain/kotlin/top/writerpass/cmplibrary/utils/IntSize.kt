package top.writerpass.cmplibrary.utils

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

fun IntSize.toDp(density: Density): DpSize {
    return with(density) {
        DpSize(
            width = width.toDp(),
            height = height.toDp()
        )
    }
}