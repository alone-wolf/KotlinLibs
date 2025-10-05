@file:OptIn(ExperimentalComposeUiApi::class)

package top.writerpass.rekuester.ui.part

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.compose.FullHeightColumn
import top.writerpass.cmplibrary.compose.FullWidthBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.modifier.onPointerHover
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.Collection
import top.writerpass.rekuester.LocalCollectionApiViewModel
import top.writerpass.rekuester.LocalMainUiViewModel
import top.writerpass.rekuester.LocalMainViewModel

@Composable
fun ApisListView() {
    val mainViewModel = LocalMainViewModel.current
    val mainUiViewModel = LocalMainUiViewModel.current
    val collectionApiViewModel = LocalCollectionApiViewModel.current
    FullHeightColumn(modifier = Modifier.width(mainUiViewModel.sideListWidth)) {
        FullWidthRow(horizontalArrangement = Arrangement.End) {
            "Load".TextButton { mainViewModel.loadData() }
            "Save".TextButton { mainViewModel.saveData() }
            Icons.Default.Add.IconButton { collectionApiViewModel.createNewApi() }
        }
        val collection by collectionApiViewModel.collectionFlow.collectAsState()
        if (collection == Collection.BLANK) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                "No Collection Selected".Text()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                collection.label.Text(maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            val apisList by collectionApiViewModel.apisListFlow.collectAsState()
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(
                    items = apisList,
                    itemContent = { api ->
                        val onHover = Mutable.someBoolean()
                        var showMenu by remember { mutableStateOf(false) }
                        FullWidthBox(
                            modifier = Modifier.height(45.dp)
                                .clickable { collectionApiViewModel.openApiTab(api) }
                                .onPointerEvent(PointerEventType.Press) { e ->
                                    if (e.buttons.isSecondaryPressed) {
                                        showMenu = true
                                    }
                                }
                                .padding(horizontal = 16.dp)
                                .onPointerHover(
                                    onNotHover = { onHover.value = false },
                                    onHover = { onHover.value = true }
                                ),
                        ) {
                            api.label.Text(
                                modifier = Modifier.align(Alignment.CenterStart),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                                AnimatedVisibility(
                                    visible = onHover.value, enter = fadeIn(), exit = fadeOut()
                                ) {
                                    val onIconHover = Mutable.someBoolean()
                                    Box(
                                        modifier = Modifier.onPointerHover(
                                            onHover = { onIconHover.setTrue() },
                                            onNotHover = { onIconHover.setFalse() })
                                    ) {
                                        AnimatedContent(onIconHover.value) {
                                            if (it) {
                                                Icons.Default.Delete.IconButton {
                                                    collectionApiViewModel.deleteApi(api)
                                                }
                                            } else {
                                                Icons.Outlined.Delete.IconButton {}
                                            }
                                        }
                                    }
                                }
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Clone") },
                                    onClick = { showMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Open") },
                                    onClick = { showMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Open All") },
                                    onClick = {
//                                        mainViewModel.openApiTabs(apis)
                                        showMenu = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}