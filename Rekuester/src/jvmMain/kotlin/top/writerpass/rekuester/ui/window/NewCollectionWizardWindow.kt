package top.writerpass.rekuester.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import top.writerpass.rekuester.LocalMainUiViewModel

@Composable
fun NewCollectionWizardWindow() {
    val mainUiViewModel = LocalMainUiViewModel.current

    Window(
        visible = mainUiViewModel.showNewCollectionWizard,
        onCloseRequest = { mainUiViewModel.showNewCollectionWizard = false },
        title = "New Collection Wizard",
        content = {}
    )
}