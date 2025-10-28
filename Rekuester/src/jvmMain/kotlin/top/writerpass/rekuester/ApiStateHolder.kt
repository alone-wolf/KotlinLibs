package top.writerpass.rekuester

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.writerpass.kmplibrary.utils.getOrCreate
import top.writerpass.kmplibrary.utils.println

object ApiStateHolder {
    private val apiStateMap = mutableMapOf<String, ApiState>()
//    fun setApiState(apiState: ApiState) {
//        apiStateMap[apiState.uuid] = apiState
//    }

    val BLANK = ApiState(Api.BLANK)

    fun getApiState(api: Api): ApiState {
        if (api.uuid == "--"){
            return BLANK
        }
        var exists = true
        var apiState = apiStateMap[api.uuid]
        if (apiState == null){
            apiState = ApiState(api)
            apiStateMap[api.uuid] = apiState
            exists = false
        }
        "in getApiState ${api.uuid} exists already:${exists}".println()
        return apiState
    }

//    fun clear() {
//        apiStateMap.clear()
//    }
//
//    fun remove(api: Api) {
//        apiStateMap.remove(api.uuid)
//    }
//
//    fun flush(api: Api): ApiState {
//        apiStateMap.remove(api.uuid)
//        val new = ApiState(api)
//        apiStateMap[api.uuid] = new
//        return new
//    }

//    @Composable
//    fun rememberApiState(api: Api): ApiState {
//        return remember { getApiState(api) }
//    }
}