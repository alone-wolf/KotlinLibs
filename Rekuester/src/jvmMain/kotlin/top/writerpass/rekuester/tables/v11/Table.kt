package top.writerpass.rekuester.tables.v11

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.MutableStateComposeExt.CxBasicTextField
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.horizontalDivider
import top.writerpass.cmplibrary.pointerIcons.XResize
import top.writerpass.cmplibrary.pointerIcons.YResize
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate

@JvmName("sumOfDp")
private inline fun <T> Iterable<T>.sumOf(selector: (T) -> Dp): Dp {
    var sum: Dp = 0.dp
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

private class TableAxisStates(val strategy: TableStrategies.Axis) {
    private val _value = mutableStateOf(
        when (strategy) {
            is TableStrategies.Axis.Fixed -> strategy.value
            is TableStrategies.Axis.Flexible -> strategy.default
            is TableStrategies.Axis.Ranged -> strategy.default
//            TableStrategies.Axis.WrapContent -> (-1).dp
//            is TableStrategies.Axis.Custom -> 0.dp
        }
    )
    val value by _value

    val updateValue: (newValue: Dp, availableSpace: Dp, id: Int) -> Unit = when (strategy) {
        is TableStrategies.Axis.Fixed -> { _, _, _ -> }
        is TableStrategies.Axis.Flexible -> { newValue, _, _ ->
            _value.value = newValue
        }

        is TableStrategies.Axis.Ranged -> { newValue, availableSpace, id ->
            val newValue = newValue.coerceIn(strategy.min, strategy.max)
            _value.value = newValue
        }
//        TableStrategies.Axis.WrapContent -> { _, _, _ -> }

//
//        is TableStrategies.Axis.Custom -> { newValue, availableSpace, id ->
//            _value.value = strategy.resolver(availableSpace, id)
//        }
    }
}


private class TableState(
    val defaultWidth: Dp = 120.dp,
    val defaultHeight: Dp = 40.dp,
    val strategies: TableStrategies,
    val extras: TableState.() -> Unit = {}
) {

    private val rowStateMap = mutableStateMapOf<Int, TableAxisStates>()

    private val columnStateMap = mutableStateMapOf<Int, TableAxisStates>()

    init {
        extras()
    }

    fun preSetColumnState(
        id: Int, strategy: TableStrategies.Axis = strategies.defaultColumn
    ) {
        if (columnStateMap.contains(id).not()) {
            columnStateMap[id] = TableAxisStates(strategy)
        }
    }

    fun preSetRowState(
        id: Int, strategy: TableStrategies.Axis = strategies.defaultColumn
    ) {
        if (rowStateMap.contains(id).not()) {
            rowStateMap[id] = TableAxisStates(strategy)
        }
    }

    @Composable
    fun rememberColumnState(
        id: Int, strategy: TableStrategies.Axis = strategies.defaultColumn
    ) = remember { columnStateMap.getOrCreate(id) { TableAxisStates(strategy) } }

    @Composable
    fun rememberRowState(
        id: Int, strategy: TableStrategies.Axis = strategies.defaultRow
    ) = remember { rowStateMap.getOrCreate(id) { TableAxisStates(strategy) } }

    val tableHeight: Dp by derivedStateOf {
        var sumOfHeight = 0.dp
        columnStateMap.values.forEach { states ->
            val strategy = states.strategy
            sumOfHeight += when (strategy) {
                is TableStrategies.Axis.Fixed -> states.value
                is TableStrategies.Axis.Flexible -> states.value
                is TableStrategies.Axis.Ranged -> states.value
//                TableStrategies.Axis.WrapContent -> states.value
//                is TableStrategies.Axis.Custom -> states.value
            }
        }
        sumOfHeight
    }

    val tableWidth: Dp by derivedStateOf {
        var sumOfWidth = 0.dp
        columnStateMap.values.forEach { states ->
            val strategy = states.strategy
            sumOfWidth += when (strategy) {
                is TableStrategies.Axis.Fixed -> states.value
                is TableStrategies.Axis.Flexible -> states.value
                is TableStrategies.Axis.Ranged -> states.value
//                TableStrategies.Axis.WrapContent -> states.value
//                is TableStrategies.Axis.Custom -> states.value
            }
        }
        sumOfWidth
    }
}

private fun Modifier.tableSize(tableState: TableState): Modifier {
    return then(
        when (tableState.strategies.horizontal) {
            TableStrategies.Size.FillContainer -> Modifier.fillMaxWidth()
            is TableStrategies.Size.Fixed -> width(tableState.strategies.horizontal.value)
            TableStrategies.Size.WrapContent -> Modifier.width(tableState.tableWidth)
        }
    ).then(
        when (tableState.strategies.vertical) {
            TableStrategies.Size.FillContainer -> Modifier.fillMaxHeight()
            is TableStrategies.Size.Fixed -> height(tableState.strategies.vertical.value)
            TableStrategies.Size.WrapContent -> Modifier.height(tableState.tableHeight)
        }
    )
}

private fun Modifier.horizontalDraggable(
    isFirstRow: Boolean,
    columnState: TableAxisStates
): Modifier {
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
                            columnState.updateValue(rr, 0.dp, 0)
                        }),
                        orientation = Orientation.Horizontal
                    )
                }
        } else {
            Modifier
        }
    )
}

private fun Modifier.verticalDraggable(
    rowState: TableAxisStates
): Modifier {
    return pointerHoverIcon(PointerIcon.YResize)
        .composed("verticalDraggable") {
            val density = LocalDensity.current
            draggable(
                state = rememberDraggableState(onDelta = { delta ->
                    val rr = with(density) {
                        (rowState.value.toPx() + delta).toDp()
                    }.coerceIn(20.dp, 500.dp)
                    rowState.updateValue(rr, 0.dp, 0)
                }),
                orientation = Orientation.Vertical
            )
        }
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

private class TableStrategies(
    val horizontal: Size,
    val vertical: Size,
    val defaultRow: Axis,
    val defaultColumn: Axis
) {
    @Stable
    sealed interface Size {
        class Fixed(val value: Dp) : Size
        object FillContainer : Size
        object WrapContent : Size
    }

    @Stable
    sealed interface Axis {
        class Fixed(val value: Dp) : Axis
        class Flexible(val default: Dp) : Axis
        class Ranged(val min: Dp, val max: Dp, val default: Dp) : Axis
//        object WrapContent : Axis
//        class Custom(val resolver: (availableSpace: Dp, id: Int) -> Dp) : Axis
//    object FillContainer : TableAxisStrategy
    }
}

private const val HeaderRowId = -1
private const val FooterRowId = -2
private const val LeadingColumnId = -1
private const val TailColumnId = -2

private fun Modifier.vBind(rowState: TableAxisStates, columnState: TableAxisStates): Modifier {
    return composed("v-bind-row") {
        this
    }.composed("v-bind-column") {
        val density = LocalDensity.current
        when (columnState.strategy) {
            is TableStrategies.Axis.Fixed -> width(columnState.value)
            is TableStrategies.Axis.Flexible -> width(columnState.value)
            is TableStrategies.Axis.Ranged -> width(columnState.value)
//            is TableStrategies.Axis.WrapContent -> onSizeChanged { intSize->
//                val newWidth = with(density){
//                    intSize.width.toDp()
//                }
//                if (columnState.wrapAxisContentValue.value < newWidth) {
//                    columnState.wrapAxisContentValue.value = newWidth
//                }
//            }
        }
    }
}

/**
 * ||/////////////////||
 * ||-----------------||
 * ||-----------------||
 * ||/////////////////||
 * */
@Composable
private fun CommonTableFrame(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    tableState: TableState,
    dataRowCount: Int,
    dataColumnCount: Int,
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
            .padding(4.dp),
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
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { leadingItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    (0 until dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { headerItemContent?.invoke(this, columnId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    if (hasTailColumn) {
                        val columnState = tableState.rememberColumnState(TailColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { tailItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(modifier = Modifier.horizontalDraggable(true, columnState))
                    }
                    Box(modifier = Modifier.fillMaxHeight().width(18.dp))
                }
                HorizontalDivider(
                    modifier = Modifier.then(
                        if (hasLeadingColumn) {
                            Modifier.verticalDraggable(rowState)
                        } else {
                            Modifier
                        }
                    )
                )
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
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { leadingItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(
                            modifier = Modifier.horizontalDraggable(
                                isFirstRow,
                                columnState
                            )
                        )
                    }
                    (0 until dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { dataItemContent(rowId, columnId) }
                        )
                        VerticalDivider(
                            modifier = Modifier.horizontalDraggable(
                                isFirstRow,
                                columnState
                            )
                        )
                    }
                    if (hasTailColumn) {
                        val columnState = tableState.rememberColumnState(TailColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { tailItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider(
                            modifier = Modifier.horizontalDraggable(
                                isFirstRow,
                                columnState
                            )
                        )
                    }
                    Box(modifier = Modifier.fillMaxHeight().width(18.dp))
                }
                HorizontalDivider(
                    modifier = Modifier.verticalDraggable(rowState)
                )
            }
        )
        if (hasFooterRow) {
            item {
                val rowId = FooterRowId
                val rowState = tableState.rememberRowState(rowId)
                FullWidthRow(modifier = Modifier.height(rowState.value)) {
                    VerticalDivider()
                    if (hasLeadingColumn) {
                        val columnState = tableState.rememberColumnState(LeadingColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { leadingItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider()
                    }
                    (0 until dataColumnCount).forEach { columnId ->
                        val columnState = tableState.rememberColumnState(columnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { footerItemContent?.invoke(this, columnId) }
                        )
                        VerticalDivider()
                    }
                    if (hasTailColumn) {
                        val columnState = tableState.rememberColumnState(TailColumnId)
                        Box(
                            modifier = Modifier.fillMaxHeight().vBind(rowState, columnState),
                            content = { tailItemContent?.invoke(this, rowId) }
                        )
                        VerticalDivider()
                    }
                    Box(modifier = Modifier.fillMaxHeight().width(18.dp))
                }
                HorizontalDivider(
                    modifier = Modifier.verticalDraggable(rowState)
                )
            }
        }
    }
}


private fun main() = singleWindowApplication {
    val e = Mutable.someBoolean()
    val k = Mutable.someString("")
    val v = Mutable.someString("")
    val d = Mutable.someString("")

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
                strategies = TableStrategies(
                    horizontal = TableStrategies.Size.FillContainer,
                    vertical = TableStrategies.Size.FillContainer,
                    defaultRow = TableStrategies.Axis.Fixed(40.dp),
                    defaultColumn = TableStrategies.Axis.Fixed(120.dp)
                ),
                extras = {
                    preSetColumnState(
                        id = -1,
                        strategy = TableStrategies.Axis.Fixed(30.dp)
                    )
                    preSetColumnState(
                        id = 0,
                        strategy = TableStrategies.Axis.Ranged(
                            min = 100.dp,
                            max = 300.dp,
                            default = 120.dp
                        )
                    )
                    preSetColumnState(
                        id = 1,
                        strategy = TableStrategies.Axis.Ranged(
                            min = 100.dp,
                            max = 300.dp,
                            default = 120.dp
                        )
                    )
                    preSetColumnState(
                        id = 2,
                        strategy = TableStrategies.Axis.Ranged(
                            min = 100.dp,
                            max = 300.dp,
                            default = 120.dp
                        )
                    )
                }
            )
        },
        dataRowCount = 15,
        dataColumnCount = 3,
        headerItemContent = { columnId ->
            val headers = remember { listOf("Key", "Value", "Description") }
            val header = remember { headers[columnId] }
            header.CxText(modifier = Modifier.align(Alignment.Center))
        },
        footerItemContent = { columnId ->
            when (columnId) {
                0 -> k.CxBasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
                1 -> v.CxBasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
                2 -> d.CxBasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
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
                    "Actions".CxText(modifier = Modifier.align(Alignment.Center))
                }

                FooterRowId -> {
                    Row {
                        Icons.Default.Delete.CxIcon()
                        Icons.Default.Save.CxIcon()
                    }
                }

                else -> {
                    Row {
                        Icons.Default.CleanHands.CxIcon()
                        Icons.Default.Add.CxIcon()
                    }
                }
            }
        },
        dataItemContent = { rowId, columnId ->
            val density = LocalDensity.current
            val item = remember(rowId, columnId) {
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
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            ),
                        )
                        Icons.Default.Check.CxIcon(modifier = Modifier.size(16.dp).clickable {
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



