@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.ApiBasicInfo
import top.writerpass.rekuester.Singletons
import java.io.File


interface BaseDataDao<ID, ITEM> {
    suspend fun findById(id: ID): ITEM?
    suspend fun findAll(): List<ITEM>
    val allFlow: Flow<List<ITEM>>
    suspend fun delete(id: ID)
    suspend fun delete(item: ITEM)
    suspend fun insert(item: ITEM)
    suspend fun update(item: ITEM)
    suspend fun update(id: ID, item: ITEM)
}

class ApiDao(
    private val _apis: MutableList<Api> = mutableListOf(),
    private val _apisFlow: MutableStateFlow<List<Api>> = MutableStateFlow(emptyList())
) : BaseDataDao<String, Api> {
    override suspend fun findById(id: String): Api? {
        return _apis.find { it.uuid == id }
    }

    override suspend fun findAll(): List<Api> {
        return _apis.toList()
    }

    override val allFlow: Flow<List<Api>>
        get() = _apisFlow.asStateFlow()

    suspend fun delete(index: Int) {
        _apis.removeAt(index)
    }

    override suspend fun delete(id: String) {
        val index = _apis.indexOfFirst { it.uuid == id }
        delete(index)
    }

    override suspend fun delete(item: Api) {
        delete(item.uuid)
    }

    override suspend fun insert(item: Api) {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: Api) {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, item: Api) {
        TODO("Not yet implemented")
    }

}

class ApiRepository() {

    private val _apisFlow = MutableStateFlow(emptyList<Api>())
    val apisFlow = _apisFlow.asStateFlow()

    suspend fun initialLoadApis() {
        _apisFlow.emit(_apis)
    }

    private val _apis = mutableListOf(
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

    private suspend fun autoLoadApis(block: suspend () -> Unit) {
        block()
        _apisFlow.emit(_apis)
    }

    suspend fun deleteByIndex(index: Int) = autoLoadApis {
        _apis.removeAt(index)
    }

    suspend fun deleteById(uuid: String) = autoLoadApis {
        val index = _apis.indexOfFirst { it.uuid == uuid }
        _apis.removeAt(index)
    }

    suspend fun delete(api: Api) = autoLoadApis {
        val index = _apis.indexOfFirst { it.uuid == api.uuid }
        _apis.removeAt(index)
    }

    suspend fun update(api: Api) {
        val index = _apis.indexOfFirst { it.uuid == api.uuid }
        _apis[index] = api
        _apisFlow.emit(_apis)
    }

    fun findById(uuid: String): Api? {
        return _apis.find { it.uuid == uuid }
    }
}

class MainViewModel() : ViewModel() {
    val apis = Singletons.apiRepository._apis

//    private val debouncer = debounce { saveApis() }
//
//    fun touchDataSaving() {
//        debouncer.submit()
//    }


//    var currentApi by mutableStateOf<Api?>(apis.first())
//        private set
//
//    fun updateCurrentApi(api: Api) {
//        currentApi = api
//    }

    fun updateApi(api: Api) {
        val uuid = api.uuid
        val index = apis.indexOfFirst { it.uuid == uuid }
        apis[index] = api
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