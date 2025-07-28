package top.writerpass.kmplibrary.result

import kotlin.coroutines.cancellation.CancellationException

sealed interface ResultWithNull {
    object Null : ResultWithNull
    class Success<T : Any>(val data: T) : ResultWithNull
    class Failed(val exception: Throwable) : ResultWithNull
}

suspend inline fun <reified T : Any> ResultWithNull.onSuccess(block: suspend (T) -> Unit): ResultWithNull {
    if (this is ResultWithNull.Success<*>) {
        block(data as T)
    }
    return this
}

suspend inline fun ResultWithNull.onNull(block: suspend () -> Unit): ResultWithNull {
    if (this is ResultWithNull.Null) {
        block()
    }
    return this
}

suspend fun ResultWithNull.onFailed(block: suspend (Throwable) -> Unit): ResultWithNull {
    if (this is ResultWithNull.Failed) {
        block(exception)
    }
    return this
}

suspend fun <T> forResultWithNull(block: suspend () -> T): ResultWithNull {
    return try {
        val data = block()
        if (data == null) ResultWithNull.Null
        else ResultWithNull.Success(data)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        ResultWithNull.Failed(e)
    }
}