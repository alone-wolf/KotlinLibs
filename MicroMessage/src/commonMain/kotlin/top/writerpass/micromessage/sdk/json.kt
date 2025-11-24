package top.writerpass.micromessage.sdk

import kotlinx.serialization.json.Json

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