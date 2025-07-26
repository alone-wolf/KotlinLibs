package top.writerpass.kmplibrary.result

sealed interface ResultNullable {
    class Success<T>(val data: T) : ResultNullable
    class Failed(val exception: Throwable) : ResultNullable

    companion object {
        fun <T> successfully(data: T): Success<T> {
            return Success(data)
        }

        fun unsuccessfully(exception: Throwable): Failed {
            return Failed(exception)
        }
    }
}

suspend inline fun <reified T> ResultNullable.onSuccess(block: suspend (T) -> Unit): ResultNullable {
    if (this is ResultNullable.Success<*>) {
        block(data as T)
    }
    return this
}

suspend fun ResultNullable.onFailed(block: suspend (Throwable) -> Unit): ResultNullable {
    if (this is ResultNullable.Failed) {
        block(exception)
    }
    return this
}

suspend fun <T> forResultNullable(block: suspend () -> T): ResultNullable {
    return try {
        ResultNullable.successfully(block())
    } catch (e: Exception) {
        ResultNullable.unsuccessfully(e)
    }
}