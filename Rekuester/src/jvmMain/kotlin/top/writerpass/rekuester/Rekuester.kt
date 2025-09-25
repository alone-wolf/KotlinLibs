package top.writerpass.rekuester

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullHeightColumn
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.FullWidthBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.OutlinedTextFiled
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.modifier.onPointerHover
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import java.awt.Dimension
import java.awt.MenuBar
import java.util.UUID

@Serializable
enum class BodyType {
    None, Form, UrlEncodedForm, Raw, Binary, GraphQL,
}

@Serializable
abstract class ApiRequestBodyContainer(
    val type: BodyType
)

@Serializable
data class ApiBasicInfo(
    val label: String,
    @Serializable(with = HttpMethodSerializer::class) val method: HttpMethod,
    val address: String, // scheme://host:port
)

@Serializable
data class Api(
    val uuid: String = UUID.randomUUID().toString(),
    val basicInfo: ApiBasicInfo,
    val params: Map<String, List<String>> = emptyMap(),
    val headers: Map<String, List<String>> = emptyMap(),
    val requestBody: ApiRequestBodyContainer? = null
)

@Composable
private fun ApiRequestPage(api: Api, client: RekuesterClient) {
    FullSizeColumn(modifier = Modifier) {
        val mainViewModel = LocalMainViewModel.current
        val apiRequestViewModel = viewModel(key = api.uuid) {
            ApiRequestViewModel(
                client = client, api = api
            )
        }

        var isFirst by remember { mutableStateOf(true) }

        LaunchedEffect(
            apiRequestViewModel.label.value,
            apiRequestViewModel.method.value,
            apiRequestViewModel.address.value,
            apiRequestViewModel.params,
            apiRequestViewModel.headers,
        ) {
            if (isFirst) {
                isFirst = false
            } else {
                apiRequestViewModel.isModified.setTrue()
            }
        }
        FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
            var editLabel by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (editLabel) {
                    apiRequestViewModel.label.OutlinedTextFiled()
                    Icons.Default.Check.IconButton { editLabel = false }
                } else {
                    apiRequestViewModel.label.value.Text()
                    Icons.Default.Edit.IconButton { editLabel = true }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (apiRequestViewModel.isModified.value) "Save".TextButton {
                val newApi = apiRequestViewModel.composeNewApi()
                mainViewModel.updateApi(newApi)
                apiRequestViewModel.isModified.setFalse()
            }
        }
        FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
            apiRequestViewModel.method.DropDownMenu(
                entities = remember {
                    HttpMethod.DefaultMethods.associateBy { it.value }
                },
                any2String = { this.value }
            )
            Spacer(modifier = Modifier.width(8.dp))
            apiRequestViewModel.address.OutlinedTextFiled(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            "Send".OutlinedButton {
                apiRequestViewModel.request()
            }
        }
        FullSizeColumn {
            "Request".Text()
            val requestContentTabId = Mutable.someInt()
            FullWidthRow {
                remember {
                    listOf(
                        "Param", "Authorization", "Headers", "Body", "Scripts", "Settings"
                    )
                }.forEachIndexed { index, string ->
                    string.TextButton { requestContentTabId.value = index }
                }
            }
            "Response".Text()
            val responseContentTabId = Mutable.someInt()
            FullWidthRow {
                remember {
                    listOf(
                        "Overview", "Body", "Cookies", "Headers"
                    )
                }.forEachIndexed { index, string ->
                    string.TextButton {
                        responseContentTabId.value = index
                    }
                }
            }
            apiRequestViewModel.currentResult?.let { reqResult ->
                reqResult.response?.let { result ->
                    FullSizeColumn(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        when (responseContentTabId.value) {
                            0 -> {
                                "Overview".Text()
                                "Status: ${result.code}".Text()
                                "Requested at: ${result.reqTime}".Text()
                                "Responded at: ${result.respTime}".Text()
                            }

                            1 -> {
                                "Requested at: ${result.reqTime}".Text()
                                "Responded at: ${result.respTime}".Text()
                                result.body.Text()
                            }

                            2 -> {}
                            3 -> {
                                result.headers.forEach { (key, values) ->
                                    values.forEach { value ->
                                        FullWidthRow(modifier = Modifier.clickable {}) {
                                            key.Text(modifier = Modifier.weight(0.4f))
                                            value.Text(modifier = Modifier.weight(1f))
                                        }
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                } ?: FullSizeColumn {
                    "Error: ${reqResult.error}".Text()
                }

            }
        }
    }
}

@Composable
private fun LeftList() {
    val mainViewModel = LocalMainViewModel.current
    val mainUiViewModel = LocalMainUiViewModel.current
    FullHeightColumn(modifier = Modifier.width(mainUiViewModel.leftListWidth)) {
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
        LazyColumn(modifier = Modifier.fillMaxHeight().weight(1f)) {
            items(
                items = mainViewModel.apis, itemContent = { api ->
                    val onHover = Mutable.someBoolean()
                    FullWidthBox(
                        modifier = Modifier.height(45.dp)
                            .clickable { mainViewModel.updateCurrentApi(api) }
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
                })
        }
    }
}

@Composable
fun DraggableDivideBar(value: Dp, onDrag: (Dp) -> Unit) {
    val density = LocalDensity.current
    val draggableState = rememberDraggableState(onDelta = { delta ->
        val result = with(density) {
            (value.toPx() + delta).toDp()
        }.coerceIn(150.dp, 300.dp)
        onDrag(result)
    })

    val onBarHover = Mutable.someBoolean()
    val barWidth by animateDpAsState(if (onBarHover.value) 5.dp else 2.dp)
    Box(
        modifier = Modifier.fillMaxHeight().width(barWidth).background(Color.Gray).onPointerHover(
            onHover = { onBarHover.value = true },
            onNotHover = { onBarHover.value = false }).pointerHoverIcon(
            PointerIcon.Hand
        ).draggable(
            state = draggableState, orientation = Orientation.Horizontal
        )
    )
}

val LocalMainViewModel =
    staticCompositionLocalOf<MainViewModel> { error("No MainViewModel provided") }
val LocalMainUiViewModel =
    staticCompositionLocalOf<MainUiViewModel> { error("No MainUiViewModel provided") }

fun main() = singleWindowApplication(
    title = "Rekuester",
) {
    this.window.minimumSize = Dimension(800, 600)
    this.window.menuBar = MenuBar().apply {
    }
    val client = remember { RekuesterClient() }
    val mainViewModel = viewModel { MainViewModel() }
    val mainUiViewModel = viewModel { MainUiViewModel() }

    CompositionLocalProvider(
        LocalMainViewModel provides mainViewModel,
        LocalMainUiViewModel provides mainUiViewModel,
    ) {
        FullSizeRow {
            LeftList()
            DraggableDivideBar(mainUiViewModel.leftListWidth) { it ->
                mainUiViewModel.leftListWidth = it
            }
            mainViewModel.currentApi?.let {
                ApiRequestPage(it, client)
            }
        }
    }
}
// TODO 增加Api列表的排序选项
// TODO 增加TabBar for Opened API
// TODO 增加自定义Params、Headers、Body的编辑功能

