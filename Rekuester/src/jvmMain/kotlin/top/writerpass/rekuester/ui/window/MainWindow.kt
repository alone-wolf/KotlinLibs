@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package top.writerpass.rekuester.ui.window

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Https
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.rekuester.LocalApplicationScope
import top.writerpass.rekuester.LocalCollectionApiViewModel
import top.writerpass.rekuester.LocalCollectionsViewModel
import top.writerpass.rekuester.LocalMainUiViewModel
import top.writerpass.rekuester.ui.componment.DraggableDivideBar
import top.writerpass.rekuester.ui.part.ApiRequestPanel
import top.writerpass.rekuester.ui.part.ApisListView
import top.writerpass.rekuester.ui.part.MainWindowMenu
import top.writerpass.rekuester.viewmodel.CollectionApiViewModel
import java.awt.Dimension

@Composable
fun MainWindow() {
    val mainUiViewModel = LocalMainUiViewModel.current
    val applicationScope = LocalApplicationScope.current
    val collectionsViewModel = LocalCollectionsViewModel.current
    val currentCollectionUuid = collectionsViewModel.currentCollectionUuid
    val collectionApiViewModel = CollectionApiViewModel.instance(currentCollectionUuid)

    Window(
        onCloseRequest = {
            if (mainUiViewModel.exitOnClose.value) {
                applicationScope.exitApplication()
            } else {
                mainUiViewModel.showMainWindow.setFalse()
            }
        },
        state = rememberWindowState(),
        visible = mainUiViewModel.showMainWindow.value,
        title = "Rekuester",
        icon = rememberVectorPainter(Icons.Default.Https),
        resizable = true,
        enabled = mainUiViewModel.showNewCollectionWizard.not(),
        focusable = true,
        alwaysOnTop = false,
        content = {
            window.minimumSize = Dimension(800, 600)

            MainWindowMenu()

            CompositionLocalProvider(
                LocalCollectionApiViewModel provides collectionApiViewModel
            ) {
                FullSizeRow {
                    ApisListView()
                    DraggableDivideBar(mainUiViewModel.sideListWidth) { it ->
                        mainUiViewModel.sideListWidth = it
                    }
                    ApiRequestPanel()
                }
            }
        }
    )
}

