package top.writerpass.kmplibrary.result

import top.writerpass.kmplibrary.result.Result.Failed
import top.writerpass.kmplibrary.result.Result.Success

sealed interface Result {
    class Success<T : Any>(val data: T) : Result
    class Failed(val exception: Throwable) : Result

    companion object {
        fun <T : Any> successfully(data: T): Success<T> {
            return Success(data)
        }

        fun unsuccessfully(exception: Throwable): Failed {
            return Failed(exception)
        }
    }
}

suspend inline fun <reified T> Result.onSuccess(block: suspend (T) -> Unit): Result {
    if (this is Success<*>) {
        block(data as T)
    }
    return this
}

suspend fun Result.onFailed(block: suspend (Throwable) -> Unit): Result {
    if (this is Failed) {
        block(exception)
    }
    return this
}

suspend inline fun <reified T : Any> forResult(block: suspend () -> T): Result {
    return try {
        Result.successfully(block())
    } catch (e: Exception) {
        Result.unsuccessfully(e)
    }
}