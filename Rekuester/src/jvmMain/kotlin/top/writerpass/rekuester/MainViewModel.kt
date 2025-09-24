@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import top.writerpass.kmplibrary.coroutine.launchIO
import java.io.File
import kotlin.coroutines.CoroutineContext

class MainUiViewModel : ViewModel() {
    var leftListWidth by mutableStateOf(200.dp)
}


class Debouncer(
    private val scope: CoroutineScope,
    private val waitMs: Long = 5000L,
    private val context: CoroutineContext = Dispatchers.Default,
    private val action: () -> Unit = {}
) {
    private var job: Job? = null

    fun submit() {
        job?.cancel() // 取消上一次
        job = scope.launch(context) {
            delay(waitMs)
            action()
        }
    }

    /**
     * 调用此方法触发防抖
     * @param action 延迟结束后执行的动作
     */
    fun submit(action: () -> Unit) {
        job?.cancel() // 取消上一次
        job = scope.launch(context) {
            delay(waitMs)
            action()
        }
    }

    /** 可选：取消当前任务 */
    fun cancel() {
        job?.cancel()
    }
}

fun ViewModel.debounce(
    waitMs: Long = 5000L,
    context: CoroutineContext = Dispatchers.IO,
    action: () -> Unit
) = Debouncer(
    scope = viewModelScope,
    waitMs = waitMs,
    context = context,
    action = action
)

class MainViewModel() : ViewModel() {

    private val debouncer = debounce { saveApis() }

    fun touchDataSaving() {
        debouncer.submit()
    }

    val apis = mutableStateListOf(
        Api(
            basicInfo = ApiBasicInfo(
                label = "GET All Users",
                method = HttpMethod.Companion.Get,
                address = "http://localhost:8080/api/users"
            ), params = emptyMap(), headers = emptyMap(), requestBody = null
        ),
        Api(
            basicInfo = ApiBasicInfo(
                label = "GET User by Id",
                method = HttpMethod.Companion.Get,
                address = "http://localhost:8080/api/users/100"
            ), params = emptyMap(), headers = emptyMap(), requestBody = null
        ),
        Api(
            basicInfo = ApiBasicInfo(
                label = "Delete User by Id",
                method = HttpMethod.Companion.Delete,
                address = "http://localhost:8080/api/users/1"
            ), params = emptyMap(), headers = emptyMap(), requestBody = null
        ),
        Api(
            basicInfo = ApiBasicInfo(
                label = "Delete All Users",
                method = HttpMethod.Companion.Delete,
                address = "http://localhost:8080/api/users"
            ), params = emptyMap(), headers = emptyMap(), requestBody = null
        ),
        Api(
            basicInfo = ApiBasicInfo(
                label = "Inject 100 Users",
                method = HttpMethod.Companion.Post,
                address = "http://localhost:8080/api/users/inject"
            ), params = emptyMap(), headers = emptyMap(), requestBody = null
        ),
        Api(
            basicInfo = ApiBasicInfo(
                label = "Baidu",
                method = HttpMethod.Companion.Get,
                address = "https://baidu.com"
            ), params = emptyMap(), headers = emptyMap(), requestBody = null
        ),
    )
    var currentApi by mutableStateOf<Api?>(apis.first())
        private set

    fun updateCurrentApi(api: Api) {
        currentApi = api
    }

    fun updateApi(api: Api) {
        val uuid = api.uuid
        val index = apis.indexOfFirst { it.uuid == uuid }
        apis[index] = api
        updateCurrentApi(api)
    }

    fun deleteApi(api: Api) {
        apis.remove(api)
    }

    fun createNewApi() {
        val newApi = Api(
            basicInfo = ApiBasicInfo(
                label = "untitled",
                method = HttpMethod.Get,
                address = "http://"
            ),
            params = emptyMap(),
            headers = emptyMap(),
            requestBody = null
        )
        apis.add(newApi)
        updateCurrentApi(newApi)
    }

    fun saveApis() {
        viewModelScope.launchIO {
            val json = Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
            json.encodeToStream(apis.toList(), File("apis.json").outputStream())
        }
    }

    fun loadApis() {
        viewModelScope.launchIO {
            val json = Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
            val data = json.decodeFromStream<List<Api>>(File("apis.json").inputStream())
            apis.clear()
            apis += data
        }
    }
}