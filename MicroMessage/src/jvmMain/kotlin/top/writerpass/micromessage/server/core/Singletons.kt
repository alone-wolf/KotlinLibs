package top.writerpass.micromessage.server.core

import kotlinx.serialization.json.Json
import top.writerpass.micromessage.server.server.DatabaseContainer

object Singletons {
    val singletonMap = mutableMapOf<String, Any?>()
    inline fun <reified T> set(value: T) {
        singletonMap[T::class.qualifiedName!!] = value
    }

    inline fun <reified T> get(): T {
        return singletonMap.getValue(T::class.qualifiedName!!) as T
    }

    inline fun <reified T> getOrNull(): T? {
        return singletonMap.getValue(T::class.qualifiedName!!) as T?
    }

    inline fun <reified T> gets(): T {
        return when (T::class) {
            Json::class -> prettyJson as T
            DatabaseContainer::class -> databaseContainer as T
            ClassScanner::class -> classScanner as T
            else -> get<T>()
        }
    }

    val prettyJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }

    val compactJson = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }


    val databaseContainer = DatabaseContainer()

    val classScanner = ClassScanner()

    val register = Register()

}