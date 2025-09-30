package top.writerpass.rekuester

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.StateFactoryMarker
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.utils.AutoActionMutableState
import top.writerpass.rekuester.utils.AutoActionMutableStateList
import top.writerpass.rekuester.utils.flatToList
import top.writerpass.rekuester.utils.pairListToMap

class ApiState(
    api: Api,
    val uuid: String = api.uuid,
) {
    val collectionUUID = api.collectionUUID
    val isModified = mutableStateOf(false)

    val label = autoTagModifiedStateOf(api.label)
    val method = autoTagModifiedStateOf(api.method)
    val address = autoTagModifiedStateOf(api.address)
    val params = autoTagModifiedStateListOf(api.params.flatToList())
    val headers = autoTagModifiedStateListOf(api.headers.flatToList())
    val requestBody = autoTagModifiedStateOf(api.requestBody)


    fun composeNewApi(): Api {
        return Api(
            uuid = uuid,
            collectionUUID = collectionUUID,
            label = label.value,
            method = method.value,
            address = address.value,
            params = params.list.toList().pairListToMap(),
            headers = headers.list.toList().pairListToMap(),
            requestBody = requestBody.value,
            tabOpened = true,
        )
    }

    @StateFactoryMarker
    fun <T> autoTagModifiedStateOf(initial: T): AutoActionMutableState<T> {
        return AutoActionMutableState(initial) {
            isModified.setTrue()
        }
    }

    @StateFactoryMarker
    fun <T> autoTagModifiedStateListOf(initial: List<T>): AutoActionMutableStateList<T> {
        return AutoActionMutableStateList(initial) {
            isModified.setTrue()
        }
    }
}