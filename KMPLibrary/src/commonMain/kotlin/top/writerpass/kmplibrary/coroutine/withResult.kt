package top.writerpass.kmplibrary.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> withContextResult(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): Result<T> {
    return try {
        Result.success(withContext(context, block))
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun <T> withContextIOResult(
    block: suspend CoroutineScope.() -> T
): Result<T> {
    return try {
        Result.success(withContext(PlatformDispatchers.IO, block))
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun <T> withContextMainResult(
    block: suspend CoroutineScope.() -> T
): Result<T> {
    return try {
        Result.success(withContext(Dispatchers.Main, block))
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun <T> withContextDefaultResult(
    block: suspend CoroutineScope.() -> T
): Result<T> {
    return try {
        Result.success(withContext(Dispatchers.Default, block))
    } catch (e: Exception) {
        Result.failure(e)
    }
}