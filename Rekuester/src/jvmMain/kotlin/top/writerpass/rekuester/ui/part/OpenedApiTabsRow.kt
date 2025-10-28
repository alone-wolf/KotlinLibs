@file:OptIn(ExperimentalComposeUiApi::class)

package top.writerpass.rekuester.ui.part

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.modifier.onPointerRightClick
import top.writerpass.cmplibrary.reorderable.ReorderableItem
import top.writerpass.cmplibrary.reorderable.detectReorder
import top.writerpass.cmplibrary.reorderable.rememberReorderableLazyListState
import top.writerpass.cmplibrary.reorderable.reorderable
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalCollectionApiViewModel

private val tabWidth = 120.dp

@Composable
fun OpenedApiTabsRow() {
    val collectionApiViewModel = LocalCollectionApiViewModel.current
    val density = LocalDensity.current
    val openedApiTabs by collectionApiViewModel.openedApiTabsFlow.collectAsState()
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        collectionApiViewModel.reorderApiTabs(from.index,to.index)
    })
    val lazyListState = remember { state.listState }
    val tabWidthPx = with(density) { tabWidth.toPx() }
    val scope = rememberCoroutineScope()

    val isListAtHead by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0
        }
    }
    val isListAtTail by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == openedApiTabs.size - 1
        }
    }


    LaunchedEffect(collectionApiViewModel.currentApiTabUuid) {
        val index = openedApiTabs.indexOfFirst {
            it.uuid == collectionApiViewModel.currentApiTabUuid
        }
        if (index != -1) {
            lazyListState.animateScrollToItem(index)
        }
    }
    FullWidthRow(verticalAlignment = Alignment.CenterVertically) {


        LazyRow(
            modifier = Modifier.reorderable(state).height(30.dp).weight(1f)
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
                key = { it },
                itemContent = { api ->
                    ReorderableItem(state, orientationLocked = false, key = api){isDragging->
                        val isSelected by remember {
                            derivedStateOf { collectionApiViewModel.currentApiTabUuid == api.uuid }
                        }
                        val showMenu = Mutable.someBoolean()
                        var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
                        val apiState = remember { ApiStateHolder.getApiState(api) }
                        Row(
                            modifier = Modifier
                                .width(tabWidth)
                                .height(30.dp)
                                .then(
                                    if (isSelected) {
                                        Modifier
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 8.dp,
                                                    topEnd = 8.dp,
                                                    bottomStart = 0.dp,
                                                    bottomEnd = 0.dp
                                                )
                                            )
                                            .background(Color.LightGray.copy(alpha = 0.5f))
                                            .drawWithContent {
                                                val thisHeight = size.height
                                                val thisWidth = size.width
                                                drawLine(
                                                    color = Color.Gray,
                                                    start = Offset(x = 10f, y = thisHeight-2),
                                                    end = Offset(x = thisWidth - 10f, y = thisHeight-2),
                                                    strokeWidth = Stroke.DefaultMiter,
                                                    cap = StrokeCap.Round
                                                )
                                                if (apiState.isModified.value) {
                                                    drawCircle(
                                                        color = Color.Red,
                                                        radius = 5f,
                                                        center = Offset(10f, thisHeight / 2),
                                                        alpha = 1f,
                                                    )
                                                }
                                                drawContent()
                                            }
                                    } else {
                                        Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.LightGray)
                                    }
                                )
                                .clickable { collectionApiViewModel.openApiTab(api) }
                                .onPointerRightClick { position ->
                                    menuOffset = with(density) {
                                        DpOffset(position.x.toDp(), position.y.toDp())
                                    }
                                    showMenu.setTrue()
                                }
                                .padding(start = if (apiState.isModified.value) 12.dp else 6.dp, end = 4.dp)
                                .animateItem()
                                .detectReorder(state),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            api.label.Text(
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Icons.Default.Close.Icon(
                                modifier = Modifier.size(20.dp).clickable {
                                    collectionApiViewModel.closeApiTab(api)
                                }
                            )
                            DropdownMenu(
                                expanded = showMenu.value,
                                onDismissRequest = { showMenu.setFalse() },
                                offset = menuOffset
                            ) {
                                DropdownMenuItem(
                                    text = { Text("关闭") },
                                    onClick = {
                                        collectionApiViewModel.closeApiTab(api)
                                        showMenu.setFalse()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("关闭其他") },
                                    onClick = {
                                        collectionApiViewModel.openApiTab(api)
                                        openedApiTabs.filter { it.uuid != api.uuid }
                                            .forEach { collectionApiViewModel.closeApiTab(it) }
                                        showMenu.setFalse()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("关闭所有") },
                                    onClick = {
                                        openedApiTabs.forEach { collectionApiViewModel.closeApiTab(it) }
                                        showMenu.setFalse()
                                    }
                                )
                            }
                        }
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