package top.writerpass.rekuester

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
}
