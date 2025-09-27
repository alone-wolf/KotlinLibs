package top.writerpass.rekuester

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.serialization.json.Json
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.rekuester.data.ApisRepository

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
    private val startAt: Long = System.currentTimeMillis()
    private val endAt = mutableStateOf(startAt)
    val isModified = derivedStateOf {
        startAt != endAt.value
    }

    inner class AutoTagMutableState<T>(
        initial: T,
    ) : MutableState<T> {

        private val state = mutableStateOf(initial)

        override var value: T
            get() = state.value
            set(value) {
                state.value = value
                endAt.value = System.currentTimeMillis()
            }

        override fun component1(): T = state.component1()
        override fun component2(): (T) -> Unit = state.component2()
    }
    val label = AutoTagMutableState(api.basicInfo.label)
    val method = AutoTagMutableState(api.basicInfo.method)
    val address = AutoTagMutableState(api.basicInfo.address)
    val params = AutoTagMutableState(api.params)
    val headers = AutoTagMutableState(api.headers)
    val requestBody = AutoTagMutableState(api.requestBody)


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
}

object ApiStateHolder {
    private val apiStateMap = mutableMapOf<String, ApiState>()
    fun setApiState(apiState: ApiState) {
        apiStateMap[apiState.uuid] = apiState
    }

    fun getApiState(api: Api): ApiState {
        return apiStateMap.getOrCreate(api.uuid) { ApiState(api) }
    }

    @Composable
    fun rememberApiState(api: Api): ApiState {
        return remember { getApiState(api) }
    }
}