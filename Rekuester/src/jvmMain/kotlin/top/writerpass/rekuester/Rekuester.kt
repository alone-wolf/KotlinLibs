package top.writerpass.rekuester

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Https
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import androidx.compose.ui.window.isTraySupported
import androidx.compose.ui.window.rememberTrayState
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.rekuester.data.dao.ItemWithId
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

//@Serializable
//data class ApiBasicInfo(
//    val label: String,
//    val method: HttpMethod,
//    val address: String, // scheme://host:port
//)

@Serializable
data class Api(
    val uuid: String = UUID.randomUUID().toString(),
    val collectionUUID: String = "default",
    val label: String,
    @Serializable(with = HttpMethodSerializer::class)
    val method: HttpMethod,
    val address: String,
    val params: Map<String, List<String>> = emptyMap(),
    val headers: Map<String, List<String>> = emptyMap(),
    val requestBody: ApiRequestBodyContainer? = null,
    val tabOpened: Boolean = false,
) : ItemWithId<String> {
    override val id: String = uuid
}

//// 主表
//object ApisTable : Table("apis") {
//    val uuid = varchar("uuid", 36)
//    var label = varchar("label", 100)
//    var method = varchar("method", 10)
//    var address = varchar("address", 255)
//    val requestBody = text("request_body").nullable()
//
//    override val primaryKey = PrimaryKey(uuid)
//}
//
//// Params 表 (1 Api -> N Param)
//object ApiParamsTable : Table("api_params") {
//    val id = integer("id").autoIncrement()
//    val apiUuid = varchar("api_uuid", 36).references(
//        ref = ApisTable.uuid,
//        onDelete = ReferenceOption.CASCADE
//    )
//    val key = varchar("key", 255)
//    val value = varchar("value", 1024)
//
//    override val primaryKey = PrimaryKey(id)
//}
//
//// Headers 表 (1 Api -> N Header)
//object ApiHeadersTable : Table("api_headers") {
//    val id = integer("id").autoIncrement()
//    val apiUuid = varchar("api_uuid", 36).references(
//        ref = ApisTable.uuid,
//        onDelete = ReferenceOption.CASCADE
//    )
//    val key = varchar("key", 255)
//    val value = varchar("value", 1024)
//
//    override val primaryKey = PrimaryKey(id)
//}

@Serializable
data class Collection(
    val uuid: String = UUID.randomUUID().toString(),
    val label: String,
    val createdAt: Long = System.currentTimeMillis()
) : ItemWithId<String> {
    override val id: String = uuid
}


// TODO 增加Api列表的排序选项
// TODO 增加ApiTabBar for Opened API
// TODO 增加自定义Params的编辑功能
// TODO 增加自定义Headers的编辑功能
// TODO 增加自定义Body的编辑功能
// TODO 使用Navigation作为导航



fun main() {
    val viewModelStoreOwner = object : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore = ViewModelStore()
    }

    application {

        CompositionLocalProvider(
            LocalViewModelStoreOwner provides viewModelStoreOwner,
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
                if (isTraySupported) {
                    Tray(
                        icon = rememberVectorPainter(Icons.Default.Https),
                        state = rememberTrayState(),
                        tooltip = "Rekuester",
                        onAction = { },
                        menu = {}
                    )
                }

                NewCollectionWizardWindow()
                CollectionManagerWindow()
                MainWindow()
            }
        }
    }
}







