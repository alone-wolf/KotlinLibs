package top.writerpass.rekuester.viewmodel

import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.Singletons

class CollectionApiViewModel(
    private val connectionUUID: String
) : BaseViewModel() {
    val itemFlow = Singletons.collectionsRepository.allFlow
        .map { it.find { it.uuid == connectionUUID } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val apiRepository = Singletons.apiRepository
    val allFlow = apiRepository.allFlow
    suspend fun findAll() = apiRepository.findAll()

    val apis = allFlow
        .map { it.filter { it.collectionUUID == connectionUUID } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )

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
            params = emptyList(),
            headers = emptyList(),
            requestBody = null
        )
        insertApi(newApi)
    }
}