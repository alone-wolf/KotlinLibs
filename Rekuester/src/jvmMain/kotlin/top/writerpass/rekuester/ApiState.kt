package top.writerpass.rekuester

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.StateFactoryMarker
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.utils.AutoActionMutableState
import top.writerpass.rekuester.utils.AutoActionMutableStateList
import top.writerpass.rekuester.utils.autoActionStateListOf
import top.writerpass.rekuester.utils.autoActionStateOf

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

    suspend fun request(client: RekuesterClient) {
        requestResult = client.request(
            method = method.value,
            address = address.value,
            params = params.list.toList(),
            headers = headers.list.toList(),
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
            params = params.list.toList(),
            headers = headers.list.toList(),
            requestBody = requestBody.value,
        )
    }

    @StateFactoryMarker
    inline fun <reified T> autoTagModifiedStateOf(initial: T): AutoActionMutableState<T> {
        return autoActionStateOf(initial) {
            if (!isModified.value) {
                isModified.setTrue()
            }
        }
    }

    @StateFactoryMarker
    inline fun <reified T> autoTagModifiedStateListOf(initial: List<T>): AutoActionMutableStateList<T> {
        return autoActionStateListOf(initial) {
            if (!isModified.value) {
                isModified.setTrue()
            }
        }
    }
}