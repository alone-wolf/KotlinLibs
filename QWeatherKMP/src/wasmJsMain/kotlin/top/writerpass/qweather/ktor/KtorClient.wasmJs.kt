package top.writerpass.qweather.ktor

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.js.Js

actual val clientEngine: HttpClientEngineFactory<*> = CIO