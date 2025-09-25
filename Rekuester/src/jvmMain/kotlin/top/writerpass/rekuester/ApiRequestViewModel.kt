package top.writerpass.rekuester

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import top.writerpass.kmplibrary.coroutine.launchIO

class ApiRequestViewModel(
    private val client: RekuesterClient,
    private val api: Api
) : ViewModel() {
    val isModified = mutableStateOf(false)
    val label = mutableStateOf(api.basicInfo.label)
    val method = mutableStateOf(api.basicInfo.method)
    val address = mutableStateOf(api.basicInfo.address)

    val params = mutableStateMapOf<String, List<String>>()

    init {
        api.params.forEach { (key, value) ->
            params[key] = value
        }
    }

    val headers = mutableStateMapOf<String, List<String>>()

    init {
        api.headers.forEach { (key, value) ->
            headers[key] = value
        }
    }

    val requestBody by mutableStateOf(api.requestBody)


    var currentResult by mutableStateOf<HttpRequestResult?>(null)
        private set

    fun request() {
        viewModelScope.launchIO {
            currentResult = client.request(
                method = method.value,
                address = address.value,
                params = params,
                headers = headers,
                body = requestBody
            )
        }
    }

    fun composeNewApi(): Api {
        return Api(
            uuid = api.uuid,
            basicInfo = ApiBasicInfo(
                label = label.value,
                method = method.value,
                address = address.value
            ),
            params = params.toMap(),
            headers = headers.toMap(),
            requestBody = requestBody,
        )
    }

//    fun request(api: Api) {
//        viewModelScope.launchIO {
//            currentResult = client.request(api)
//        }
//    }
}