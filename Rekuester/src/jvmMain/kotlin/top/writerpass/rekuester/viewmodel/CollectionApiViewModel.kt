package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import top.writerpass.kmplibrary.utils.onUpdate
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.Pages
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.models.Api
import top.writerpass.rekuester.models.Collection

class CollectionApiViewModel(private val collectionUuid: String) : BaseViewModel() {

    companion object {
        @Composable
        fun instance(collectionUuid: String): CollectionApiViewModel {
            val viewModelStoreOwner = LocalAppViewModelStoreOwner.current
            return viewModel(
                viewModelStoreOwner = viewModelStoreOwner,
                key = collectionUuid
            ) {
                CollectionApiViewModel(collectionUuid)
            }
        }
    }

    val apisRepository = Singletons.apisRepository
    val collectionsRepository = Singletons.collectionsRepository


    val collectionFlow = collectionsRepository.allFlow
        .map { it.find { it.uuid == collectionUuid } ?: Collection.BLANK }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Collection.BLANK,
        )

    val apisListFlow = apisRepository.allFlow
        .map { it.filter { it.collectionUUID == collectionUuid } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )

    private val _apisMapFlow = apisListFlow.map { it.associateBy { it.uuid } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyMap(),
        )

    fun reorderApiTabs(fromIndex: Int, toIndex: Int) {
        _openedApiTabsFlow.onUpdate {
            add(toIndex, removeAt(fromIndex))
        }
    }

    private val _openedApiTabsFlow = MutableStateFlow(emptyList<Api>())
    val openedApiTabsFlow = _openedApiTabsFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _apisMapFlow.collect { apisMap ->
                _openedApiTabsFlow.update { list ->
                    mutableListOf<Api>().apply {
                        list.forEach { api ->
                            apisMap[api.uuid]?.let { add(it) }
                            if (isEmpty()) {
                                currentApiTabUuid = "--"
                                _currentPage.value = Pages.BlankPage
                            }
                        }
                    }
                }
            }
        }
    }

    var currentApiTabUuid by mutableStateOf("--")
        private set
    private val _currentPage = MutableStateFlow<Pages>(Pages.BlankPage)
    val currentPageFlow = _currentPage.asStateFlow()

    fun openApiTab(api: Api) {
        _openedApiTabsFlow.onUpdate {
            if (find { it.uuid == api.uuid } == null) {
                add(api)
            }
        }
        _currentPage.value = Pages.ApiRequestPage(api.uuid)
        currentApiTabUuid = api.uuid
    }

    fun openApiTabs(apis: List<Api>) {
        if (apis.isEmpty()) return
        _openedApiTabsFlow.onUpdate {
            apis.forEach { api ->
                if (find { it.uuid == api.uuid } == null) {
                    add(api)
                }
            }
        }
        val lastApi = apis.last()
        _currentPage.value = Pages.ApiRequestPage(lastApi.uuid)
        currentApiTabUuid = lastApi.uuid
    }

    fun closeApiTab(api: Api) {
        _openedApiTabsFlow.onUpdate {
            val removed = removeIf { it.uuid == api.uuid }
            if (isEmpty()) {
                currentApiTabUuid = "--"
                _currentPage.value = Pages.BlankPage
            }
        }
    }


    fun deleteApi(index: Int) = runInScope { apisRepository.delete(index) }
    fun deleteApi(id: String) = runInScope { apisRepository.delete(id) }
    fun deleteApi(api: Api) = runInScope { apisRepository.delete(api) }

    fun insertApi(api: Api) = runInScope { apisRepository.insert(api) }
    fun insertApis(vararg apis: Api) = runInScope { apisRepository.inserts(*apis) }
    fun updateApi(api: Api) = runInScope { apisRepository.update(api) }

    fun createNewApi() {
        val newApi = Api(
            collectionUUID = collectionUuid,
            label = "untitled",
            method = HttpMethod.Get,
            address = "http://",
            params = emptyList(),
            headers = emptyList(),
        )
        insertApi(newApi)
    }
}