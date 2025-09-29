package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import top.writerpass.kmplibrary.coroutine.launchDefault
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.HttpRequestResult
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository

class ApiRequestViewModel(
    uuid: String,
    private val repository: ApiRepository = Singletons.apiRepository
) : BaseViewModel() {
    private val client = Singletons.client

    val api: StateFlow<Api?> = repository.findByIdFlow(uuid)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun runUpdateApi(action: Api.() -> Api) {
        runInScope {
            api.value?.let { it ->
                val newApi = it.action()
                repository.update(newApi)
            }
        }
    }

    fun update(api: Api) {
        runInScope {
            repository.update(api)
        }
    }

    fun updateOrInsert(api: Api) {
        runInScope {
            repository.updateOrInsert(api)
        }
    }


    var currentResult by mutableStateOf<HttpRequestResult?>(null)
        private set

    fun request() {
        viewModelScope.launchIO {
            api.value?.let { api ->
                currentResult = client.request(
                    method = api.method,
                    address = api.address,
                    params = api.params,
                    headers = api.headers,
                    body = api.requestBody
                )
            }
        }
    }

    fun composeNewApi(): Api? {
        return api.value?.let { api ->
            Api(
                uuid = api.uuid,
                label = api.label,
                method = api.method,
                address = api.address,
                params = api.params.toMap(),
                headers = api.headers.toMap(),
                requestBody = api.requestBody,
            )
        }
    }

//    fun request(api: Api) {
//        viewModelScope.launchIO {
//            currentResult = client.request(api)
//        }
//    }
}