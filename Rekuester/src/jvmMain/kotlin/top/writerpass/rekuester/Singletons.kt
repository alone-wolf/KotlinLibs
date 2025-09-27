package top.writerpass.rekuester

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.StateFactoryMarker
import kotlinx.serialization.json.Json
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.rekuester.data.ApisRepository
import top.writerpass.rekuester.utils.AutoActionMutableState

object Singletons {
    val apisRepository = ApisRepository()
    val client = RekuesterClient()

    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
}


class ApiState(
    api: Api,
    val uuid: String = api.uuid,
) {
    val isModified = mutableStateOf(false)

    val label = autoTagModifiedMutableStateOf(api.basicInfo.label)
    val method = autoTagModifiedMutableStateOf(api.basicInfo.method)
    val address = autoTagModifiedMutableStateOf(api.basicInfo.address)
    val params = autoTagModifiedMutableStateOf(api.params)
    val headers = autoTagModifiedMutableStateOf(api.headers)
    val requestBody = autoTagModifiedMutableStateOf(api.requestBody)


    fun composeNewApi(): Api {
        return Api(
            uuid = uuid,
            basicInfo = ApiBasicInfo(
                label = label.value,
                method = method.value,
                address = address.value
            ),
            params = params.value,
            headers = headers.value,
            requestBody = requestBody.value
        )
    }

    @StateFactoryMarker
    fun <T> autoTagModifiedMutableStateOf(initial: T): AutoActionMutableState<T> {
        return AutoActionMutableState(initial) {
            isModified.setTrue()
        }
    }
}

fun <K,V>Map<K,List<V>>.deList():List<Pair<K,V>>{
    return this.flatMap { (k, v) -> v.map { Pair(k, it) } }
}

object ApiStateHolder {
    private val apiStateMap = mutableMapOf<String, ApiState>()
    fun setApiState(apiState: ApiState) {
        apiStateMap[apiState.uuid] = apiState
    }

    fun getApiState(api: Api): ApiState {
        return apiStateMap.getOrCreate(api.uuid) { ApiState(api) }
    }

    fun clear(){
        apiStateMap.clear()
    }

    fun remove(api: Api){
        apiStateMap.remove(api.uuid)
    }

    fun flush(api: Api): ApiState {
        apiStateMap.remove(api.uuid)
        val new = ApiState(api)
        apiStateMap[api.uuid] = new
        return new
    }

    @Composable
    fun rememberApiState(api: Api): ApiState {
        return remember { getApiState(api) }
    }
}