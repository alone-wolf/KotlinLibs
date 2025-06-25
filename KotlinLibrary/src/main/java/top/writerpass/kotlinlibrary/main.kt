package top.writerpass.kotlinlibrary

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import top.writerpass.kotlinlibrary.lan.BroadcastMessenger
import top.writerpass.kotlinlibrary.lan.DeviceKnowledge
import top.writerpass.kotlinlibrary.lan.MulticastMessenger
import top.writerpass.kotlinlibrary.utils.println


val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = false
}

suspend fun testMulticast(coroutineScope: CoroutineScope, tag: String) {
    val messenger = MulticastMessenger(port = 4444, tag = tag)
    coroutineScope.launch {
        messenger.receive(timeoutMillis = null, filterTag = tag) { msg, from ->
            println("📡 Multicast recv: $msg from $from")
        }
    }
    delay(1000L)
    coroutineScope.launch {
        repeat(10) {
            messenger.send(DeviceKnowledge("127.0.0.1", 4444, "WPT14A").toJsonString())
            delay(1000)
        }
    }
}

suspend fun testBroadcast(coroutineScope: CoroutineScope, tag: String) {
    val messenger = BroadcastMessenger(port = 4444, tag = tag, useGlobalBroadcast = true)
    coroutineScope.launch {
        messenger.receive(timeoutMillis = null, filterTag = tag) { msg, from ->
            println("📡 Broadcast recv: $msg from $from")
        }
    }
    delay(1000L)
    coroutineScope.launch {
        repeat(10) {
            messenger.send(DeviceKnowledge("127.0.0.1", 4444, "WPT14A").toJsonString())
            delay(100L)
        }
    }
}


fun main() {
    runBlocking {
        val tag = "[WriterPass-KotlinLibrary] "
//        testBroadcast(this, tag)
        testMulticast(this, tag)
        delay(10000L)
    }
}