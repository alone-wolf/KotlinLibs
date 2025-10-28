package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import top.writerpass.kmplibrary.coroutine.launchIO
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.ApiHeader
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository

class ApiViewModel(
    private val apiUuid: String,
) : BaseViewModel() {
    private val repository: ApiRepository = Singletons.apisRepository

    var newParamEnabled by mutableStateOf(false)
    var newParamKey by mutableStateOf("")
    var newParamValue by mutableStateOf("")
    var newParamDescription by mutableStateOf("")

    fun clearNewParam() {
        newParamEnabled = false
        newParamKey = ""
        newParamValue = ""
        newParamDescription = ""
    }

    fun saveNewApiParam(): Boolean {
        if (newParamKey == "" && newParamValue == "" && newParamDescription == ""){
            return false
        }
        val new = ApiParam(
            key = newParamKey,
            value = newParamValue,
            description = newParamDescription,
            enabled = newParamEnabled
        )
        apiStateFlow.value.params.add(new)
        return true
    }

    fun deleteApiParam(index: Int) {
        apiStateFlow.value.params.removeAt(index)
    }

    fun updateApiParam(index: Int, new: ApiParam) {
        val apiState = apiStateFlow.value
        apiState.params[index] = new
    }


    var newHeaderEnabled by mutableStateOf(false)
    var newHeaderKey by mutableStateOf("")
    var newHeaderValue by mutableStateOf("")
    var newHeaderDescription by mutableStateOf("")
    fun clearNewHeader() {
        newHeaderEnabled = false
        newHeaderKey = ""
        newHeaderValue = ""
        newHeaderDescription = ""
    }
    fun saveNewApiHeader(): Boolean {
        if (newHeaderKey == "" && newHeaderValue == "" && newHeaderDescription == ""){
            return false
        }
        val new = ApiHeader(
            key = newHeaderKey,
            value = newHeaderValue,
            description = newHeaderDescription,
            enabled = newHeaderEnabled
        )
        apiStateFlow.value.headers.add(new)
        return true
    }
    fun deleteApiHeader(index: Int) {
        apiStateFlow.value.headers.removeAt(index)
    }

    fun updateApiHeader(index: Int, new: ApiHeader) {
        val apiState = apiStateFlow.value
        apiState.headers[index] = new
    }


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

    fun updateOrInsertApi(api: Api) {
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