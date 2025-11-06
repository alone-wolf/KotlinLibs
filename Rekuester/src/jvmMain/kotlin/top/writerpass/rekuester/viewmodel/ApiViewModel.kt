package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import top.writerpass.kmplibrary.coroutine.launchMain
import top.writerpass.kmplibrary.coroutine.withContextIO
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.HttpRequestResult
import top.writerpass.rekuester.LocalAppViewModelStoreOwner
import top.writerpass.rekuester.RekuesterClient
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.models.Api
import top.writerpass.rekuester.models.ApiHeader
import top.writerpass.rekuester.models.ApiParam
import top.writerpass.rekuester.models.ApiStateAuthContainer
import top.writerpass.rekuester.models.ApiStateBodyContainer
import top.writerpass.rekuester.models.AuthTypes
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes

class ApiViewModel(val apiUuid: String) : BaseViewModel() {
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

    val ui = Singletons.apisRepository
        .allFlow.map { it.find { it.uuid == apiUuid } ?: Api.BLANK }
        .map { ApiStateHolder(it) }
        .stateIn(ApiStateHolder.BLANK)

    fun updateOrInsertApi(api: Api) {
        runInScope {
            Singletons.apisRepository.updateOrInsert(api)
        }
    }

    fun request() {
        viewModelScope.launchMain {
            val client = Singletons.client
            val newRequestResult = withContextIO { request(client) }
            ui.value.updateRequestResult(newRequestResult)
        }
    }

    suspend fun request(client: RekuesterClient): HttpRequestResult {
        return client.request(
            method = ui.value.method,
            address = ui.value.address,
            params = ui.value.params.asReadOnly(),
            headers = ui.value.headers.asReadOnly(),
            auth = ui.value.auth,
            body = ui.value.body
        )
    }

    // info part
    val infoPart = InfoPart()

    inner class InfoPart() {
        fun updateLabel(label: String) = ui.value.updateLabel(label)
        fun updateMethod(method: HttpMethod) = ui.value.updateMethod(method)
        fun updateAddress(address: String) = ui.value.updateAddress(address)
    }

    // param part
    val paramPart = ParamPart()

    inner class ParamPart {
        var enabled by mutableStateOf(false)
        var key by mutableStateOf("")
        var value by mutableStateOf("")
        var description by mutableStateOf("")

        fun clearNew() {
            enabled = false
            key = ""
            value = ""
            description = ""
        }

        fun save(): Boolean {
            if (key == "" && value == "" && description == "") {
                return false
            }
            val new = ApiParam(
                key = key,
                value = value,
                description = description,
                enabled = enabled
            )
            ui.value.params.add(new)
            return true
        }

        fun deleteById(index: Int) {
            ui.value.params.removeAt(index)
        }

        fun updateById(index: Int, new: ApiParam) {
            ui.value.params[index] = new
        }
    }

    // header part
    val headerPart = HeaderPart()

    inner class HeaderPart {
        var enabled by mutableStateOf(false)
        var key by mutableStateOf("")
        var value by mutableStateOf("")
        var description by mutableStateOf("")
        fun clearNew() {
            enabled = false
            key = ""
            value = ""
            description = ""
        }

        fun save(): Boolean {
            if (key == "" && value == "" && description == "") {
                return false
            }
            val new = ApiHeader(
                key = key,
                value = value,
                description = description,
                enabled = enabled
            )
            ui.value.headers.add(new)
            return true
        }

        fun deleteById(index: Int) {
            ui.value.headers.removeAt(index)
        }

        fun updateById(index: Int, new: ApiHeader) {
            ui.value.headers[index] = new
        }
    }

    // auth part
    val authPart = AuthPart()

    inner class AuthPart {
        fun updateType(newType: AuthTypes) {
            val newAuth = ui.value.auth.copy(type = newType)
            ui.value.updateAuth(newAuth)
        }

        fun updateBasic(basic: ApiStateAuthContainer.Basic) {
            val newAuth = ui.value.auth.copy(basic = basic)
            ui.value.updateAuth(newAuth)
        }

        fun updateBearer(bearer: ApiStateAuthContainer.Bearer) {
            val newAuth = ui.value.auth.copy(bearer = bearer)
            ui.value.updateAuth(newAuth)
        }

        fun updateJwt(jwt: ApiStateAuthContainer.Jwt) {
            val newAuth = ui.value.auth.copy(jwt = jwt)
            ui.value.updateAuth(newAuth)
        }

        fun updateApiKey(apiKey: ApiStateAuthContainer.ApiKey){
            val newAuth = ui.value.auth.copy(apiKey = apiKey)
            ui.value.updateAuth(newAuth)
        }
    }

    // body part
    val bodyPart = BodyPart()

    inner class BodyPart {
        val rawTypeCalculator = { ui.value.body.raw?.type ?: RawBodyTypes.Text }
        val rawContentCalculator = { ui.value.body.raw?.content ?: "" }
        fun updateType(newType: BodyTypes) {
            val newBody = ui.value.body.copy(type = newType)
            ui.value.updateBody(newBody)
        }

        fun updateRawType(newType: RawBodyTypes) {
            val body = ui.value.body
            val raw = body.raw
            val newRaw = raw?.copy(type = newType) ?: ApiStateBodyContainer.Raw(newType, "")
            val newBody = body.copy(raw = newRaw)
            ui.value.updateBody(newBody)
        }

        fun updateRawContent(newContent: String) {
            val body = ui.value.body
            val raw = body.raw
            val rawType = raw?.type ?: RawBodyTypes.Text
            val newRaw = raw?.copy(content = newContent) ?: ApiStateBodyContainer.Raw(
                type = rawType,
                content = newContent
            )
            val newBody = body.copy(raw = newRaw)
            ui.value.updateBody(newBody)
        }

        fun updateBinaryBody(binary: ApiStateBodyContainer.Binary) {
            val body = ui.value.body
            val newBody = body.copy(binary = binary)
            ui.value.updateBody(newBody)
        }
    }
}