package top.writerpass.rekuester.tables.v9

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.pointerIcons.XResize
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.kmplibrary.utils.times
import top.writerpass.rekuester.tables.v8.sumOf

@JvmName("sumOfDp")
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Dp): Dp {
    var sum: Dp = 0.dp
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

class ColumnState(default: Dp = 120.dp) {
    var width by mutableStateOf(default)
}

class RowState(default: Dp = 60.dp) {
    var height by mutableStateOf(default)
}

@Stable
sealed interface TableWidthStrategy {
    object WrapContent : TableWidthStrategy
    object FillContainer : TableWidthStrategy
    class WidthFixed(val width: Dp) : TableWidthStrategy
}

@Stable
sealed interface TableColumnWidthStrategy {
    object DefaultFixed : TableColumnWidthStrategy
    object DefaultFlexible : TableColumnWidthStrategy
    object TableWidthEvenlyFixed : TableColumnWidthStrategy
    object TableWidthEvenlyFlexible : TableColumnWidthStrategy
}

class TableState(
    val defaultWidth: Dp = 120.dp,
    val defaultHeight: Dp = 40.dp,
    val tableWidthStrategy: TableWidthStrategy = TableWidthStrategy.WrapContent,
    val columnWidthStrategy: TableColumnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible
) {

    val a = Arrangement.SpaceBetween
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
        when (tableWidthStrategy) {
            TableWidthStrategy.WrapContent -> {
                columnStateMap.values.sumOf { it.width }
            }

            else -> 0.dp
        }
    }

//    val columnMaxWidth:Dp by derivedStateOf {
//        columnStateMap.values
//    }
}

private val headerList = listOf("HeadAHeadAHeadAHeadA", "HeadB", "HeadC", "HeadD", "HeadE", "HeadF")
val dataSet = listOf(listOf("AA", "BB", "CC", "DD", "EE", "FF")) * 3
const val default = "--"

fun main() {
    singleWindowApplication {
        val dataStateSet = remember { mutableStateMapOf<String, String>() }

        FullSizeColumn {
            HeaderTableSheet(
                tableState = remember {
                    TableState(
                        tableWidthStrategy = TableWidthStrategy.FillContainer,
                        columnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible
                    )
                },
                rowCount = dataSet.size,
                columnCount = headerList.size,
                headers = headerList,
                onItem = { rowId, columnId ->
                    dataSet.getOrNull(rowId)?.getOrNull(columnId) ?: "--"
                },
                onItemChange = { rowId, columnId, item ->
                    val key = "${rowId}-$columnId"
                    dataStateSet[key] = item
                },
            )
            dataSet.forEachIndexed { rowId, row ->
                row.forEachIndexed { columnId, item ->
                    val key = remember { "${rowId}-$columnId" }
                    val item = remember { dataStateSet[key] ?: item }
                    "mixed: $key == $item".Text()
                }
            }
        }
    }
}

@Composable
fun HeaderTableSheet(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    tableState: TableState = remember { TableState() },
    rowCount: Int,
    columnCount: Int,
    headers: List<String>,
    onItem: (rowId: Int, columnId: Int) -> String,
    onItemChange: (rowId: Int, columnId: Int, item: String) -> Unit
) {
    HeaderTableFrame(
        modifier = modifier,
        state = state,
        tableState = tableState,
        rowCount = rowCount,
        columnCount = columnCount,
        onHeaderItem = { columnId ->
            val header = remember { headers[columnId] }
            Text(
                text = header,
                modifier = Modifier.align(Alignment.Center),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        },
        onItemContent = { rowId, columnId ->
            val item = remember { onItem(rowId, columnId) }
            val isEditing = Mutable.someBoolean()
            val textFieldValue = Mutable.something(
                default = TextFieldValue(
                    text = item,
                    selection = TextRange(item.length)
                )
            )

            isEditing.When(
                isTrue = {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffectOdd { focusRequester.requestFocus() }
                    BasicTextField(
                        value = textFieldValue.value,
                        onValueChange = {
                            textFieldValue.value = it
                            onItemChange(rowId, columnId, it.text)
                        },
                        modifier = Modifier.fillMaxSize().focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(
                            onGo = { isEditing.setFalse() }
                        ),
                        singleLine = true,
                    )
                },
                isFalse = {
                    Text(
                        text = textFieldValue.value.text,
                        modifier = Modifier.clickable { isEditing.setTrue() }
                    )
                }
            )
        }
    )
}

@Composable
fun HeaderTableFrame(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    tableState: TableState = remember { TableState() },
    rowCount: Int,
    columnCount: Int,
    onHeaderItem: @Composable BoxScope.(columnId: Int) -> Unit,
    onItemContent: @Composable (rowId: Int, columnId: Int) -> Unit,
) {
    val density = LocalDensity.current

    LazyColumn(
        modifier = modifier
            .then(
                when (tableState.tableWidthStrategy) {
                    is TableWidthStrategy.WrapContent -> {
                        Modifier.width(width = tableState.tableWidth)
                    }

                    is TableWidthStrategy.FillContainer -> {
                        Modifier.fillMaxWidth()
                    }

                    is TableWidthStrategy.WidthFixed -> {
                        Modifier.width(width = tableState.tableWidthStrategy.width)
                    }
                }
            )
            .padding(4.dp)
            .composed("watchSize") {
                val fullWidth = Mutable.something(0.dp)
                LaunchedEffect(fullWidth.value) {
                    tableState.columnStateMap.forEach { _, columnState ->
                        columnState.width = with(density) { (fullWidth.value / columnCount) }
                    }
                }
                onSizeChanged { (width, _) ->
                    fullWidth.value = with(density) { width.toDp() }
                }
            },
        state = state
    ) {
        item { HorizontalDivider() }
        item {
            val rowState = tableState.rememberRowState(0)
            val isHeaderRow = true
            FullWidthRow(modifier = Modifier.height(rowState.height)) {
                VerticalDivider()
                (0 until columnCount).forEach { columnId ->
                    val columnState = tableState.rememberColumnState(columnId)

                    Box(
                        Modifier.width(columnState.width)
                            .fillMaxHeight()
                            .background(Color.LightGray)
                    ) {
                        onHeaderItem(columnId)
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
        items(count = rowCount, key = { it }, itemContent = { rowId ->
            val rowState = tableState.rememberRowState(rowId + 1)
            FullWidthRow(modifier = Modifier.height(rowState.height)) {
                VerticalDivider()
                (0 until columnCount).forEach { columnId ->
                    val columnState = tableState.rememberColumnState(columnId)
                    Box(
                        Modifier.width(columnState.width).fillMaxHeight(),
                        content = { onItemContent(rowId, columnId) }
                    )
                    VerticalDivider()
                }
            }
            HorizontalDivider()
        })
    }
}



