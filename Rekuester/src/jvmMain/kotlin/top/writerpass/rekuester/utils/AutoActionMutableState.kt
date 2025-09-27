package top.writerpass.rekuester.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AutoActionMutableState<T>(
    initial: T,
    private val action: () -> Unit
) : MutableState<T> {

    private val state = mutableStateOf(initial)

    override var value: T
        get() = state.value
        set(value) {
            state.value = value
            action()
        }

    override fun component1(): T = state.component1()
    override fun component2(): (T) -> Unit = state.component2()
}