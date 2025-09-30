package top.writerpass.rekuester

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.writerpass.kmplibrary.utils.getOrCreate

object ApiStateHolder {
    private val apiStateMap = mutableMapOf<String, ApiState>()
    fun setApiState(apiState: ApiState) {
        apiStateMap[apiState.uuid] = apiState
    }

    fun getApiState(api: Api): ApiState {
        return apiStateMap.getOrCreate(api.uuid) { ApiState(api) }
    }

    fun clear() {
        apiStateMap.clear()
    }

    fun remove(api: Api) {
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