package top.writerpass.cmplibrary.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import top.writerpass.cmplibrary.LaunchedEffectOdd

object Mutable {

    fun MutableState<Boolean>.setTrue() {
        value = true
    }

    fun MutableState<Boolean>.setFalse() {
        value = false
    }

    fun MutableState<Boolean>.switch() {
        value = value.not()
    }

    @Composable
    fun <T> something(default: T): MutableState<T> {
        return remember { mutableStateOf(default) }
    }

    @Composable
    fun someString(default: String = ""): MutableState<String> {
        return remember { mutableStateOf(default) }
    }

    @Composable
    fun someBoolean(default: Boolean = false): MutableState<Boolean> {
        return remember { mutableStateOf(default) }
    }

    @Composable
    fun someInt(default: Int = 0): MutableState<Int> {
        return remember { mutableIntStateOf(default) }
    }

    @Composable
    fun someLong(default: Long = 0): MutableState<Long> {
        return remember { mutableLongStateOf(default) }
    }

    @Composable
    fun someDouble(default: Double = 0.0): MutableState<Double> {
        return remember { mutableDoubleStateOf(default) }
    }

    @Composable
    fun <T> someList(): SnapshotStateList<T> {
        return remember { mutableStateListOf() }
    }
}

@Composable
fun <T> MutableState<T>.LaterDefaultValue(v: T) {
    LaunchedEffectOdd {
        value = v
    }
}

