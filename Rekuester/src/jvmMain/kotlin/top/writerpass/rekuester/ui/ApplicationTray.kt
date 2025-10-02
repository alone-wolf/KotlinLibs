package top.writerpass.rekuester.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Https
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.isTraySupported
import androidx.compose.ui.window.rememberTrayState
import top.writerpass.rekuester.LocalApplicationScope
import top.writerpass.rekuester.LocalMainUiViewModel

@Composable
fun ApplicationTray() {
    val applicationScope = LocalApplicationScope.current
    val mainUiViewModel = LocalMainUiViewModel.current
    with(applicationScope) {
        if (isTraySupported) {
            Tray(
                icon = rememberVectorPainter(Icons.Default.Https),
                state = rememberTrayState(),
                tooltip = "Rekuester",
                onAction = { mainUiViewModel.showMainWindow = true },
                menu = {
                    if (mainUiViewModel.showMainWindow) {
                        Item("Hide Main Window") {
                            mainUiViewModel.showMainWindow = false
                        }
                    } else {
                        Item("Show Main Window") {
                            mainUiViewModel.showMainWindow = true
                        }
                    }
                    Separator()
                    Item("Exit") { exitApplication() }
                }
            )
        }
    }
}