package top.writerpass.micromessage.sdk

import kotlinx.coroutines.runBlocking

fun main() {
    val baseUrl = "http://127.0.0.1:8080"
    val client = ApiClient(
        baseUrl = baseUrl
    )

    runBlocking {
        val username = "writerpass"
        val password = "abc123"
//        client.requestDebugDump()
        client.auth.register(username, password)
    }
}