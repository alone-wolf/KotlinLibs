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
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.RekuesterClient
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository
import top.writerpass.rekuester.models.Api
import top.writerpass.rekuester.models.ApiHeader
import top.writerpass.rekuester.models.ApiParam
import top.writerpass.rekuester.models.ApiStateAuthContainer
import top.writerpass.rekuester.models.ApiStateBodyContainer
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes

class ApiViewModel(val apiUuid: String) : BaseViewModel() {
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
        if (newParamKey == "" && newParamValue == "" && newParamDescription == "") {
            return false
        }
        val new = ApiParam(
            key = newParamKey,
            value = newParamValue,
            description = newParamDescription,
            enabled = newParamEnabled
        )
        ui.value.params.add(new)
        return true
    }

    fun deleteApiParam(index: Int) {
        ui.value.params.removeAt(index)
    }

    fun updateApiParam(index: Int, new: ApiParam) {
        ui.value.params[index] = new
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
        if (newHeaderKey == "" && newHeaderValue == "" && newHeaderDescription == "") {
            return false
        }
        val new = ApiHeader(
            key = newHeaderKey,
            value = newHeaderValue,
            description = newHeaderDescription,
            enabled = newHeaderEnabled
        )
        ui.value.headers.add(new)
        return true
    }

    fun deleteApiHeader(index: Int) {
        ui.value.headers.removeAt(index)
    }

    fun updateApiHeader(index: Int, new: ApiHeader) {
        ui.value.headers[index] = new
    }

    fun updateAuth(block: ApiStateAuthContainer.() -> ApiStateAuthContainer) {
        val new = block(ui.value.auth)
        ui.value.auth = new
    }

    // TODO 把上面这些移动到 state 中


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

    val ui = apiFlow.map { ApiStateHolder(it) }
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
            request(client)
        }
    }

    suspend fun request(client: RekuesterClient) {
        ui.value.requestResult = client.request(
            method = ui.value.method,
            address = ui.value.address,
            params = ui.value.params.asReadOnly(),
            headers = ui.value.headers.asReadOnly(),
            auth = ui.value.auth,
            body = ui.value.body
        )
    }

    val bodyPart = BodyPart()

    inner class BodyPart{
        val rawTypeCalculator = { ui.value.body.raw?.type ?: RawBodyTypes.Text }
        val rawContentCalculator = { ui.value.body.raw?.content ?: "" }
        fun updateType(newType: BodyTypes){
            ui.value.body = ui.value.body.copy(type = newType)
        }
        fun updateRawType(newType: RawBodyTypes){
            val body = ui.value.body
            val raw = body.raw
            val newRaw = raw?.copy(type = newType) ?: ApiStateBodyContainer.Raw(newType,"")
            val newBody = body.copy(raw = newRaw)
            ui.value.body = newBody
        }
        fun updateRawContent(newContent: String){
            val body = ui.value.body
            val raw = body.raw
            val rawType = raw?.type ?: RawBodyTypes.Text
            val newRaw = raw?.copy(content = newContent) ?: ApiStateBodyContainer.Raw(
                type = rawType,
                content = newContent
            )
            val newBody = body.copy(raw = newRaw)
            ui.value.body = newBody
        }
    }
}