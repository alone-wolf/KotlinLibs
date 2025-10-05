package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository

class ApiViewModel(
    apiUuid: String,
) : BaseViewModel() {
    private val repository: ApiRepository = Singletons.apisRepository


    companion object Companion {
        @Composable
        fun instance(apiUuid: String): ApiViewModel {
            val viewModelStoreOwner = LocalAppViewModelStoreOwner.current

            return viewModel(
                viewModelStoreOwner = viewModelStoreOwner,
                key = apiUuid,
                initializer = { ApiViewModel(apiUuid) }
            )
        }
    }

    private val client = Singletons.client

    val apiFlow = Singletons.apisRepository
        .allFlow.map { it.find { it.uuid == apiUuid } ?: Api.BLANK }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Api.BLANK
        )
    val apiStateFlow = apiFlow.map { ApiStateHolder.getApiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ApiStateHolder.BLANK
        )

//    val apiNullableFlow = repository.allFlow
//        .map { it.find { it.uuid == uuid } }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = null
//        )

//    val apiStateNullableFlow = apiNullableFlow
//        .map { it?.let { ApiStateHolder.getApiState(it) } }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = null
//        )

//    val api = repository.findByIdFlow(uuid)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = null
//        )

//    var apiNullable by mutableStateOf<Api?>(null)
//    var apiStateNullable by mutableStateOf<ApiState?>(null)
//        private set
//
//    init {
//        runInScope {
//            repository.findByIdFlow(uuid).collect { item ->
//                apiNullable = item
//                apiStateNullable = item?.let { ApiStateHolder.getApiState(it) }
//            }
//        }
//    }

//    fun runUpdateApi(action: Api.() -> Api) {
//        runInScope {
//            api.value?.let { it ->
//                val newApi = it.action()
//                repository.update(newApi)
//            }
//        }
//    }

//    fun update(api: Api) {
//        runInScope {
//            repository.update(api)
//        }
//    }

    fun updateOrInsert(api: Api) {
        runInScope {
            repository.updateOrInsert(api)
        }
    }

    fun request() {
        viewModelScope.launchIO {
            apiStateFlow.value.request(client)
        }
    }
}