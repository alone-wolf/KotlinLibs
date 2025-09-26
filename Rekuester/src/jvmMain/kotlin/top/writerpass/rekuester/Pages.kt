package top.writerpass.rekuester

import kotlinx.serialization.Serializable

object Pages{
    @Serializable
    object BlankPage

    @Serializable
    class ApiRequestPage(val uuid: String)
}