@file:OptIn(ExperimentalMaterial3Api::class)

package top.writerpass.rekuester.ui.componment

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.ables.Composables

@Composable
fun TabBarWithContent(
    modifier: Modifier = Modifier,
    entities: List<String>,
    onPage: @Composable ColumnScope.(Int) -> Unit
) {
    FullWidthColumn {
        var currentPage by remember { mutableStateOf(0) }
        SecondaryScrollableTabRow(
            modifier = modifier,
            selectedTabIndex = currentPage,
            tabs = {
                entities.forEachIndexed { index, entity ->
                    Tab(
                        selected = currentPage == index,
                        onClick = { currentPage = index },
                        modifier = Modifier.height(45.dp),
                        enabled = true,
                        content = { entity.Composables { it.Text() } })
                }
            }
        )
        FullWidthColumn {
            onPage(currentPage)
        }
    }
}