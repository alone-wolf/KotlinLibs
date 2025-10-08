package top.writerpass.rekuester.ui.part

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.rekuester.LocalCollectionApiViewModel
import top.writerpass.rekuester.Pages
import top.writerpass.rekuester.ui.page.ApiRequestPage

@Composable
fun ApiRequestPanel() {
    FullSizeColumn {
        OpenedApiTabsRow()
        FullSizeBox {
            val collectionApiViewModel = LocalCollectionApiViewModel.current
            val currentPage by collectionApiViewModel.currentPageFlow.collectAsState()
            when (currentPage) {
                is Pages.BlankPage -> {
                    FullSizeBox {
                        "This is a Blank Page, select an API to start".Text(
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }

                is Pages.ApiRequestPage -> {
                    ApiRequestPage((currentPage as Pages.ApiRequestPage).apiUuid)
                }
            }
        }
    }
}