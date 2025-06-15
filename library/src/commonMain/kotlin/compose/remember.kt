package io.github.kotlin.fibonacci.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.cache
import androidx.compose.runtime.currentComposer

@Composable
inline fun <T> r(crossinline calculation: @DisallowComposableCalls () -> T): T =
    currentComposer.cache(false, calculation)