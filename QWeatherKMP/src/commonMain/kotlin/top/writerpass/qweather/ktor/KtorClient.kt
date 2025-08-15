package top.writerpass.qweather.ktor

import io.ktor.client.engine.HttpClientEngineFactory

expect val clientEngine:HttpClientEngineFactory<*>