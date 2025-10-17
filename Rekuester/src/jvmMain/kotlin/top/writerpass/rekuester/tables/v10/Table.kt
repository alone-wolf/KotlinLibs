package top.writerpass.rekuester.tables.v10

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.OutlinedTextFiled1
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.pointerIcons.XResize
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.kmplibrary.utils.times
import top.writerpass.rekuester.tables.v10.CommonTableFrames.horizontalDivider
import top.writerpass.rekuester.tables.v10.CommonTableFrames.horizontalDraggable
import top.writerpass.rekuester.tables.v10.CommonTableFrames.tableWidth
import top.writerpass.rekuester.tables.v10.CommonTableFrames.watchTableSize
import kotlin.collections.component1
import kotlin.collections.component2

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
    val dataRowCount: Int,
    val dataColumnCount: Int,
    val defaultWidth: Dp = 120.dp,
    val defaultHeight: Dp = 40.dp,
    val tableWidthStrategy: TableWidthStrategy = TableWidthStrategy.WrapContent,
    val columnWidthStrategy: TableColumnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible,
    val hasLeadingColumn: Boolean = false,
    val hasTailColumn: Boolean = false,
    val hasHeaderRow: Boolean = false,
    val hasFooterRow: Boolean = false
) {
    val tableColumnCount: Int = run {
        var a1 = this@TableState.dataColumnCount
        if (this@TableState.hasLeadingColumn) a1 += 1
        if (this@TableState.hasTailColumn) a1 += 1
        a1
    }

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
}


//fun main1() {
//    val headerList = listOf("HeadAHeadAHeadAHeadA", "HeadB", "HeadC", "HeadD", "HeadE", "HeadF")
//    val dataSet = listOf(listOf("AA", "BB", "CC", "DD", "EE", "FF")) * 3
//    val default = "--"
//    singleWindowApplication {
//        val dataStateSet = remember { mutableStateMapOf<String, String>() }
//
//        FullSizeColumn {
//            HeaderTableSheet(
//                tableState = remember {
//                    TableState(
//                        tableWidthStrategy = TableWidthStrategy.FillContainer,
//                        columnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible
//                    )
//                },
//                rowCount = dataSet.size,
//                columnCount = headerList.size,
//                headers = headerList,
//                onItem = { rowId, columnId ->
//                    dataSet.getOrNull(rowId)?.getOrNull(columnId) ?: default
//                },
//                onItemChange = { rowId, columnId, item ->
//                    val key = "${rowId}-$columnId"
//                    dataStateSet[key] = item
//                },
//            )
//            dataSet.forEachIndexed { rowId, row ->
//                row.forEachIndexed { columnId, item ->
//                    val key = remember { "${rowId}-$columnId" }
//                    val item = remember { dataStateSet[key] ?: item }
//                    "mixed: $key == $item".Text()
//                }
//            }
//        }
//    }
//}

//@Composable
//fun HeaderTableSheet(
//    modifier: Modifier = Modifier,
//    state: LazyListState = rememberLazyListState(),
//    tableState: TableState = remember { TableState() },
//    rowCount: Int,
//    columnCount: Int,
//    headers: List<String>,
//    onItem: (rowId: Int, columnId: Int) -> String,
//    onItemChange: (rowId: Int, columnId: Int, item: String) -> Unit
//) {
//    HeaderTableFrame(
//        modifier = modifier,
//        state = state,
//        tableState = tableState,
//        rowCount = rowCount,
//        columnCount = columnCount,
//        onHeaderItem = { columnId ->
//            val header = remember { headers[columnId] }
//            Text(
//                text = header,
//                modifier = Modifier.align(Alignment.Center),
//                maxLines = 1,
//                overflow = TextOverflow.Clip
//            )
//        },
//        onItemContent = { rowId, columnId ->
//            val item = remember { onItem(rowId, columnId) }
//            val isEditing = Mutable.someBoolean()
//            val textFieldValue = Mutable.something(
//                default = TextFieldValue(
//                    text = item,
//                    selection = TextRange(item.length)
//                )
//            )
//            val density = LocalDensity.current
//            var lineHeightSp by remember { mutableStateOf(TextUnit.Unspecified) }
//
//
//            isEditing.When(
//                isTrue = {
//                    val focusRequester = remember { FocusRequester() }
//                    LaunchedEffectOdd { focusRequester.requestFocus() }
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        BasicTextField(
//                            value = textFieldValue.value,
//                            onValueChange = {
//                                textFieldValue.value = it
//                                onItemChange(rowId, columnId, it.text)
//                            },
//                            modifier = Modifier.fillMaxHeight().weight(1f)
//                                .focusRequester(focusRequester),
//                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
//                            keyboardActions = KeyboardActions(
//                                onGo = { isEditing.setFalse() }
//                            ),
//                            singleLine = true,
//                            textStyle = TextStyle.Default.copy(
//                                lineHeight = lineHeightSp,
//                                fontSize = 14.sp
//                            )
//                        )
//                        Icons.Default.Check.Icon(modifier = Modifier.size(16.dp).clickable {
//                            isEditing.setFalse()
//                        })
//                    }
//                },
//                isFalse = {
//                    Text(
//                        text = textFieldValue.value.text,
//                        modifier = Modifier.fillMaxSize().clickable { isEditing.setTrue() },
//                        onTextLayout = { textLayoutResult ->
//                            // 获取文本高度（像素）
//                            lineHeightSp =
//                                with(density) { textLayoutResult.size.height.toFloat().toSp() }
//                        },
//                        lineHeight = lineHeightSp,
//                        fontSize = 14.sp
//                    )
//                }
//            )
//        }
//    )
//}

//@Composable
//fun HeaderTableFrame(
//    modifier: Modifier = Modifier,
//    state: LazyListState = rememberLazyListState(),
//    tableState: TableState = remember { TableState() },
//    rowCount: Int,
//    columnCount: Int,
//    onHeaderItem: @Composable BoxScope.(columnId: Int) -> Unit,
//    onItemContent: @Composable (rowId: Int, columnId: Int) -> Unit,
//) {
//    val density = LocalDensity.current
//
//    LazyColumn(
//        modifier = modifier
//            .then(
//                when (tableState.tableWidthStrategy) {
//                    is TableWidthStrategy.WrapContent -> {
//                        Modifier.width(width = tableState.tableWidth)
//                    }
//
//                    is TableWidthStrategy.FillContainer -> {
//                        Modifier.fillMaxWidth()
//                    }
//
//                    is TableWidthStrategy.WidthFixed -> {
//                        Modifier.width(width = tableState.tableWidthStrategy.width)
//                    }
//                }
//            )
//            .padding(4.dp)
//            .composed("watchSize") {
//                val fullWidth = Mutable.something(0.dp)
//                LaunchedEffect(fullWidth.value) {
//                    tableState.columnStateMap.forEach { _, columnState ->
//                        columnState.width = with(density) { (fullWidth.value / columnCount) }
//                    }
//                }
//                onSizeChanged { (width, _) ->
//                    fullWidth.value = with(density) { width.toDp() }
//                }
//            },
//        state = state
//    ) {
//        item { HorizontalDivider() }
//        item {
//            val rowState = tableState.rememberRowState(0)
//            val isHeaderRow = true
//            FullWidthRow(modifier = Modifier.height(rowState.height)) {
//                VerticalDivider()
//                (0 until columnCount).forEach { columnId ->
//                    val columnState = tableState.rememberColumnState(columnId)
//
//                    Box(
//                        Modifier.width(columnState.width)
//                            .fillMaxHeight()
//                            .background(Color.LightGray)
//                    ) {
//                        onHeaderItem(columnId)
//                    }
//
//                    VerticalDivider(
//                        modifier = Modifier.then(
//                            if (isHeaderRow) {
//                                Modifier.pointerHoverIcon(PointerIcon.XResize)
//                                    .draggable(
//                                        state = rememberDraggableState(onDelta = { delta ->
//                                            val rr = with(density) {
//                                                (columnState.width.toPx() + delta).toDp()
//                                            }.coerceIn(20.dp, 500.dp)
//                                            columnState.width = rr
//                                        }),
//                                        orientation = Orientation.Horizontal
//                                    )
//                            } else {
//                                Modifier
//                            }
//                        )
//                    )
//                }
//
//            }
//            HorizontalDivider()
//        }
//        items(count = rowCount, key = { it }, itemContent = { rowId ->
//            val rowState = tableState.rememberRowState(rowId + 1)
//            FullWidthRow(modifier = Modifier.height(rowState.height)) {
//                VerticalDivider()
//                (0 until columnCount).forEach { columnId ->
//                    val columnState = tableState.rememberColumnState(columnId)
//                    Box(
//                        Modifier.width(columnState.width).fillMaxHeight(),
//                        content = { onItemContent(rowId, columnId) }
//                    )
//                    VerticalDivider()
//                }
//            }
//            HorizontalDivider()
//        })
//    }
//}


object CommonTableFrames {
    fun Modifier.tableWidth(tableState: TableState): Modifier {
        return then(
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
    }

    fun Modifier.watchTableSize(tableState: TableState): Modifier {
        return composed("watchSize") {
            val density = LocalDensity.current
            val fullWidth = Mutable.something(0.dp)
            LaunchedEffect(fullWidth.value) {
                tableState.columnStateMap.forEach { (_, columnState) ->
                    columnState.width = ((fullWidth.value / tableState.tableColumnCount))
                }
            }
            onSizeChanged { (width, _) ->
                fullWidth.value = with(density) { width.toDp() }
            }
        }
    }

    fun LazyListScope.horizontalDivider() {
        item { HorizontalDivider() }
    }

    fun Modifier.horizontalDraggable(isFirstRow: Boolean, columnState: ColumnState): Modifier {
        return then(
            if (isFirstRow) {
                pointerHoverIcon(PointerIcon.XResize)
                    .composed("horizontalDraggable") {
                        val density = LocalDensity.current
                        draggable(
                            state = rememberDraggableState(onDelta = { delta ->
                                val rr = with(density) {
                                    (columnState.width.toPx() + delta).toDp()
                                }.coerceIn(20.dp, 500.dp)
                                columnState.width = rr
                            }),
                            orientation = Orientation.Horizontal
                        )
                    }
            } else {
                Modifier
            }
        )
    }
}

enum class RowType {
    Header, Footer, Normal
}

enum class ColumnType {
    Leading, Tail, Normal
}

fun main() = singleWindowApplication {
    CommonTableFrame(
        modifier = Modifier,
        listState = rememberLazyListState(),
        tableState = remember {
            TableState(
                dataRowCount = 15,
                dataColumnCount = 3,
                defaultWidth = 120.dp,
                defaultHeight = 40.dp,
                tableWidthStrategy = TableWidthStrategy.WrapContent,
                columnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible,
                hasLeadingColumn = true,
                hasTailColumn = true,
                hasHeaderRow = true,
                hasFooterRow = true
            )
        },
        onItemContent = { rowId, columnId, rowType, columnType ->
            val e = Mutable.someBoolean()
            val k = Mutable.someString("")
            val v = Mutable.someString("")
            val d = Mutable.someString("")

            when (rowType) {
                RowType.Header -> {
                    when (columnType) {
                        ColumnType.Leading -> Checkbox(false, {}, modifier = Modifier.align(Alignment.Center))
                        ColumnType.Tail -> "Actions".Text(modifier = Modifier.align(Alignment.Center))
                        ColumnType.Normal -> {
                            val headers = remember { listOf("Key", "Value", "Description") }
                            val header = remember { headers[columnId] }
                            header.Text(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }

                RowType.Footer -> {
                    when (columnType) {
                        ColumnType.Leading -> {}
                        ColumnType.Tail -> {
                            Icons.Default.Save.Icon()
                        }

                        ColumnType.Normal -> {
                            when (columnId) {
                                0 -> k.OutlinedTextFiled1(modifier = Modifier.fillMaxSize())
                                1 -> v.OutlinedTextFiled1(modifier = Modifier.fillMaxSize())
                                2 -> d.OutlinedTextFiled1(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }

                RowType.Normal -> {
                    when (columnType) {
                        ColumnType.Leading -> Checkbox(false, {}, modifier = Modifier.align(Alignment.Center))
                        ColumnType.Tail -> {
                            Icons.Default.Save.Icon()
                            Icons.Default.Delete.Icon()
                        }

                        ColumnType.Normal -> {
                            val density = LocalDensity.current
                            val item = remember {
                                when (columnId) {
                                    0 -> "$rowId - 000"
                                    1 -> "$rowId - 111"
                                    2 -> "$rowId - 222"
                                    else -> "$rowId - ---"
                                }
                            }
                            val isEditing = Mutable.someBoolean()
                            val textFieldValue = Mutable.something(
                                default = TextFieldValue(
                                    text = item,
                                    selection = TextRange(item.length)
                                )
                            )
                            var lineHeightSp by remember { mutableStateOf(TextUnit.Unspecified) }

                            isEditing.When(
                                isTrue = {
                                    val focusRequester = remember { FocusRequester() }
                                    LaunchedEffectOdd { focusRequester.requestFocus() }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        BasicTextField(
                                            value = textFieldValue.value,
                                            onValueChange = {
                                                textFieldValue.value = it
//                                                onItemChange(rowId, columnId, it.text)
                                            },
                                            modifier = Modifier.fillMaxHeight().weight(1f)
                                                .focusRequester(focusRequester),
                                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                                            keyboardActions = KeyboardActions(
                                                onGo = { isEditing.setFalse() }
                                            ),
                                            singleLine = true,
                                            textStyle = TextStyle.Default.copy(
                                                lineHeight = lineHeightSp,
                                                fontSize = 14.sp
                                            )
                                        )
                                        Icons.Default.Check.Icon(modifier = Modifier.size(16.dp).clickable {
                                            isEditing.setFalse()
                                        })
                                    }
                                },
                                isFalse = {
                                    Text(
                                        text = textFieldValue.value.text,
                                        modifier = Modifier.fillMaxSize().clickable { isEditing.setTrue() },
                                        onTextLayout = { textLayoutResult ->
                                            // 获取文本高度（像素）
                                            lineHeightSp =
                                                with(density) { textLayoutResult.size.height.toFloat().toSp() }
                                        },
                                        lineHeight = lineHeightSp,
                                        fontSize = 14.sp
                                    )
                                }
                            )

                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CommonTableFrame(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    tableState: TableState,
    onItemContent: @Composable BoxScope.(rowId: Int, columnId: Int, rowType: RowType, columnType: ColumnType) -> Unit,
) {

    LazyColumn(
        modifier = modifier
            .tableWidth(tableState)
            .padding(4.dp)
//            .watchTableSize(tableState)
        ,
        state = listState
    ) {
        horizontalDivider()
        if (tableState.hasHeaderRow) {
            item {
                val rowId = remember { -1 }
                val rowState = tableState.rememberRowState(rowId)
                FullWidthRow(modifier = Modifier.height(rowState.height)) {
                    VerticalDivider()
                    if (tableState.hasLeadingColumn) {
                        val columnId = remember { -1 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Header, ColumnType.Leading) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    (0 until tableState.dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Header, ColumnType.Normal) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    if (tableState.hasTailColumn) {
                        val columnId = remember { -2 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Header, ColumnType.Tail) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                }
                HorizontalDivider()
            }
        }
        items(
            count = tableState.dataRowCount,
            key = { it },
            itemContent = { rowId ->
                val rowState = tableState.rememberRowState(rowId)
                val isFirstRow = remember { if (tableState.hasHeaderRow) false else rowId == 0 }
                FullWidthRow(modifier = Modifier.height(rowState.height)) {
                    VerticalDivider()
                    if (tableState.hasLeadingColumn) {
                        val columnId = remember { -1 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Normal, ColumnType.Leading) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(isFirstRow, columnState))
                    }
                    (0 until tableState.dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Normal, ColumnType.Normal) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(isFirstRow, columnState))
                    }
                    if (tableState.hasTailColumn) {
                        val columnId = remember { -2 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Normal, ColumnType.Tail) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(isFirstRow, columnState))
                    }
                    Box(modifier = Modifier.height(rowState.height).width(18.dp))
                }
                HorizontalDivider()
            }
        )
        if (tableState.hasFooterRow) {
            item {
                val rowId = remember { -2 }
                val rowState = tableState.rememberRowState(rowId)
                FullWidthRow(modifier = Modifier.height(rowState.height)) {
                    VerticalDivider()
                    if (tableState.hasLeadingColumn) {
                        val columnId = remember { -1 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Footer, ColumnType.Leading) }
                        )
                        VerticalDivider()
                    }
                    (0 until tableState.dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Footer, ColumnType.Normal) }
                        )
                        VerticalDivider()
                    }
                    if (tableState.hasTailColumn) {
                        val columnId = remember { -2 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.width),
                            content = { onItemContent(rowId, columnId, RowType.Footer, ColumnType.Tail) }
                        )
                        VerticalDivider()
                    }
                }
            }
        }
    }
}



