package top.writerpass.rekuester

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Https
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.isTraySupported
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.navigation.composableNoAnimate
import top.writerpass.rekuester.data.dao.ItemWithId
import top.writerpass.rekuester.ui.componment.DraggableDivideBar
import top.writerpass.rekuester.ui.page.ApiRequestPage
import top.writerpass.rekuester.ui.part.ApisListView
import top.writerpass.rekuester.viewmodel.CollectionApiViewModel
import top.writerpass.rekuester.viewmodel.CollectionsViewModel
import top.writerpass.rekuester.viewmodel.MainUiViewModel
import top.writerpass.rekuester.viewmodel.MainViewModel
import java.awt.Dimension
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
    val requestBody: ApiRequestBodyContainer? = null
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
//    Database.connect(
//        url = "jdbc:sqlite:rekuester.db",
//        driver = "org.sqlite.JDBC",
//    )

//    transaction { SchemaUtils.create() }

    application {

        var showNewCollectionWizard by remember { mutableStateOf(false) }
//    var showMainWindow by remember { mutableStateOf(true) }

        if (isTraySupported) {
            Tray(
                icon = rememberVectorPainter(Icons.Default.Https),
                state = rememberTrayState(),
                tooltip = "Rekuester",
                onAction = { },
                menu = {}
            )
        }

        Window(
            visible = showNewCollectionWizard,
            onCloseRequest = { showNewCollectionWizard = false },
            title = "New Collection Wizard",
            content = {
            }
        )


        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(),
            visible = true,
            title = "Rekuester",
            icon = rememberVectorPainter(Icons.Default.Https),
            resizable = true,
            enabled = showNewCollectionWizard.not(),
            focusable = true,
            alwaysOnTop = false,
            content = {
                val collectionsViewModel = viewModel { CollectionsViewModel() }
                val collectionApiViewModel = viewModel(
                    key = collectionsViewModel.currentCollectionUUID
                ) {
                    CollectionApiViewModel(collectionsViewModel.currentCollectionUUID)
                }


                MenuBar {
                    Menu("File") {
                        Item(
                            text = "New",
                            onClick = { println("New clicked") },
                            shortcut = KeyShortcut(Key.N, ctrl = true)
                        )
                        Item(
                            text = "Open",
                            onClick = { println("Open clicked") },
                            shortcut = KeyShortcut(Key.O, ctrl = true)
                        )
//                    Item(
//                        text = "Close",
//                        onClick = { showMainWindow = false },
//                        shortcut = KeyShortcut(Key.W, meta = true)
//                    )
                    }
                    Menu("Edit") {
                        Item("Undo", onClick = { println("Undo") })
                        Item("Redo", onClick = { println("Redo") })
                    }
                    Menu("Collections") {
                        Item(
                            text = "New Collection",
                            onClick = {
                                showNewCollectionWizard = true
                            },
                            shortcut = KeyShortcut(Key.N, ctrl = true, shift = true)
                        )
                        Separator()
                        val collections by collectionsViewModel.collectionsFlow.collectAsState()
                        collections.forEachIndexed { index, collection ->
                            RadioButtonItem(
                                text = collection.label,
                                selected = collectionsViewModel.currentCollectionUUID == collection.uuid,
                                onClick = {
                                    collectionsViewModel.currentCollectionUUID = collection.uuid
                                },
                            )
                        }
                    }
                    Menu("Theme") {
                        var selectedTheme by remember { mutableStateOf("System") }
                        RadioButtonItem(
                            "Light",
                            selected = selectedTheme == "Light",
                            onClick = { selectedTheme = "Light" }
                        )
                        RadioButtonItem(
                            "Dark",
                            selected = selectedTheme == "Dark",
                            onClick = { selectedTheme = "Dark" }
                        )
                        RadioButtonItem(
                            "System",
                            selected = selectedTheme == "System",
                            onClick = { selectedTheme = "System" }
                        )
                    }
                    Menu("Preferences") {
                        CheckboxItem("Auto Check Update", true) {}
                    }
                    Menu("About") {
                        Item("Version 0.0.1", onClick = { println("About clicked") })
                        Item("Author", onClick = { println("About clicked") })
                    }
                }

                window.minimumSize = Dimension(800, 600)
                val mainViewModel = viewModel { MainViewModel() }
                val mainUiViewModel = viewModel { MainUiViewModel() }
                val navController = rememberNavController()


                CompositionLocalProvider(
                    LocalMainViewModel provides mainViewModel,
                    LocalMainUiViewModel provides mainUiViewModel,
                    LocalNavController provides navController,
                    LocalCollectionsViewModel provides collectionsViewModel,
                    LocalCollectionApiViewModel provides collectionApiViewModel
                ) {
                    FullSizeRow {
                        ApisListView()
                        DraggableDivideBar(mainUiViewModel.apisListViewWidth) { it ->
                            mainUiViewModel.apisListViewWidth = it
                        }
                        FullSizeColumn {
                            FullWidthRow(
                                modifier = Modifier
                                    .horizontalScroll(rememberScrollState())
                                    .height(30.dp)
                            ) {
                                mainViewModel.openedApiTabs.forEach { apiTab ->
                                    Row(
                                        modifier = Modifier
                                            .width(120.dp)
                                            .height(30.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.Gray)
                                            .clickable {
                                                navController.navigate(Pages.ApiRequestPage(apiTab.key)) {
                                                    popUpTo<Pages.BlankPage>()
                                                }
                                            }
                                            .padding(horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        apiTab.value.Text(
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icons.Default.Close.Icon(
                                            modifier = Modifier.size(20.dp).clickable {
                                                navController.navigate(Pages.BlankPage) {
                                                    popUpTo<Pages.BlankPage>()
                                                }
                                                mainViewModel.closeApiTab(apiTab.key)
                                            }
                                        )
                                    }
                                }
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
            }
        )
    }
}







