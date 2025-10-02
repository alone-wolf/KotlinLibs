package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

class MainUiViewModel : BaseViewModel() {
    var sideListWidth by mutableStateOf(200.dp)
    var showNewCollectionWizard by mutableStateOf(false)
    var showCollectionManager by mutableStateOf(false)

    var showMainWindow by mutableStateOf(true)
}





