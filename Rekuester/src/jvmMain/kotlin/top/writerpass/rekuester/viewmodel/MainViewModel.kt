@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.Singletons
import java.io.File

data class ApiTab(
    val uuid: String,
    val label: String
)

class MainViewModel() : BaseViewModel() {
    private val apiRepository = Singletons.apiRepository

    //    val openedApis = mutableStateListOf<Api>()
    val openedApiTabs = mutableStateMapOf<String, String>()

    fun openApiTab(api: Api) {
        openedApiTabs[api.uuid] = api.label
    }

    fun closeApiTab(uuid: String) {
        openedApiTabs.remove(uuid)
    }

    fun saveData() {
        viewModelScope.launchIO {
            val json = Singletons.json
            json.encodeToStream(
                value = apiRepository.findAll(),
                stream = File("apis.json").outputStream()
            )
            json.encodeToStream(
                value = Singletons.collectionsRepository.findAll(),
                stream = File("collections.json").outputStream()
            )
        }
    }

    fun loadData() {
//        viewModelScope.launchIO {
//            val json = Singletons.json
//            val data = json.decodeFromStream<List<Api>>(File("apis.json").inputStream())
//            apiRepository.overWriteItems(data)
//        }
    }
}