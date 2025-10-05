package top.writerpass.rekuester

import kotlinx.serialization.Serializable

@Serializable
sealed interface Pages {
    @Serializable
    object BlankPage : Pages

    @Serializable
    class ApiRequestPage(val apiUuid: String) : Pages
}