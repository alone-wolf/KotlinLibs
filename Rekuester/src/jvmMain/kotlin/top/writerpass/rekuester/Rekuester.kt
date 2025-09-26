package top.writerpass.rekuester

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullHeightColumn
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.FullWidthBox
import top.writerpass.cmplibrary.compose.FullWidthColumn
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
import top.writerpass.rekuester.ui.componment.DraggableDivideBar
import top.writerpass.rekuester.ui.componment.TabBarWithContent
import top.writerpass.rekuester.ui.page.ApiRequestPage
import top.writerpass.rekuester.ui.part.ApisListView
import top.writerpass.rekuester.viewmodel.ApiRequestViewModel
import top.writerpass.rekuester.viewmodel.MainUiViewModel
import top.writerpass.rekuester.viewmodel.MainViewModel
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


// TODO 增加Api列表的排序选项
// TODO 增加ApiTabBar for Opened API
// TODO 增加自定义Params的编辑功能
// TODO 增加自定义Headers的编辑功能
// TODO 增加自定义Body的编辑功能
// TODO 使用Navigation作为导航

fun main() = singleWindowApplication(
    title = "Rekuester",
) {
    this.window.minimumSize = Dimension(800, 600)
    this.window.menuBar = MenuBar().apply {
    }
    val client = remember { RekuesterClient() }
    val mainViewModel = viewModel { MainViewModel() }
    val mainUiViewModel = viewModel { MainUiViewModel() }
    val navController = rememberNavController()


    CompositionLocalProvider(
        LocalMainViewModel provides mainViewModel,
        LocalMainUiViewModel provides mainUiViewModel,
        LocalNavController provides navController
    ) {
        FullSizeRow {
            ApisListView()
            DraggableDivideBar(mainUiViewModel.apisListViewWidth) { it ->
                mainUiViewModel.apisListViewWidth = it
            }
            NavHost(navController, startDestination = Pages.BlankPage) {
                composable<Pages.BlankPage>(
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {
                    FullSizeBox {
                        "This is a Blank Page, select an API to start".Text(
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }
                composable<Pages.ApiRequestPage>(
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {
                    val apiId = it.savedStateHandle.toRoute(Pages.ApiRequestPage::class).uuid
                    val api = mainViewModel.apis.find { it.uuid == apiId }!!
                    val apiRequestViewModel = viewModel(
                        viewModelStoreOwner = it,
                        key = apiId,
                        initializer = { ApiRequestViewModel(
                            savedStateHandle = it.savedStateHandle,
                            client = client,
                            uuid = it.savedStateHandle.toRoute(Pages.ApiRequestPage::class).uuid,
                            api = api
                        ) }
                    )
                    ApiRequestPage(api = api, apiRequestViewModel = apiRequestViewModel)
                }
            }
        }
    }
}



