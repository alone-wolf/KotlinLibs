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
    private val onChange: ((List<T>) -> Unit)? = null
) {
    private val stateList = mutableStateListOf<T>().apply { addAll(initial) }

    operator fun get(index: Int): T = stateList[index]
    val size: Int
        get() {return stateList.size}
    fun asReadOnly(): List<T> = stateList

    fun add(item: T) = modify { stateList.add(item) }
    fun addAll(items:List<T>) = modify { stateList.addAll(items)}
    fun remove(item: T) = modify { stateList.remove(item) }
    fun removeAt(index: Int) = modify { stateList.removeAt(index) }
    operator fun set(index: Int, item: T) = modify { stateList[index] = item }
    fun clear() = modify {
//        stateList.clear()
    }

    fun toList(): List<T> {
        return stateList.toList()
    }

    private inline fun modify(block: () -> Unit) {
        block()
        onChange?.invoke(stateList)
    }
}

inline fun <reified T> autoActionStateListOf(
    initial: List<T> = emptyList(),
    noinline onChange: (List<T>) -> Unit
) = AutoActionMutableStateList(initial, onChange)