package top.writerpass.kmplibrary.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun <T> withContextIO(
    block: suspend CoroutineScope.() -> T
) = withContext(
    context = PlatformDispatchers.IO,
    block = block
)

suspend fun <T> withContextDefault(
    block: suspend CoroutineScope.() -> T
) = withContext(
    context = Dispatchers.Default,
    block = block
)

suspend fun <T> withContextMain(
    block: suspend CoroutineScope.() -> T
) = withContext(
    context = Dispatchers.Main,
    block = block
)