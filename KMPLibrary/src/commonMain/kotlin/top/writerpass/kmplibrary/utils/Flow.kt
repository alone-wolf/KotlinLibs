package top.writerpass.kmplibrary.utils

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<List<T>>.onUpdate(block: MutableList<T>.() -> Unit) {
    value.toMutableList().let { mutableList ->
        mutableList.block()
        value = mutableList.toList()
    }
}