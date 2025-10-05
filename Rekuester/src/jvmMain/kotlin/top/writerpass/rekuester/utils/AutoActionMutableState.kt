package top.writerpass.rekuester.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

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

inline fun <reified T> autoActionStateOf(
    initial: T,
    noinline action: () -> Unit
) = AutoActionMutableState(initial, action)

class AutoActionMutableStateList<T>(
    initial: List<T> = emptyList(),
    private val action: () -> Unit
) {
    private val stateList = mutableStateListOf<T>().apply { addAll(initial) }

    val list: SnapshotStateList<T> get() = stateList

    fun add(item: T) {
        stateList.add(item)
        action()
    }

    fun remove(item: T) {
        stateList.remove(item)
        action()
    }

    fun removeAt(index: Int) {
        stateList.removeAt(index)
        action()
    }

    fun set(index: Int, item: T) {
        stateList[index] = item
        action()
    }

    fun clear() {
        stateList.clear()
        action()
    }

    operator fun get(index: Int): T = stateList[index]
    val size: Int get() = stateList.size
}

inline fun <reified T> autoActionStateListOf(
    initial: List<T> = emptyList(),
    noinline action: () -> Unit
) = AutoActionMutableStateList(initial, action)