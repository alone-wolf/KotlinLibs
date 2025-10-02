@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package top.writerpass.rekuester.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Https
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.launch
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
fun FrameWindowScope.MainWindowMenu() {
    val mainUiViewModel = LocalMainUiViewModel.current
    val collectionsViewModel = LocalCollectionsViewModel.current
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
}

@Composable
fun OpenedApiTabsRow(){
    val mainViewModel = LocalMainViewModel.current
    val navController = LocalNavController.current
    val openedApiTabs by mainViewModel.openedApiTabsFlow.collectAsState()
    val lazyListState = rememberLazyListState()
    LaunchedEffect(mainViewModel.openedTabApiUUID) {
        val index = openedApiTabs.indexOfFirst {
            it.uuid == mainViewModel.openedTabApiUUID
        }
        if (index != -1) {
            lazyListState.animateScrollToItem(index)
        }
        if (mainViewModel.openedTabApiUUID == "") {
            navController.navigate(Pages.BlankPage) {
                popUpTo(Pages.BlankPage) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Pages.ApiRequestPage(mainViewModel.openedTabApiUUID)) {
                popUpTo(Pages.BlankPage)
            }
        }
    }
    FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
        val scope = rememberCoroutineScope()
        val tabWidthPx = with(LocalDensity.current) {
            120.dp.toPx()
        }
        LazyRow(
            modifier = Modifier.height(30.dp).weight(1f)
                .onPointerEvent(PointerEventType.Scroll) { event ->
                    val deltaY = event.changes.first().scrollDelta.y
                    if (deltaY != 0f) {
                        scope.launch {
                            // 把纵向滚轮事件映射为横向滚动
                            lazyListState.scrollBy(deltaY * 30)
                        }
                    }
                },
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = openedApiTabs,
                key = { it.uuid },
                itemContent = { api ->
                    val isSelected by remember {
                        derivedStateOf { mainViewModel.openedTabApiUUID == api.uuid }
                    }
                    Row(
                        modifier = Modifier
                            .width(120.dp)
                            .height(30.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 4.dp, topEnd = 4.dp,
                                    bottomStart = if (isSelected) 0.dp else 4.dp,
                                    bottomEnd = if (isSelected) 0.dp else 4.dp
                                )
                            )
                            .then(
                                if (isSelected) {
                                    Modifier
                                } else {
                                    Modifier.background(Color.LightGray)
                                }
                            )
//                                            .clip(RoundedCornerShape(4.dp))
//                                            .background(if (isSelected) Color.White else Color.Gray)
                            .clickable { mainViewModel.openApiTab(api) }
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
                                mainViewModel.closeApiTab(api)
                            }
                        )
                    }
                }
            )
        }
        Icons.Default.ArrowLeft.Icon(modifier = Modifier.clickable {
            scope.launch { lazyListState.animateScrollBy(-tabWidthPx * 1.5f) }
        })
        Icons.Default.ArrowRight.Icon(modifier = Modifier.clickable {
            scope.launch { lazyListState.animateScrollBy(tabWidthPx * 1.5f) }
        })
    }
}

@Composable
fun MainWindow() {
    val mainUiViewModel = LocalMainUiViewModel.current
    val collectionsViewModel = LocalCollectionsViewModel.current
    val navController = LocalNavController.current
    val applicationScope = LocalApplicationScope.current

    val currentCollectionUUID = collectionsViewModel.currentCollectionUUID
    val collectionApiViewModel = CollectionApiViewModel.instance(currentCollectionUUID)

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
                    FullSizeColumn {
                        OpenedApiTabsRow()

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