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
import kotlinx.coroutines.launch
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.Collection
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.Pages
import top.writerpass.rekuester.Singletons

class CollectionApiViewModel(
    private val collectionUuid: String
) : BaseViewModel() {

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

    val apisMapFlow = apisListFlow.map { it.associateBy { it.uuid } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyMap(),
        )

    private val _openedApiTabs = mutableListOf<Api>()
    private val _openedApiTabsFlow = MutableStateFlow(emptyList<Api>())
    val openedApiTabsFlow = _openedApiTabsFlow.asStateFlow()

    init {
        // 当 apisMapFlow 更新时，同步更新 openedApiTabsFlow 中的 label、method 等显示内容
        viewModelScope.launch {
            apisMapFlow.collect { apisMap ->
                // 清除已不存在的 tab（例如某 API 被删除）
                val validTabs = _openedApiTabs.filter { it.uuid in apisMap.keys }
                if (validTabs.size != _openedApiTabs.size) {
                    _openedApiTabs.clear()
                    _openedApiTabs.addAll(validTabs)
                    _openedApiTabsFlow.value = validTabs.toList()
                } else {
                    // 无结构变化则不触发更新（可选优化）
                    // 如果你希望即使 Api 数据更新时 Tab 内容也立即刷新，可直接：
                    _openedApiTabsFlow.value = validTabs.toList()
                }
            }
        }
    }

    var currentApiTabUuid by mutableStateOf("--")
        private set
    private val _currentPage = MutableStateFlow<Pages>(Pages.BlankPage)
    val currentPageFlow = _currentPage.asStateFlow()

    fun openApiTab(api: Api) {
        if (_openedApiTabs.find { it.uuid == api.uuid } == null) {
            _openedApiTabs.add(api)
            _openedApiTabsFlow.value = _openedApiTabs.toList()
        }
        _currentPage.value = Pages.ApiRequestPage(api.uuid)
        currentApiTabUuid = api.uuid
    }

    fun closeApiTab(api: Api) {
        val removed = _openedApiTabs.removeIf { it.uuid == api.uuid }
        if (removed) {
            _openedApiTabsFlow.value = _openedApiTabs.toList()
        }
        if (_openedApiTabs.isEmpty()) {
            _currentPage.value = Pages.BlankPage
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
            requestBody = null
        )
        insertApi(newApi)
    }
}