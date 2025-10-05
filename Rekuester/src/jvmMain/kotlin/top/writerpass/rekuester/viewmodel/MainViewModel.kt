@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Singletons
import java.io.File

class MainViewModel() : BaseViewModel() {
    private val apiRepository = Singletons.apisRepository

//    val allApiList = mutableStateListOf<Api>()
//    var allApisMap by mutableStateOf<Map<String, Api>>(emptyMap())
//        private set
//
//    init {
//        runInScope {
//            apiRepository.allFlow.collect { apiList ->
//                allApiList.clear()
//                allApiList.addAll(apiList)
//                allApisMap = apiList.associateBy { it.uuid }
//            }
//        }
//    }

//    val openedApiTabsFlow = apiRepository.allFlow.map { it.filter { it.tabOpened } }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = emptyList()
//    )

//    var openedTabApiUUID by mutableStateOf("")
//        private set
//    val openedTabStack = mutableStateListOf<String>()
//
//    fun openApiTab(api: Api) = runInScope {
//        apiRepository.update(api.copy(tabOpened = true))
//        openedTabApiUUID = api.uuid
//        val index = openedTabStack.indexOf(api.uuid)
//        if (index != -1) {
//            openedTabStack.removeAt(index)
//        }
//        openedTabStack.add(api.uuid)
//    }
//
//    fun openApiTabs(apis: List<Api>) = apis
//        .filter { it.tabOpened.not() }
//        .forEach { api ->
//            openApiTab(api)
//        }
//
//    fun closeApiTab(api: Api) = runInScope {
//        apiRepository.update(api.copy(tabOpened = false))
//        openedTabStack.remove(api.uuid)
//        if (api.uuid == openedTabApiUUID) {
//            openedTabApiUUID = openedTabStack.lastOrNull() ?: ""
//        }
//    }

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