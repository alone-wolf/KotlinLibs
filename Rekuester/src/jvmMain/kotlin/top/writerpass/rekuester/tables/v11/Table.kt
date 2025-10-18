package top.writerpass.rekuester.tables.v11

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.singleWindowApplication
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.BasicTextField
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.pointerIcons.XResize
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.rekuester.tables.v11.CommonTableFrames.horizontalDivider
import top.writerpass.rekuester.tables.v11.CommonTableFrames.horizontalDraggable
import top.writerpass.rekuester.tables.v11.CommonTableFrames.tableSize

@JvmName("sumOfDp")
private inline fun <T> Iterable<T>.sumOf(selector: (T) -> Dp): Dp {
    var sum: Dp = 0.dp
    for (element in this) {
        sum += selector(element)
    }
    return sum
}


class TableAxisStates(val strategy: TableAxisStrategy) {
    private val _value = mutableStateOf(
        when (strategy) {
            is TableAxisStrategy.Ranged -> strategy.default
            is TableAxisStrategy.Fixed -> strategy.value // 后面可传入
            is TableAxisStrategy.Flexible -> strategy.default
            is TableAxisStrategy.Custom -> 0.dp
            TableAxisStrategy.FillContainer -> 0.dp
            TableAxisStrategy.WrapContent -> 0.dp
        }
    )
    val value by _value

    val updateValue: (new,availableSpace: Dp, id: Int) -> Unit = when (strategy) {
        is TableAxisStrategy.Ranged -> { availableSpace, id ->
            val newValue = value.coerceIn(strategy.min, strategy.max)
            _value.value = newValue
        }

        is TableAxisStrategy.Custom -> { availableSpace, id ->
            _value.value = strategy.resolver(availableSpace, id)
        }

        TableAxisStrategy.FillContainer -> TODO()
        is TableAxisStrategy.Fixed -> {_,_-> }
        is TableAxisStrategy.Flexible -> {_,_->}
        TableAxisStrategy.WrapContent -> TODO()
    }
}

@Stable
sealed interface TableAxisStrategy {
    class Flexible(val default: Dp) : TableAxisStrategy
    class Fixed(val value: Dp) : TableAxisStrategy
    class Ranged(val min: Dp, val max: Dp, val default: Dp) : TableAxisStrategy
    class Custom(val resolver: (availableSpace: Dp, id: Int) -> Dp) : TableAxisStrategy

    object WrapContent : TableAxisStrategy
    object FillContainer : TableAxisStrategy
}

@Stable
sealed interface TableSizeStrategy {
    class Fixed(val value: Dp) : TableSizeStrategy
    object FillContainer : TableSizeStrategy
    object WrapContent : TableSizeStrategy
}

class TableState(
    val defaultWidth: Dp = 120.dp,
    val defaultHeight: Dp = 40.dp,
    val tableWidthStrategy: TableSizeStrategy = TableSizeStrategy.WrapContent,
    val tableHeightStrategy: TableSizeStrategy = TableSizeStrategy.WrapContent,
    val defaultColumnStrategy: TableAxisStrategy = TableAxisStrategy.Flexible(120.dp),
    val defaultRowStrategy: TableAxisStrategy = TableAxisStrategy.Fixed(40.dp),
) {
    private val rowStateMap = mutableStateMapOf<Int, TableAxisStates>()
    val columnStateMap = mutableStateMapOf<Int, TableAxisStates>()

    @Composable
    fun rememberColumnState(id: Int, strategy: TableAxisStrategy = defaultColumnStrategy) =
        remember { columnStateMap.getOrCreate(id) { TableAxisStates(strategy) } }

    @Composable
    fun rememberRowState(id: Int, strategy: TableAxisStrategy = defaultRowStrategy) =
        remember { rowStateMap.getOrCreate(id) { TableAxisStates(strategy) } }


    val tableHeight: Dp by derivedStateOf {
        rowStateMap.values.sumOf { it.value }
    }

    val tableWidth: Dp by derivedStateOf {
        when (tableWidthStrategy) {
            TableSizeStrategy.WrapContent -> {
                columnStateMap.values.sumOf { it.value }
            }

            TableSizeStrategy.FillContainer -> {
                0.dp
            }

            is TableSizeStrategy.Fixed -> {
                0.dp
            }
        }
    }
}

object CommonTableFrames {
    fun Modifier.tableSize(tableState: TableState): Modifier {
        return then(
            when (tableState.tableWidthStrategy) {
                TableSizeStrategy.FillContainer -> Modifier.fillMaxWidth()
                is TableSizeStrategy.Fixed -> Modifier.width(tableState.tableWidthStrategy.value)
                TableSizeStrategy.WrapContent -> Modifier.width(tableState.tableWidth)
            }.then(
                when (tableState.tableHeightStrategy) {
                    TableSizeStrategy.FillContainer -> Modifier.fillMaxHeight()
                    is TableSizeStrategy.Fixed -> Modifier.height(tableState.tableHeightStrategy.value)
                    TableSizeStrategy.WrapContent -> Modifier.height(tableState.tableHeight)
                }
            )
        )
    }

//    fun Modifier.watchTableSize(tableState: TableState,tableColumnCount: Int): Modifier {
//        return composed("watchSize") {
//            val density = LocalDensity.current
//            val fullWidth = Mutable.something(0.dp)
//            LaunchedEffect(fullWidth.value) {
//                tableState.columnStateMap.forEach { (_, columnState) ->
//                    val newValue = ((fullWidth.value / tableColumnCount))
//                    columnState.updateValue(newValue)
//                }
//            }
//            onSizeChanged { (width, _) ->
//                fullWidth.value = with(density) { width.toDp() }
//            }
//        }
//    }

    fun LazyListScope.horizontalDivider() {
        item { HorizontalDivider() }
    }

    fun Modifier.horizontalDraggable(isFirstRow: Boolean, columnState: TableAxisStates): Modifier {
        return then(
            if (isFirstRow) {
                pointerHoverIcon(PointerIcon.XResize)
                    .composed("horizontalDraggable") {
                        val density = LocalDensity.current
                        draggable(
                            state = rememberDraggableState(onDelta = { delta ->
                                val rr = with(density) {
                                    (columnState.value.toPx() + delta).toDp()
                                }.coerceIn(20.dp, 500.dp)
                                columnState.updateValue(rr)
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

fun main() = singleWindowApplication {
    val e = Mutable.someBoolean()
    val k = Mutable.someString("")
    val v = Mutable.someString("")
    val d = Mutable.someString("")

    "aaa".Text()

    CommonTableFrame(
        modifier = Modifier.border(
            width = 2.dp,
            color = Color.Green,
            shape = RoundedCornerShape(4.dp)
        ),
        listState = rememberLazyListState(),
        tableState = remember {
            TableState(
                defaultWidth = 120.dp,
                defaultHeight = 40.dp,
                tableWidthStrategy = TableSizeStrategy.Fixed(500.dp),
                tableHeightStrategy = TableSizeStrategy.Fixed(500.dp),
                defaultColumnStrategy = TableAxisStrategy.Flexible(120.dp),
                defaultRowStrategy = TableAxisStrategy.Flexible(40.dp),
            )
        },
        dataRowCount = 15,
        dataColumnCount = 3,
        headerItemContent = { columnId ->
            val headers = remember { listOf("Key", "Value", "Description") }
            val header = remember { headers[columnId] }
            header.Text(modifier = Modifier.align(Alignment.Center))
        },
        footerItemContent = { columnId ->
            when (columnId) {
                0 -> k.BasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
                1 -> v.BasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
                2 -> d.BasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
            }
        },
        leadingItemContent = { rowId ->
            when (rowId) {
                HeaderRowId -> {
                    Checkbox(false, {}, modifier = Modifier.align(Alignment.Center))
                }

                FooterRowId -> {
                    Checkbox(e.value, { e.value = it }, modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    Checkbox(true, {}, modifier = Modifier.align(Alignment.Center))
                }
            }
        },
        tailItemContent = { rowId ->
            when (rowId) {
                HeaderRowId -> {
                    "Actions".Text(modifier = Modifier.align(Alignment.Center))
                }

                FooterRowId -> {
                    Row {
                        Icons.Default.Delete.Icon()
                        Icons.Default.Save.Icon()
                    }
                }

                else -> {
                    Row {
                        Icons.Default.CleanHands.Icon()
                        Icons.Default.Add.Icon()
                    }
                }
            }
        },
        dataItemContent = { rowId, columnId ->
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
//                                onItemChange(rowId, columnId, it.text)
                            },
                            modifier = Modifier.fillMaxHeight()
                                .weight(1f)
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
    )
}

private const val HeaderRowId = -1
private const val FooterRowId = -2
private const val LeadingColumnId = -1
private const val TailColumnId = -2

/**
 * ||/////////////////||
 * ||-----------------||
 * ||-----------------||
 * ||/////////////////||
 * */
@Composable
fun CommonTableFrame(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    tableState: TableState,
    dataRowCount: Int = 15,
    dataColumnCount: Int = 3,
    headerItemContent: (@Composable BoxScope.(columnId: Int) -> Unit)? = null, // HeaderRowId
    footerItemContent: (@Composable BoxScope.(columnId: Int) -> Unit)? = null, // FooterRowId
    leadingItemContent: (@Composable BoxScope.(rowId: Int) -> Unit)? = null, // LeadingColumnId
    tailItemContent: (@Composable BoxScope.(rowId: Int) -> Unit)? = null, // TailColumnId
    dataItemContent: @Composable BoxScope.(rowId: Int, columnId: Int) -> Unit,
) {

    val hasHeaderRow = remember { headerItemContent != null }
    val hasFooterRow = remember { footerItemContent != null }
    val hasLeadingColumn = remember { leadingItemContent != null }
    val hasTailColumn = remember { tailItemContent != null }

//    val tableColumnCount = remember {
//        var count = dataColumnCount
//        if (hasLeadingColumn) count += 1
//        if (hasTailColumn) count += 1
//        count
//    }

    LazyColumn(
        modifier = modifier
            .tableSize(tableState)
            .padding(4.dp)
//            .watchTableSize(tableState,tableColumnCount)
        ,
        state = listState
    ) {
        horizontalDivider()
        if (hasHeaderRow) {
            item {
                val rowId = HeaderRowId
                val rowState = tableState.rememberRowState(rowId)
                FullWidthRow(modifier = Modifier.height(rowState.value)) {
                    VerticalDivider()
                    if (hasLeadingColumn) {
                        val columnState = tableState.rememberColumnState(LeadingColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { leadingItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    (0 until dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { headerItemContent?.invoke(this, columnId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    if (hasTailColumn) {
                        val columnState = tableState.rememberColumnState(TailColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { tailItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    Box(modifier = Modifier.height(rowState.value).width(18.dp))
                }
                HorizontalDivider()
            }
        }
        items(
            count = dataRowCount,
            key = { it },
            itemContent = { rowId ->
                val rowState = tableState.rememberRowState(rowId)
                val isFirstRow = remember { if (hasHeaderRow) false else rowId == 0 }
                FullWidthRow(modifier = Modifier.height(rowState.value)) {
                    VerticalDivider()
                    if (hasLeadingColumn) {
                        val columnState = tableState.rememberColumnState(LeadingColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { leadingItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(isFirstRow, columnState))
                    }
                    (0 until dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { dataItemContent(rowId, columnId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(isFirstRow, columnState))
                    }
                    if (hasTailColumn) {
                        val columnState = tableState.rememberColumnState(TailColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { tailItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(isFirstRow, columnState))
                    }
                    Box(modifier = Modifier.height(rowState.value).width(18.dp))
                }
                HorizontalDivider()
            }
        )
        if (hasFooterRow) {
            item {
                val rowId = FooterRowId
                val rowState = tableState.rememberRowState(rowId)
                FullWidthRow(modifier = Modifier.height(rowState.value)) {
                    VerticalDivider()
                    if (hasLeadingColumn) {
                        val columnId = remember { -1 }
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { leadingItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider()
                    }
                    (0 until dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { footerItemContent?.invoke(this, columnId) }
                        )
                        VerticalDivider()
                    }
                    if (hasTailColumn) {
                        val columnState = tableState.rememberColumnState(TailColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().width(columnState.value),
                            content = { tailItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider()
                    }
                }
            }
        }
    }
}



