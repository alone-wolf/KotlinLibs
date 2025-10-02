@file:OptIn(ExperimentalComposeUiApi::class)

package top.writerpass.rekuester.ui.part

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.rekuester.LocalMainViewModel
import top.writerpass.rekuester.LocalNavController
import top.writerpass.rekuester.Pages

@Composable
fun OpenedApiTabsRow() {
    val mainViewModel = LocalMainViewModel.current
    val navController = LocalNavController.current
    val density = LocalDensity.current
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
        val tabWidthPx = with(density) { 120.dp.toPx() }
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
                    var showMenu by remember { mutableStateOf(false) }
                    var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
                    Row(
                        modifier = Modifier.Companion
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
                            .clickable { mainViewModel.openApiTab(api) }
                            .onPointerEvent(PointerEventType.Press) { e ->
                                if (e.buttons.isSecondaryPressed) {
                                    val pos = e.changes.first().position
                                    with(density) {
                                        menuOffset = DpOffset(pos.x.toDp(), pos.y.toDp())
                                    }
                                    showMenu = true
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
                                mainViewModel.closeApiTab(api)
                            }
                        )
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            offset = menuOffset
                        ) {
                            DropdownMenuItem(
                                text = { Text("关闭") },
                                onClick = {
                                    mainViewModel.closeApiTab(api)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("关闭其他") },
                                onClick = {
                                    openedApiTabs.filter { it.uuid != api.uuid }
                                        .forEach { mainViewModel.closeApiTab(it) }
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("关闭所有") },
                                onClick = {
                                    openedApiTabs.forEach { mainViewModel.closeApiTab(it) }
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
        Icons.Default.ArrowLeft.Icon(modifier = Modifier.Companion.clickable {
            scope.launch { lazyListState.animateScrollBy(-tabWidthPx * 1.5f) }
        })
        Icons.Default.ArrowRight.Icon(modifier = Modifier.Companion.clickable {
            scope.launch { lazyListState.animateScrollBy(tabWidthPx * 1.5f) }
        })
    }
}