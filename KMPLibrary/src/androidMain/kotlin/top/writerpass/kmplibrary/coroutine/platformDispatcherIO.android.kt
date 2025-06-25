package top.writerpass.kmplibrary.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun platformDispatcherIO(): CoroutineDispatcher {
    return Dispatchers.IO
}