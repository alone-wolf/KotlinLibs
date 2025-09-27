package top.writerpass.rekuester

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.navigation.composableNoAnimate
import top.writerpass.rekuester.ui.componment.DraggableDivideBar
import top.writerpass.rekuester.ui.page.ApiRequestPage
import top.writerpass.rekuester.ui.part.ApisListView
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
    val mainViewModel = viewModel { MainViewModel() }
    val mainUiViewModel = viewModel { MainUiViewModel() }
    val navController = rememberNavController()


    CompositionLocalProvider(
        LocalMainViewModel provides mainViewModel,
        LocalMainUiViewModel provides mainUiViewModel,
        LocalNavController provides navController,
    ) {
        FullSizeRow {
            ApisListView()
            DraggableDivideBar(mainUiViewModel.apisListViewWidth) { it ->
                mainUiViewModel.apisListViewWidth = it
            }
            NavHost(
                navController = navController,
                startDestination = Pages.BlankPage
            ) {
                composableNoAnimate<Pages.BlankPage> {
                    FullSizeBox {
                        "This is a Blank Page, select an API to start".Text(
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }
                composableNoAnimate<Pages.ApiRequestPage> { navBackStackEntry ->
                    ApiRequestPage(navBackStackEntry = navBackStackEntry)
                }
            }
        }
    }
}







