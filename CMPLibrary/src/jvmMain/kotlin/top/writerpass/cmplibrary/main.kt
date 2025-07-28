package top.writerpass.cmplibrary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(
            title = "Test",
            onCloseRequest = ::exitApplication,
            content = { CatScreen() }
        )
    }
}

//fun dataGenerator(): List<Float> {
//    val a = mutableListOf<Float>()
//    val price = 5199f
//    for (i in 1..10000) {
//        a.add(price / i)
//    }
//    return a.toList()
//}
//
//@Composable
//fun LineChartPreview() {
//    val exampleData = dataGenerator()
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        LineChart(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),
//            data = exampleData
//        )
//    }
//}
//
//@Composable
//fun LineChart(
//    modifier: Modifier = Modifier,
//    data: List<Float>,
//    lineColor: Color = Color(0xFF3F51B5),
//    pointColor: Color = Color(0xFF3F51B5),
//    strokeWidth: Dp = 2.dp,
//    padding: Dp = 16.dp
//) {
//    if (data.isEmpty()) return
//
//    Canvas(modifier = modifier.padding(padding)) {
//        val maxValue = data.maxOrNull() ?: 0f
//        val minValue = data.minOrNull() ?: 0f
//        val valueRange = (maxValue - minValue).takeIf { it != 0f } ?: 1f
//
//        val spacing = size.width / (data.size - 1).coerceAtLeast(1)
//
//        val points = data.mapIndexed { index, value ->
//            Offset(
//                x = index * spacing,
//                y = size.height - ((value - minValue) / valueRange * size.height)
//            )
//        }
//
//        // 绘制折线
//        val path = Path().apply {
//            points.forEachIndexed { i, point ->
//                if (i == 0) moveTo(point.x, point.y)
//                else lineTo(point.x, point.y)
//            }
//        }
//        drawPath(
//            path = path,
//            color = lineColor,
//            style = Stroke(
//                width = strokeWidth.toPx(),
//                cap = StrokeCap.Round,
//                join = StrokeJoin.Round
//            )
//        )
//
//        // 绘制数据点
//        points.forEach { point ->
//            drawCircle(
//                color = pointColor,
//                radius = 4.dp.toPx(),
//                center = point
//            )
//        }
//    }
//}
