package top.writerpass.ktorm.client

import io.ktor.client.engine.HttpClientEngineFactory

expect fun provideEngine(): HttpClientEngineFactory<*>