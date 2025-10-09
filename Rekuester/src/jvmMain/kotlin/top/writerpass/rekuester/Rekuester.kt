package top.writerpass.rekuester

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.rekuester.data.dao.ItemWithId
import top.writerpass.rekuester.tables.v6.Table6Wrapper1
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
    val description: String = ""
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

fun main1() {
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

fun main() = singleWindowApplication {
    Table6Wrapper1()
}


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    VerticalDivider()
    Text(
        text = text,
        modifier = Modifier
//            .border(width = 1.dp, color =  Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}


//@Composable
//fun LazyItemScope.TabRow(
//    rowState: RowState
//) {
//    HorizontalDivider()
//    Row(modifier = Modifier.background(Color.Gray).height(rowState.height.dp)) {
//        TableCell({})
//        TableCell({})
//        VerticalDivider()
//    }
//}

//object CellStyleHolder{
//    private val rowStateMap = mutableMapOf<Int, RowState>()
//    private val columnStateMap = mutableMapOf<Int, ColumnState>()
//
//    fun getState(x:Int,y:Int): CellState{
//
//    }
//}


//


//@Composable
//fun RowScope.TableCell(
//    content: @Composable RowScope.() -> Unit
//) {
//    VerticalDivider()
//    content()
//}


//@Composable
//fun TableScreen() {
//    // Just a fake data... a Pair of Int and String
//    val tableData = (1..100).mapIndexed { index, item ->
//        index to "Item $index"
//    }
//    // Each cell of a column must have the same weight.
//    val column1Weight = .3f // 30%
//    val column2Weight = .7f // 70%
//    // The LazyColumn will be our table. Notice the use of the weights below
//    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
//        // Here is the header
//        item {
//            HorizontalDivider()
//            Row(modifier = Modifier.background(Color.Gray).height(IntrinsicSize.Min)) {
//                TableCell(text = "Column 1", weight = column1Weight)
//                TableCell(text = "Column 2", weight = column2Weight)
//                VerticalDivider()
//            }
//        }
//        // Here are all the lines of your table.
//        items(tableData) { (id, text) ->
//            HorizontalDivider()
//            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
//                TableCell(text = id.toString(), weight = column1Weight)
//                TableCell(text = text, weight = column2Weight)
//                VerticalDivider()
//            }
//        }
//        item {
//            HorizontalDivider()
//        }
//    }
//}







