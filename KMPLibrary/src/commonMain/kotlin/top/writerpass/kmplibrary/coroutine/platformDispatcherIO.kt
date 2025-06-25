package top.writerpass.kmplibrary.coroutine

import kotlinx.coroutines.CoroutineDispatcher

object PlatformDispatchers {
    val IO get() = platformDispatcherIO()
}

expect fun platformDispatcherIO(): CoroutineDispatcher
