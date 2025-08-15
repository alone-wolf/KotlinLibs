package top.writerpass.qweather.ktor

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.java.Java

actual val clientEngine:HttpClientEngineFactory<*> = Java