package top.writerpass.micromessage.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ApplicationState {
    var showMainWindow by mutableStateOf(true)
    var pinMainWindowOnTop by mutableStateOf(false)
}