package top.writerpass.rekuester.tables.v3

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
import androidx.compose.ui.unit.coerceIn
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

@Composable
private fun Table3(
    dataRowCount: Int = dataSet.size,
    dataColumnCount: Int = dataSet.first().size,
    onData: (rowIndex: Int, columnIndex: Int) -> String = { rowIndex, columnIndex ->
        dataSet.getOrNull(rowIndex)?.getOrNull(columnIndex) ?: "--"
    },
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
        (0 until dataRowCount).forEach { rowIndex ->
            val rowState = state.rememberRowState(rowIndex)
            val isHeaderRow = rowIndex == 0
            FullWidthRow(modifier = Modifier.height(rowState.height)) {
                VerticalDivider()
                (0 until dataColumnCount).forEach { columnIndex ->
                    val columnState = state.rememberColumnState(columnIndex)
                    val dateValue = remember { onData(rowIndex, columnIndex) }
                    Text(
                        text = dateValue,
                        modifier = Modifier
                            .width(columnState.width)
                            .fillMaxHeight()
                            .then(
                                if (isHeaderRow) {
                                    Modifier.background(Color.LightGray)
                                } else {
                                    Modifier
                                }
                            )
                    )
                    VerticalDivider(
                        modifier = Modifier.then(
                            if (isHeaderRow) {
                                Modifier.pointerHoverIcon(PointerIcon.Hand)
                                    .draggable(
                                        state = rememberDraggableState(onDelta = { delta ->
                                            val rr = with(density) {
                                                (columnState.width.toPx() + delta).toDp()
                                            }.coerceIn(0.dp, 500.dp)
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