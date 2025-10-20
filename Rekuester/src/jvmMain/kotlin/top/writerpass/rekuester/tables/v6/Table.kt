package top.writerpass.rekuester.tables.v6

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.pointerIcons.XResize
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate
import java.awt.Cursor

@JvmName("sumOfDp")
private inline fun <T> Iterable<T>.sumOf(selector: (T) -> Dp): Dp {
    var sum: Dp = 0.dp
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

private class ColumnState(default: Dp = 120.dp) {
    var width by mutableStateOf(default)
}

private class RowState(default: Dp = 60.dp) {
    var height by mutableStateOf(default)
}

private object TableState {
    val defaultWidth = 120.dp
    val defaultHeight = 40.dp
    val rowStateMap = mutableStateMapOf<Int, RowState>()
    fun getRowState(index: Int): RowState {
        return rowStateMap.getOrCreate(index) { RowState(defaultHeight) }
    }

    @Composable
    fun rememberRowState(index: Int): RowState = remember { getRowState(index) }

    val columnStateMap = mutableStateMapOf<Int, ColumnState>()
    fun getColumnState(index: Int): ColumnState {
        return columnStateMap.getOrCreate(index) { ColumnState(defaultWidth) }
    }

    @Composable
    fun rememberColumnState(index: Int): ColumnState = remember { getColumnState(index) }

    val tableHeight: Dp by derivedStateOf {
        rowStateMap.values.sumOf { it.height }
    }

    val tableWidth: Dp by derivedStateOf {
        columnStateMap.values.sumOf { it.width }
    }
}

private val dataSet = listOf(
    listOf(
        "HeadAHeadAHeadAHeadA",
        "HeadB",
        "HeadC",
        "HeadD",
        "HeadE",
        "HeadF"
    ),
    listOf("AA", "BB", "CC", "DD", "EE", "FF"),
    listOf("AA", "BB", "CC", "DD", "EE", "FF"),
    listOf("AA", "BB", "CC", "DD", "EE", "FF"),
    listOf("AA", "BB", "CC", "DD", "EE", "FF"),
    listOf("AA", "BB", "CC", "DD", "EE", "FF"),
)

private const val default = "--"

@Composable
private fun Table6Wrapper() {
    Table6(
        rowCount = dataSet.size,
        columnCount = dataSet.first().size,
        onItem = { rowId, columnId ->
            dataSet.getOrNull(rowId)?.getOrNull(columnId) ?: default
        },
        onItemContent = { rowId, columnId, isHeader, item ->
            Text(
                text = item,
                modifier = Modifier.then(if (isHeader) Modifier.align(Alignment.Center) else Modifier),
                maxLines = 1,
                overflow = TextOverflow.Clip,
                textAlign = if (isHeader) TextAlign.Center else null
            )
        }
    )
}

@Composable
private fun Table6Wrapper1() {
    val focusManager = LocalFocusManager.current
    Table6(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            focusManager.clearFocus()
        },
        rowCount = dataSet.size,
        columnCount = dataSet.first().size,
        onItem = { rowId, columnId ->
            dataSet.getOrNull(rowId)?.getOrNull(columnId) ?: default
        },
        onItemContent = { rowId, columnId, isHeader, item ->
            val isEditing = Mutable.someBoolean()
            val textFieldValue = Mutable.something(
                TextFieldValue(
                    item,
                    TextRange(item.length)
                )
            )
            val focusRequester = remember { FocusRequester() }

            if (isEditing.value) {
                LaunchedEffectOdd {
                    focusRequester.requestFocus()
                }

                BasicTextField(
                    value = textFieldValue.value,
                    onValueChange = { textFieldValue.value = it },
                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = { isEditing.setFalse() }
                    ),
                    singleLine = true,
                )
            } else {
                Text(
                    text = textFieldValue.value.text,
                    modifier = Modifier.fillMaxSize().then(
                        if (isHeader) {
                            Modifier.align(Alignment.Center)
                        } else {
                            Modifier.clickable {
                                isEditing.setTrue()
                            }
                        }
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = if (isHeader) TextAlign.Center else null,
                )
            }
        }
    )
}

@Composable
private fun <T : Any> Table6(
    modifier: Modifier = Modifier,
    rowCount: Int,
    columnCount: Int,
    onItem: (rowId: Int, columnId: Int) -> T,
    onItemContent: @Composable BoxScope.(rowId: Int, columnId: Int, isHeader: Boolean, item: T) -> Unit,
    state: TableState = TableState,
) {
    Column(
        modifier = modifier
            .size(
                width = state.tableWidth,
                height = state.tableHeight
            )
            .padding(18.dp)
    ) {
        val density = LocalDensity.current
        HorizontalDivider()
        (0 until rowCount).forEach { rowIndex ->
            val rowState = state.rememberRowState(rowIndex)
            val isHeaderRow = rowIndex == 0
            FullWidthRow(modifier = Modifier.height(rowState.height)) {
                VerticalDivider()
                (0 until columnCount).forEach { columnIndex ->
                    val columnState = state.rememberColumnState(columnIndex)
                    val item = remember { onItem(rowIndex, columnIndex) }
                    Box(
                        Modifier.width(columnState.width)
                            .fillMaxHeight()
                            .then(
                                if (isHeaderRow) {
                                    Modifier.background(Color.LightGray)
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        onItemContent(
                            rowIndex,
                            columnIndex,
                            isHeaderRow,
                            item,
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.then(
                            if (isHeaderRow) {
                                Modifier.pointerHoverIcon(PointerIcon.XResize)
                                    .draggable(
                                        state = rememberDraggableState(onDelta = { delta ->
                                            val rr = with(density) {
                                                (columnState.width.toPx() + delta).toDp()
                                            }.coerceIn(20.dp, 500.dp)
                                            columnState.width = rr
                                        }),
                                        orientation = Orientation.Horizontal
                                    )
                            } else {
                                Modifier
                            }
                        )
                    )
                }
            }
            HorizontalDivider()
        }
    }
}