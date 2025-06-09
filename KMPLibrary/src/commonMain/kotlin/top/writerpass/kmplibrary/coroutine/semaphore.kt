package top.writerpass.kmplibrary.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlin.coroutines.CoroutineContext

inline fun Semaphore.launch(
    scope: CoroutineScope,
    context: CoroutineContext,
    crossinline block: suspend () -> Unit
): Job {
    return scope.launch(context) {
        acquire()
        block()
        release()
    }
}