package top.writerpass.rekuester.tables.v10

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
import top.writerpass.cmplibrary.compose.OutlinedBasicTextField
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.pointerIcons.XResize
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.rekuester.tables.v10.CommonTableFrames.horizontalDivider
import top.writerpass.rekuester.tables.v10.CommonTableFrames.horizontalDraggable
import top.writerpass.rekuester.tables.v10.CommonTableFrames.tableWidth

@JvmName("sumOfDp")
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Dp): Dp {
    var sum: Dp = 0.dp
    for (element in this) {
        sum += selector(element)
    }
    return sum
}


sealed class TableAxisStates(default: Dp) {
    private val _value = mutableStateOf(default)
    val value by _value
    open fun updateValue(newValue: Dp) {
        _value.value = newValue
    }

    class AxisState(default: Dp) : TableAxisStates(default) {}

    @Stable
    class FixedAxisState(fixedValue: Dp) : TableAxisStates(fixedValue) {
        override fun updateValue(newValue: Dp) {}
    }

    class RangedAxisState(private val min: Dp, private val max: Dp, default: Dp) : TableAxisStates(default) {
        override fun updateValue(newValue: Dp) {
            if (newValue > min && newValue < max) {
                super.updateValue(newValue)
            }
        }
    }
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
    val columnWidthStrategy: TableColumnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible,
) {
    private val rowStateMap = mutableStateMapOf<Int, TableAxisStates>()

    @Composable
    fun rememberRowState(id: Int) = remember {
        rowStateMap.getOrCreate(id) { TableAxisStates.AxisState(defaultHeight) }
    }

    @Composable
    fun rememberFixedRowState(id: Int, height: Dp = defaultHeight) = remember {
        rowStateMap.getOrCreate(id) { TableAxisStates.FixedAxisState(height) }
    }

    @Composable
    fun rememberRangedRowState(id: Int, min: Dp, max: Dp, default: Dp = defaultHeight) = remember {
        rowStateMap.getOrCreate(id) { TableAxisStates.RangedAxisState(min, max, default) }
    }


    val columnStateMap = mutableStateMapOf<Int, TableAxisStates>()

    @Composable
    fun rememberColumnState(id: Int) = remember {
        columnStateMap.getOrCreate(id) { TableAxisStates.AxisState(defaultWidth) }
    }

    @Composable
    fun rememberFixedColumnState(id: Int, width: Dp = defaultWidth) = remember {
        columnStateMap.getOrCreate(id) { TableAxisStates.FixedAxisState(width) }
    }

    @Composable
    fun rememberRangedColumnState(id: Int, min: Dp, max: Dp, default: Dp = defaultWidth) = remember {
        columnStateMap.getOrCreate(id) { TableAxisStates.RangedAxisState(min, max, default) }
    }

    val tableHeight: Dp by derivedStateOf {
        rowStateMap.values.sumOf { it.value }
    }

    val tableWidth: Dp by derivedStateOf {
        when (tableWidthStrategy) {
            TableWidthStrategy.WrapContent -> {
                columnStateMap.values.sumOf { it.value }
            }

            else -> 0.dp
        }
    }
}

object CommonTableFrames {
    fun Modifier.tableWidth(tableState: TableState): Modifier {
        return then(
            when (tableState.tableWidthStrategy) {
                is TableWidthStrategy.WrapContent -> {
                    Modifier.width(width = tableState.tableWidth + 18.dp)
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
                tableWidthStrategy = TableWidthStrategy.WrapContent,
                columnWidthStrategy = TableColumnWidthStrategy.DefaultFlexible,
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
                    Checkbox(e.value, {e.value = it}, modifier = Modifier.align(Alignment.Center))
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

    val tableColumnCount = remember {
        var count = dataColumnCount
        if (hasLeadingColumn) count+=1
        if (hasTailColumn) count +=1
        count
    }

    LazyColumn(
        modifier = modifier
            .tableWidth(tableState)
            .padding(4.dp)
//            .watchTableSize(tableState,tableColumnCount)
        ,
        state = listState
    ) {
        horizontalDivider()
        if (hasHeaderRow) {
            item {
                val rowId = HeaderRowId
                val rowState = tableState.rememberFixedRowState(rowId)
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
                val rowState = tableState.rememberFixedRowState(rowId)
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
                val rowState = tableState.rememberFixedRowState(rowId)
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



