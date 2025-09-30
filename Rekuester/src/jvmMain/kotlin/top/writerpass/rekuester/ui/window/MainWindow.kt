package top.writerpass.rekuester.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Https
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.navigation.composableNoAnimate
import top.writerpass.rekuester.LocalApplicationScope
import top.writerpass.rekuester.LocalCollectionApiViewModel
import top.writerpass.rekuester.LocalCollectionsViewModel
import top.writerpass.rekuester.LocalMainUiViewModel
import top.writerpass.rekuester.LocalMainViewModel
import top.writerpass.rekuester.LocalNavController
import top.writerpass.rekuester.Pages
import top.writerpass.rekuester.ui.componment.DraggableDivideBar
import top.writerpass.rekuester.ui.page.ApiRequestPage
import top.writerpass.rekuester.ui.part.ApisListView
import top.writerpass.rekuester.viewmodel.CollectionApiViewModel
import java.awt.Dimension

@Composable
fun MainWindow(){
    val mainUiViewModel = LocalMainUiViewModel.current
    val mainViewModel = LocalMainViewModel.current
    val collectionsViewModel = LocalCollectionsViewModel.current
    val navController = LocalNavController.current
    val applicationScope = LocalApplicationScope.current

    Window(
        onCloseRequest = applicationScope::exitApplication,
        state = rememberWindowState(),
        visible = true,
        title = "Rekuester",
        icon = rememberVectorPainter(Icons.Default.Https),
        resizable = true,
        enabled = mainUiViewModel.showNewCollectionWizard.not(),
        focusable = true,
        alwaysOnTop = false,
        content = {
            val collectionApiViewModel = viewModel(
                key = collectionsViewModel.currentCollectionUUID
            ) {
                CollectionApiViewModel(collectionsViewModel.currentCollectionUUID)
            }


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
//                    Item(
//                        text = "Exit",
//                        onClick = applicationScope::exitApplication,
//                        shortcut = KeyShortcut(Key.W, meta = true)
//                    )
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
                            selected = collectionsViewModel.currentCollectionUUID == collection.uuid,
                            onClick = {
                                collectionsViewModel.currentCollectionUUID = collection.uuid
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
                }
                Menu("About") {
                    Item("Version 0.0.1", onClick = { println("About clicked") })
                    Item("Author", onClick = { println("About clicked") })
                }
            }

            window.minimumSize = Dimension(800, 600)


            CompositionLocalProvider(
                LocalCollectionApiViewModel provides collectionApiViewModel
            ) {
                FullSizeRow {
                    ApisListView()
                    DraggableDivideBar(mainUiViewModel.apisListViewWidth) { it ->
                        mainUiViewModel.apisListViewWidth = it
                    }
                    FullSizeColumn {
                        FullWidthRow(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .height(30.dp)
                        ) {
                            val openedApiTabs by mainViewModel.openedApiTabsFlow.collectAsState()
                            openedApiTabs.forEach { api ->
                                Row(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(30.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Gray)
                                        .clickable {
                                            navController.navigate(Pages.ApiRequestPage(api.uuid)) {
                                                popUpTo<Pages.BlankPage>()
                                            }
                                        }
                                        .padding(horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    api.label.Text(
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icons.Default.Close.Icon(
                                        modifier = Modifier.size(20.dp).clickable {
                                            navController.navigate(Pages.BlankPage) {
                                                popUpTo<Pages.BlankPage>()
                                            }
                                            mainViewModel.closeApiTab(api)
                                        }
                                    )
                                }
                            }
                        }
                        NavHost(
                            navController = navController,
                            startDestination = Pages.BlankPage
                        ) {
                            composableNoAnimate<Pages.BlankPage> {
                                FullSizeBox {
                                    "This is a Blank Page, select an API to start".Text(
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                            }
                            composableNoAnimate<Pages.ApiRequestPage> { navBackStackEntry ->
                                ApiRequestPage(navBackStackEntry = navBackStackEntry)
                            }
                        }
                    }
                }
            }
        }
    )
}