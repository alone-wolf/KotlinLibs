@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.Singletons
import java.io.File

class MainViewModel() : BaseViewModel() {
    private val apiRepository = Singletons.apiRepository

    val openedApiTabsFlow = apiRepository.allFlow.map { it.filter { it.tabOpened } }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun openApiTab(api: Api) = runInScope {
        apiRepository.update(api.copy(tabOpened = true))
    }

    fun closeApiTab(api: Api) = runInScope {
        apiRepository.update(api.copy(tabOpened = false))
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