package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.ApiState
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository

class ApiRequestViewModel(
    uuid: String,
    private val repository: ApiRepository = Singletons.apiRepository
) : BaseViewModel() {

    companion object {
        @Composable
        fun viewModelInstance(apiUUID: String): ApiRequestViewModel {
            val viewModelStoreOwner = LocalAppViewModelStoreOwner.current

            return viewModel(
                viewModelStoreOwner = viewModelStoreOwner,
                key = apiUUID,
                initializer = {
                    ApiRequestViewModel(apiUUID)
                }
            )
        }
    }

    private val client = Singletons.client

//    val api = repository.findByIdFlow(uuid)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = null
//        )

    var apiNullable by mutableStateOf<Api?>(null)
    var apiStateNullable by mutableStateOf<ApiState?>(null)
        private set

    init {
        runInScope {
            repository.findByIdFlow(uuid).collect { item ->
                apiNullable = item
                apiStateNullable = item?.let { ApiStateHolder.getApiState(it) }
            }
        }
    }

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
            apiNullable?.let { api ->
                apiStateNullable?.requestResult = client.request(
                    method = api.method,
                    address = api.address,
                    params = api.params,
                    headers = api.headers,
                    body = api.requestBody
                )
            }
        }
    }
}