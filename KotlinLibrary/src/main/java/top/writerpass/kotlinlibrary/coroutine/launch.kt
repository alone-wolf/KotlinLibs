package top.writerpass.kotlinlibrary.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.launchIO(
    block: suspend CoroutineScope.() -> Unit
) = launch(
    context = Dispatchers.IO,
    start = CoroutineStart.DEFAULT,
    block = block
)

fun CoroutineScope.launchDefault(
    block: suspend CoroutineScope.() -> Unit
) = launch(
    context = Dispatchers.Default,
    start = CoroutineStart.DEFAULT,
    block = block
)

fun CoroutineScope.launchMain(
    block: suspend CoroutineScope.() -> Unit
) = launch(
    context = Dispatchers.Main,
    start = CoroutineStart.DEFAULT,
    block = block
)