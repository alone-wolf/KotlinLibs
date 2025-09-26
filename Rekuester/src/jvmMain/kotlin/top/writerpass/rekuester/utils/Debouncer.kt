package top.writerpass.rekuester.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Debouncer(
    private val scope: CoroutineScope,
    private val waitMs: Long = 5000L,
    private val context: CoroutineContext = Dispatchers.Default,
    private val action: () -> Unit = {}
) {
    private var job: Job? = null

    fun submit() {
        job?.cancel() // 取消上一次
        job = scope.launch(context) {
            delay(waitMs)
            action()
        }
    }

    /**
     * 调用此方法触发防抖
     * @param action 延迟结束后执行的动作
     */
    fun submit(action: () -> Unit) {
        job?.cancel() // 取消上一次
        job = scope.launch(context) {
            delay(waitMs)
            action()
        }
    }

    /** 可选：取消当前任务 */
    fun cancel() {
        job?.cancel()
    }
}

fun ViewModel.debounce(
    waitMs: Long = 5000L,
    context: CoroutineContext = Dispatchers.IO,
    action: () -> Unit
) = Debouncer(
    scope = viewModelScope,
    waitMs = waitMs,
    context = context,
    action = action
)