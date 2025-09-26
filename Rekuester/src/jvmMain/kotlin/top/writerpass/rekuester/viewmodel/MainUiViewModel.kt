package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class MainUiViewModel : ViewModel() {
    var apisListViewWidth by mutableStateOf(200.dp)
}