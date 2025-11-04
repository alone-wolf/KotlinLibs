package top.writerpass.rekuester.ui.window

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import top.writerpass.cmplibrary.compose.FullHeightColumn
import top.writerpass.cmplibrary.compose.FullWidthBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.CxIconButton
import top.writerpass.cmplibrary.compose.ables.CxText
import top.writerpass.cmplibrary.modifier.onPointerHover
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.LocalCollectionsViewModel
import top.writerpass.rekuester.LocalMainUiViewModel

@Composable
fun CollectionManagerWindow() {
    val mainUiViewModel = LocalMainUiViewModel.current

    Window(
        visible = mainUiViewModel.showCollectionManager,
        onCloseRequest = { mainUiViewModel.showCollectionManager = false },
        title = "Collection Manager",
        content = {
            val mainUiViewModel = LocalMainUiViewModel.current
            val collectionsViewModel = LocalCollectionsViewModel.current
            FullHeightColumn(modifier = Modifier.width(mainUiViewModel.sideListWidth)) {
                FullWidthRow(horizontalArrangement = Arrangement.End) {
//                        "Load".TextButton {
//                            mainViewModel.loadData()
//                        }
//                        "Save".TextButton {
//                            mainViewModel.saveData()
//                        }
                    Icons.Default.Add.CxIconButton {}
                }
                val collections by collectionsViewModel.collectionsFlow.collectAsState()
                Row(modifier = Modifier.fillMaxWidth()) {

                }
                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    items(
                        items = collections,
                        itemContent = { collection ->
                            val onHover = Mutable.someBoolean()
                            FullWidthBox(
                                modifier = Modifier.height(45.dp)
                                    .clickable {
//                                            navController.navigate(Pages.ApiRequestPage(api.uuid)) {
//                                                popUpTo<Pages.BlankPage>()
//                                            }
//                                            mainViewModel.openApiTab(api)

//                                    val r = mainViewModel.openedApis.find { it.uuid == api.uuid }
//                                    if (r == null) {
//                                        mainViewModel.openedApis.add(api)
//                                    } else {
//                                        val index = mainViewModel.openedApis.indexOf(r)
//                                        mainViewModel.openedApis[index] = api
//                                    }
                                    }
                                    .padding(horizontal = 16.dp)
                                    .onPointerHover(
                                        onNotHover = { onHover.value = false },
                                        onHover = { onHover.value = true }
                                    ),
                            ) {
                                collection.label.CxText(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                                    AnimatedVisibility(
                                        visible = onHover.value,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        val onIconHover = Mutable.someBoolean()
                                        Box(
                                            modifier = Modifier.onPointerHover(
                                                onHover = { onIconHover.setTrue() },
                                                onNotHover = { onIconHover.setFalse() })
                                        ) {
                                            AnimatedContent(onIconHover.value) {
//                                                    if (it) {
//                                                        Icons.Default.Delete.IconButton {
//                                                            collectionApiViewModel.deleteApi(api)
//                                                        }
//                                                    } else {
//                                                        Icons.Outlined.Delete.IconButton {}
//                                                    }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    )
}