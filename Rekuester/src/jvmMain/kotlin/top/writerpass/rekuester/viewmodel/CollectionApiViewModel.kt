package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.Singletons

abstract class BaseViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    fun runInScope(block: suspend () -> Unit) {
        viewModelScope.launch(defaultDispatcher) { block() }
    }
}

class CollectionApiViewModel(
    val connectionUUID: String
) : BaseViewModel() {
    val itemFlow = Singletons.collectionsRepository.allFlow
        .map { it.find { it.uuid == connectionUUID } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val apis = mutableStateListOf<Api>()

    init {
        runInScope {
            Singletons.apiRepository.allFlow
                .map { it.filter { it.collectionUUID == connectionUUID } }
                .collect {
                    apis.clear()
                    apis.addAll(it)
                }
        }
    }

    private val apiRepository = Singletons.apiRepository
    val allFlow = apiRepository.allFlow
    suspend fun findAll() = apiRepository.findAll()

    fun deleteApi(index: Int) = runInScope { apiRepository.delete(index) }
    fun deleteApi(id: String) = runInScope { apiRepository.delete(id) }
    fun deleteApi(api: Api) = runInScope { apiRepository.delete(api) }
    fun insertApi(api: Api) = runInScope { apiRepository.insert(api) }
    fun insertApis(vararg apis: Api) = runInScope { apiRepository.inserts(*apis) }
    fun updateApi(api: Api) = runInScope { apiRepository.update(api) }

    fun createNewApi() {
        val newApi = Api(
            collectionUUID = connectionUUID,
            label = "untitled",
            method = HttpMethod.Get,
            address = "http://",
            params = emptyMap(),
            headers = emptyMap(),
            requestBody = null
        )
        insertApi(newApi)
    }

}