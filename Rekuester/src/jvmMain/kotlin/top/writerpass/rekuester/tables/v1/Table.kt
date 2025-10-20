package top.writerpass.rekuester.tables.v1

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.kmplibrary.utils.getOrCreate

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

@Composable
private fun Table1(
    dataSet: List<List<Any>> = listOf(
        listOf("AAAAAA", "BBAAAA", "CCAAAA", "DDAAAA", "EEAAAA", "FFAAAA"),
        listOf("AA", "BB", "CC", "DD", "EE", "FF"),
        listOf("AA", "BB", "CC", "DD", "EE", "FF"),
        listOf("AA", "BB", "CC", "DD", "EE", "FF"),
        listOf("AA", "BB", "CC", "DD", "EE", "FF"),
    ),
    state: TableState = TableState,
) {
    Column(
        modifier = Modifier
            .size(
                width = state.tableWidth,
                height = state.tableHeight
            )
            .padding(18.dp)
    ) {
        val density = LocalDensity.current
        HorizontalDivider()
        dataSet.forEachIndexed { i, dataRow ->
            val rowState = state.rememberRowState(i)
            FullWidthRow(modifier = Modifier.height(rowState.height)) {
                VerticalDivider()
                dataRow.forEachIndexed { i, data ->
                    val columnState = state.rememberColumnState(i)
                    val dateValue = remember { data.toString() }
                    val draggableState = rememberDraggableState(onDelta = { delta ->
                        val rr = with(density) {
                            (columnState.width.toPx() + delta).toDp()
                        }
                        columnState.width = rr
                    })
                    Text(
                        text = dateValue,
                        modifier = Modifier
                            .width(columnState.width)
                            .fillMaxHeight()
                            .background(Color.LightGray)
                    )
                    VerticalDivider(
                        color = Color.Black,
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .draggable(
                                state = draggableState,
                                orientation = Orientation.Horizontal
                            )
                    )
                }
            }
            val draggableState1 = rememberDraggableState(onDelta = { delta ->
                val rr = with(density) {
                    (rowState.height.toPx() + delta).toDp()
                }
                rowState.height = rr
            })
            HorizontalDivider(
                color = Color.Black,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .draggable(
                        state = draggableState1,
                        orientation = Orientation.Vertical
                    )
            )
        }
    }
}