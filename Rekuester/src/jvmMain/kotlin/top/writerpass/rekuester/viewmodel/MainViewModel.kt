@file:OptIn(ExperimentalSerializationApi::class)

package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.ExperimentalSerializationApi
import top.writerpass.rekuester.Api
import top.writerpass.rekuester.Singletons


class MainViewModel() : BaseViewModel() {
    private val apiRepository = Singletons.apiRepository

    val openedApis = mutableStateListOf<Api>()


    fun saveData() {
//        viewModelScope.launchIO {
//            val json = Singletons.json
//            json.encodeToStream(
//                value = apiRepository.findAll(),
//                stream = File("apis.json").outputStream()
//            )
//        }
    }

    fun loadData() {
//        viewModelScope.launchIO {
//            val json = Singletons.json
//            val data = json.decodeFromStream<List<Api>>(File("apis.json").inputStream())
//            apiRepository.overWriteItems(data)
//        }
    }
}