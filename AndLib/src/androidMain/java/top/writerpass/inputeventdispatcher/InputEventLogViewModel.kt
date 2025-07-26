package top.writerpass.inputeventdispatcher

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InputEventLogViewModel : ViewModel() {
    private val _logs = MutableStateFlow<List<InputEventLog>>(emptyList())
    val logs: StateFlow<List<InputEventLog>> = _logs.asStateFlow()

    fun log(event: InputEventLog) {
        _logs.update { current ->
            (current + event).takeLast(200).reversed() // 最多保留200条
        }
    }
}