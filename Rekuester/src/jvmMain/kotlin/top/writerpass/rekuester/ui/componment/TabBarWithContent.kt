package top.writerpass.rekuester.ui.componment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.TextButton

@Composable
fun TabBarWithContent(
    entities: List<String>,
    onPage: @Composable ColumnScope.(Int) -> Unit
) {
    FullWidthColumn {
        var currentPage by remember { mutableStateOf(0) }
        FullWidthRow {
            entities.forEachIndexed { index, entity ->
                entity.TextButton(
                    modifier = Modifier.then(
                        if (currentPage == index)
                            Modifier.border(2.dp, Color.Black)
                        else Modifier
                    )
                ) { currentPage = index }
            }
        }
        FullWidthColumn {
            onPage(currentPage)
        }
    }
}