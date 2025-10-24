package top.writerpass.rekuester

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.rekuester.data.dao.ItemWithId
import top.writerpass.rekuester.ui.ApplicationTray
import top.writerpass.rekuester.ui.window.CollectionManagerWindow
import top.writerpass.rekuester.ui.window.MainWindow
import top.writerpass.rekuester.ui.window.NewCollectionWizardWindow
import top.writerpass.rekuester.viewmodel.CollectionsViewModel
import top.writerpass.rekuester.viewmodel.MainUiViewModel
import top.writerpass.rekuester.viewmodel.MainViewModel
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
data class ApiParam(
    val key: String,
    val value: String,
    val description: String = "",
    val enabled: Boolean
)

@Serializable
data class ApiHeader(
    val key: String,
    val value: String,
    val description: String
)

@Serializable
data class Api(
    val uuid: String = UUID.randomUUID().toString(),
    val collectionUUID: String = "default",
    val label: String,
    @Serializable(with = HttpMethodSerializer::class)
    val method: HttpMethod,
    val address: String,
    val params: List<ApiParam> = emptyList(),
    val headers: List<ApiHeader> = emptyList(),
    val requestBody: ApiRequestBodyContainer? = null,
//    val tabOpened: Boolean = false,
) : ItemWithId<String> {
    override val id: String = uuid

    companion object {
        val BLANK = Api(
            uuid = "--",
            collectionUUID = "--",
            label = "untitled",
            method = HttpMethod.Get,
            address = "http://",
            params = emptyList(),
            headers = emptyList(),
            requestBody = null,
        )
    }
}

@Serializable
data class Collection(
    val uuid: String = UUID.randomUUID().toString(),
    val label: String,
    val createdAt: Long = System.currentTimeMillis()
) : ItemWithId<String> {
    override val id: String = uuid

    companion object {
        val BLANK = Collection(
            uuid = "--",
            label = "untitled",
            createdAt = System.currentTimeMillis(),
        )
    }
}


// TODO 增加Api列表的排序选项
// TODO 增加自定义Params的编辑功能
// TODO 增加自定义Headers的编辑功能
// TODO 增加自定义Body的编辑功能
// TODO 使用Navigation作为导航

fun main() {
    application {
        CompositionLocalProvider(
            LocalViewModelStoreOwner provides Singletons.viewModelStoreOwner,
            LocalAppViewModelStoreOwner provides Singletons.viewModelStoreOwner,
            LocalApplicationScope provides this,
        ) {
            val mainViewModel = viewModel { MainViewModel() }
            val mainUiViewModel = viewModel { MainUiViewModel() }
            val collectionsViewModel = viewModel { CollectionsViewModel() }
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalMainViewModel provides mainViewModel,
                LocalMainUiViewModel provides mainUiViewModel,
                LocalCollectionsViewModel provides collectionsViewModel,
                LocalNavController provides navController,
            ) {
                ApplicationTray()
                NewCollectionWizardWindow()
                CollectionManagerWindow()
                MainWindow()
            }
        }
    }
}

//fun main() = singleWindowApplication {
//    val state = remember {
//        mutableStateMapOf<String,String>()
//    }
//    FullSizeColumn {
//        state.entries.forEach { (k,v)->
//            "${k}:${v}".Text()
//        }
//        DataTableEditable(
//            Modifier, 3, 5,
//            onItem = { _, _ ->
//                "--"
//            },
//            onItemChange = {rowId, columnId, itemString ->
//                state["$rowId-$columnId"] = itemString
//            }
//        )
//    }
//}






