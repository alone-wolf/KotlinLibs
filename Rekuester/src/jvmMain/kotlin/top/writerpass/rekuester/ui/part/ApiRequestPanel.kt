package top.writerpass.rekuester.ui.part

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.LocalCollectionApiViewModel
import top.writerpass.rekuester.Pages
import top.writerpass.rekuester.ui.page.ApiRequestPage
import top.writerpass.rekuester.viewmodel.ApiViewModel

@Composable
fun ApiRequestPanel() {
    FullSizeColumn {
        val collectionApiViewModel = LocalCollectionApiViewModel.current
        val currentPage by collectionApiViewModel.currentPageFlow.collectAsState()
        when (currentPage) {
            is Pages.BlankPage -> {
                FullSizeBox {
                    "This is a Blank Page, select one API to start".Text(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is Pages.ApiRequestPage -> {
                val apiUuid = remember(currentPage) { (currentPage as Pages.ApiRequestPage).apiUuid }
                val apiViewModel = ApiViewModel.instance(apiUuid)
                CompositionLocalProvider(
                    LocalApiViewModel provides apiViewModel
                ) {
                    OpenedApiTabsRow()
                    ApiRequestPage()
                }
            }
        }
    }
}
