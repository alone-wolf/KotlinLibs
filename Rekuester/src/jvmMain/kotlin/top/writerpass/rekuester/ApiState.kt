package top.writerpass.rekuester

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.StateFactoryMarker
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.utils.AutoActionMutableState
import top.writerpass.rekuester.utils.AutoActionMutableStateList
import top.writerpass.rekuester.utils.autoActionStateListOf
import top.writerpass.rekuester.utils.autoActionStateOf
import java.util.UUID

val HttpHeaders.RekuesterToken: String
    get() = "Rekuester-Token"

fun defaultRequestHeaders(): Map<String, String> {
    return mapOf(
        HttpHeaders.UserAgent to "Rekuester/1.0.0",
        HttpHeaders.Accept to "*/*",
        HttpHeaders.AcceptEncoding to "gzip, deflate, br",
        HttpHeaders.Connection to "keep-alive",
        HttpHeaders.RekuesterToken to UUID.randomUUID().toString()
    )
}

class ApiState(
    api: Api,
    val uuid: String = api.uuid,
) {
    val collectionUUID = api.collectionUUID
    val isModified = mutableStateOf(false)

    val label = autoTagModifiedStateOf(api.label)
    val method = autoTagModifiedStateOf(api.method)
    val address = autoTagModifiedStateOf(api.address)
    val params = autoTagModifiedStateListOf(api.params)
    val headers = autoTagModifiedStateListOf(api.headers)
    val requestBody = autoTagModifiedStateOf(api.requestBody)
    var requestResult by mutableStateOf<HttpRequestResult?>(null)
        private set

    val urlBinding = UrlParamsBinding(
        address = address,
        params = params
    )

    suspend fun request(client: RekuesterClient) {
        requestResult = client.request(
            method = method.value,
            address = address.value,
            params = params.toList(),
            headers = headers.toList(),
            body = requestBody.value
        )
    }


    fun composeNewApi(): Api {
        return Api(
            uuid = uuid,
            collectionUUID = collectionUUID,
            label = label.value,
            method = method.value,
            address = address.value,
            params = params.toList(),
            headers = headers.toList(),
            requestBody = requestBody.value,
        )
    }

    @StateFactoryMarker
    inline fun <reified T> autoTagModifiedStateOf(initial: T): AutoActionMutableState<T> {
        return autoActionStateOf(initial) {
            if (!isModified.value) isModified.setTrue()
        }
    }

    @StateFactoryMarker
    inline fun <reified T> autoTagModifiedStateListOf(initial: List<T>): AutoActionMutableStateList<T> {
        return autoActionStateListOf(initial) {
            if (!isModified.value) isModified.setTrue()
        }
    }
}

class UrlParamsBinding(
    private val address: AutoActionMutableState<String>,
    private val params: AutoActionMutableStateList<ApiParam>
) {
    val text = mutableStateOf(buildUrl(address.value, params.toList()))

    init {
        // 监听 address 或 params 变化时更新 text
        snapshotFlow { address.value to params.toList() }
            .onEach { (addr, ps) ->
                val newUrl = buildUrl(addr, ps)
                if (text.value != newUrl) {
                    text.value = newUrl
                }
            }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }

    fun onTextChange(newText: String) {
        text.value = newText
        tryParseUrl(newText)?.let { (newAddress, pairs) ->
            if (newAddress != address.value) address.value = newAddress
            params.clear()
            params.addAll(pairs.map {
                ApiParam(
                key = it.first,
                value = it.second,
                description = "",
                enabled = true
            )
            })
        }
    }

    private fun buildUrl(address: String, params: List<ApiParam>): String {
        return try {
            URLBuilder(address).apply {
                params.forEach { parameters.append(it.key, it.value) }
            }.buildString()
        } catch (e: Exception) {
            address // fallback，鲁棒
        }
    }

    private fun tryParseUrl(url: String): Pair<String, List<Pair<String, String>>>? {
        return try {
            val builder = URLBuilder(url)
            val newParams = builder.parameters.entries()
                .flatMap { item -> item.value.map { it -> item.key to it } }
            val newAddress = builder.apply { parameters.clear() }.buildString()
            newAddress to newParams
        } catch (e: Exception) {
            null // 格式错误时不影响现有状态
        }
    }

    fun onUrlEditStart() {

    }

    fun onParamsEditStart() {

    }
}
