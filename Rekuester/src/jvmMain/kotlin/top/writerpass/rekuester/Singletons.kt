package top.writerpass.rekuester

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.serialization.json.Json
import top.writerpass.rekuester.data.ApiRepository
import top.writerpass.rekuester.data.CollectionsRepository

object Singletons {
    val apiRepository = ApiRepository()
    val collectionsRepository = CollectionsRepository()
    val client = RekuesterClient()

    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    val viewModelStoreOwner = object : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore = ViewModelStore()
    }
}
