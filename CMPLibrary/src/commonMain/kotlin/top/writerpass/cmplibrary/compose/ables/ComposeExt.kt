package top.writerpass.cmplibrary.compose.ables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

//object ComposeExt : ImageComposeExt by IconComposeExt,
//    StringComposeExt by TextComposeExt,
//    StateComposeExt by MutableStateComposeExt {

//    @Composable
//    fun Scope(block: @Composable ComposeExt.() -> Unit){
//        block()
//    }
//}

//@Composable
//fun String.Composables(block: @Composable ComposeExt.(String) -> Unit) {
//    ComposeExt.block(this)
//}
//
//@Composable
//fun ImageVector.Composables(block: @Composable ComposeExt.(ImageVector) -> Unit) {
//    ComposeExt.block(this)
//}
//
//@Composable
//fun Painter.Composables(block: @Composable ComposeExt.(Painter) -> Unit) {
//    ComposeExt.block(this)
//}
//
//@Composable
//fun <T> MutableState<T>.Composables(block: ComposeExt.(MutableState<T>) -> Unit) {
//    ComposeExt.block(this)
//}