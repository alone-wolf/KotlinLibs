package top.writerpass.rekuester.ui.part

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import top.writerpass.cmplibrary.utils.Mutable.switch
import top.writerpass.rekuester.LocalApplicationScope
import top.writerpass.rekuester.LocalCollectionsViewModel
import top.writerpass.rekuester.LocalMainUiViewModel

@Composable
fun FrameWindowScope.MainWindowMenu() {
    val mainUiViewModel = LocalMainUiViewModel.current
    val collectionsViewModel = LocalCollectionsViewModel.current
    val applicationScope = LocalApplicationScope.current
    MenuBar {
        Menu("File") {
            Item(
                text = "New",
                onClick = { println("New clicked") },
                shortcut = KeyShortcut(Key.N, ctrl = true)
            )
            Item(
                text = "Open",
                onClick = { println("Open clicked") },
                shortcut = KeyShortcut(Key.O, ctrl = true)
            )
            Item(
                text = "Exit",
                onClick = applicationScope::exitApplication,
            )
        }
        Menu("Edit") {
            Item("Undo", onClick = { println("Undo") })
            Item("Redo", onClick = { println("Redo") })
        }
        Menu("Collections") {
            Item(
                text = "New Collection",
                onClick = { mainUiViewModel.showNewCollectionWizard = true },
                shortcut = KeyShortcut(Key.N, ctrl = true, shift = true)
            )
            Item(
                text = "Collection Manager",
                onClick = { mainUiViewModel.showCollectionManager = true },
                shortcut = KeyShortcut(Key.M, ctrl = true, shift = true)
            )
            Separator()
            val collections by collectionsViewModel.collectionsFlow.collectAsState()
            collections.forEachIndexed { index, collection ->
                RadioButtonItem(
                    text = collection.label,
                    selected = collectionsViewModel.currentCollectionUuid == collection.uuid,
                    onClick = {
                        collectionsViewModel.currentCollectionUuid = collection.uuid
                    },
                )
            }
        }
        Menu("Theme") {
            var selectedTheme by remember { mutableStateOf("System") }
            RadioButtonItem(
                "Light",
                selected = selectedTheme == "Light",
                onClick = { selectedTheme = "Light" }
            )
            RadioButtonItem(
                "Dark",
                selected = selectedTheme == "Dark",
                onClick = { selectedTheme = "Dark" }
            )
            RadioButtonItem(
                "System",
                selected = selectedTheme == "System",
                onClick = { selectedTheme = "System" }
            )
        }
        Menu("Preferences") {
            CheckboxItem("Auto Check Update", true) {}
            CheckboxItem("Exit on Close", mainUiViewModel.exitOnClose.value) {
                mainUiViewModel.exitOnClose.switch()
            }
        }
        Menu("About") {
            Item("Version 0.0.1", onClick = { println("About clicked") })
            Item("Author", onClick = { println("About clicked") })
        }
    }
}