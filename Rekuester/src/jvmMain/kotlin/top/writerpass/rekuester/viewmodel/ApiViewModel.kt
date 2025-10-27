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
import top.writerpass.kmplibrary.utils.addressString
import top.writerpass.kmplibrary.utils.println
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository

class ApiViewModel(
    private val apiUuid: String,
) : BaseViewModel() {
    private val repository: ApiRepository = Singletons.apisRepository

    var e by mutableStateOf(false)
    var k by mutableStateOf("")
    var v by mutableStateOf("")
    var d by mutableStateOf("")

    fun clearEKVD() {
        e = false
        k = ""
        v = ""
        d = ""
    }

    fun createApiParam() {
        val new = ApiParam(
            key = k,
            value = v,
            description = d,
            enabled = e
        )
        apiStateFlow.value.params.add(new)
    }

    fun deleteApiParam(index: Int) {
        apiStateFlow.value.params.removeAt(index)
    }

    fun updateApiParam(index: Int, new: ApiParam) {
        val apiState = apiStateFlow.value
        apiState.params[index] = new
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