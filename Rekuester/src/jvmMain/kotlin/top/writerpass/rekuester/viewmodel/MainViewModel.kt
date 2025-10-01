@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var openedTabApiUUID by mutableStateOf("")
        private set
    val openedTabStack = mutableStateListOf<String>()

    fun openApiTab(api: Api) = runInScope {
        apiRepository.update(api.copy(tabOpened = true))
        openedTabApiUUID = api.uuid
        val index = openedTabStack.indexOf(api.uuid)
        if (index != -1) {
            openedTabStack.removeAt(index)
        }
        openedTabStack.add(api.uuid)
    }

    fun closeApiTab(api: Api) = runInScope {
        apiRepository.update(api.copy(tabOpened = false))
        openedTabStack.remove(api.uuid)
        if (api.uuid == openedTabApiUUID) {
            openedTabApiUUID = openedTabStack.lastOrNull() ?: ""
        }
    }

    fun saveData() {
        viewModelScope.launchIO {
            val json = Singletons.json
            json.encodeToStream(
                value = apiRepository.findAll(), stream = File("apis.json").outputStream()
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