package top.writerpass.rekuester.ui.part

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.compose.FullHeightColumn
import top.writerpass.cmplibrary.compose.FullWidthBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.modifier.onPointerHover
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.LocalMainUiViewModel
import top.writerpass.rekuester.LocalMainViewModel
import top.writerpass.rekuester.LocalNavController
import top.writerpass.rekuester.Pages

@Composable
fun ApisListView() {
    val mainViewModel = LocalMainViewModel.current
    val mainUiViewModel = LocalMainUiViewModel.current
    val navController = LocalNavController.current
    FullHeightColumn(modifier = Modifier.width(mainUiViewModel.apisListViewWidth)) {
        FullWidthRow(horizontalArrangement = Arrangement.End) {
            "Load".TextButton {
                mainViewModel.loadApis()
            }
            "Save".TextButton {
                mainViewModel.saveApis()
            }
            Icons.Default.Add.IconButton {
                mainViewModel.createNewApi()
            }
        }
        val apis by mainViewModel.allFlow.collectAsState(emptyList())
        LazyColumn(modifier = Modifier.fillMaxHeight().weight(1f)) {
            items(
                items = apis,
                itemContent = { api ->
                    val onHover = Mutable.someBoolean()
                    FullWidthBox(
                        modifier = Modifier.height(45.dp)
                            .clickable {
                                navController.navigate(Pages.ApiRequestPage(api.uuid)) {
                                    popUpTo<Pages.BlankPage>()
                                }
                            }
                            .padding(horizontal = 16.dp).onPointerHover(
                                onNotHover = { onHover.value = false },
                                onHover = { onHover.value = true }),
                    ) {
                        api.basicInfo.label.Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            AnimatedVisibility(
                                visible = onHover.value, enter = fadeIn(), exit = fadeOut()
                            ) {
                                val onIconHover = Mutable.someBoolean()
                                Box(
                                    modifier = Modifier.onPointerHover(
                                        onHover = { onIconHover.setTrue() },
                                        onNotHover = { onIconHover.setFalse() })
                                ) {
                                    AnimatedContent(onIconHover.value) {
                                        if (it) {
                                            Icons.Default.Delete.IconButton {
                                                mainViewModel.deleteApi(api)
                                            }
                                        } else {
                                            Icons.Outlined.Delete.IconButton {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}