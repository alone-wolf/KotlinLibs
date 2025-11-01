package top.writerpass.cmplibrary.compose.ables

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

object Composables : ImageComposeExt, StringComposeExt{
    @Composable
    inline fun Scope(block: @Composable Composables.() -> Unit){
        block()
    }
}

@Composable
fun String.Composables(block: @Composable Composables.(String) -> Unit) {
    Composables.block(this)
}

@Composable
fun ImageVector.Composables(block: @Composable Composables.(ImageVector) -> Unit) {
    Composables.block(this)
}

@Composable
fun Painter.Composables(block: @Composable Composables.(Painter) -> Unit) {
    Composables.block(this)
}