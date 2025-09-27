@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import top.writerpass.kmplibrary.coroutine.launchDefault
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.ApiBasicInfo
import top.writerpass.rekuester.Singletons
import java.io.File


class MainViewModel() : ViewModel() {
    private fun runInScope(block: suspend () -> Unit) {
        viewModelScope.launchDefault { block() }
    }

    private val repository = Singletons.apisRepository

    init {
        runInScope {
            repository.initialLoadApis()
        }
    }

    val allFlow = repository.allFlow
    fun findAll() = runInScope { repository.findAll() }
    fun findById(id: String) = runInScope { repository.findById(id) }
    fun deleteApi(index: Int) = runInScope { repository.delete(index) }
    fun deleteApi(id: String) = runInScope { repository.delete(id) }
    fun deleteApi(api: Api) = runInScope { repository.delete(api) }
    fun insertApi(api: Api) = runInScope { repository.insert(api) }
    fun insertApis(vararg apis: Api) = runInScope { repository.inserts(*apis) }
    fun updateApi(api: Api) = runInScope { repository.update(api) }

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
        insertApi(newApi)
    }

    fun saveApis() {
        viewModelScope.launchIO {
            val json = Singletons.json
            json.encodeToStream(
                value = repository.findAll(),
                stream = File("apis.json").outputStream()
            )
        }
    }

    fun loadApis() {
        viewModelScope.launchIO {
            val json = Singletons.json
            val data = json.decodeFromStream<List<Api>>(File("apis.json").inputStream())
            repository.overWriteItems(data)
        }
    }
}