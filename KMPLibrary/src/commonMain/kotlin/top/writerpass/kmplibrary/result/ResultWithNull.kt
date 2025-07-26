package top.writerpass.kmplibrary.result

sealed interface ResultWithNull {
    class Success<T>(val data: T) : ResultWithNull
    class Failed(val exception: Throwable) : ResultWithNull

    companion object {
        fun <T> successfully(data: T): Success<T> {
            return Success(data)
        }

        fun unsuccessfully(exception: Throwable): Failed {
            return Failed(exception)
        }
    }
}

suspend inline fun <reified T : Any> ResultWithNull.onSuccess(block: suspend (T) -> Unit): ResultWithNull {
    if (this is ResultWithNull.Success<*> && data != null) {
        block(data as T)
    }
    return this
}

suspend inline fun ResultWithNull.onNull(block: suspend () -> Unit): ResultWithNull {
    if (this is ResultWithNull.Success<*> && data == null) {
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
        ResultWithNull.successfully(block())
    } catch (e: Exception) {
        ResultWithNull.unsuccessfully(e)
    }
}