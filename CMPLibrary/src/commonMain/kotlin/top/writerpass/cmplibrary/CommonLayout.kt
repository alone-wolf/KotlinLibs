package top.writerpass.cmplibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
private fun CommonLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        var width = 0
        var height = 0
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints).also { placeable ->
                width = max(width, placeable.measuredWidth)
                height += placeable.measuredHeight
            }
        }

        layout(width, height) {
            var totalHeight = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(0, totalHeight)
                totalHeight += placeable.measuredHeight
            }
        }
    }
}

@Composable
private fun CommonTableLayout(
    modifier: Modifier = Modifier,
//    tableState: TableState,
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

    val content: @Composable () -> Unit = {}

    Layout(content = content, modifier = modifier) { measurables, constraints ->
        var width = 0
        var height = 0

        measurables.map { measurable ->
//            measurable.
        }


        layout(width, height) {

        }
    }
}

//fun main() = singleWindowApplication {
//    CommonLayout {
//        Box(modifier = Modifier.size(80.dp).background(color = Color.Green))
//        Box(modifier = Modifier.size(120.dp).background(color = Color.Red))
//        Box(modifier = Modifier.size(60.dp).background(color = Color.Yellow))
//    }
//}