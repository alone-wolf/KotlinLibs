package top.writerpass.cmplibrary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun LaunchedEffectOdd(block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(Unit, block)
}