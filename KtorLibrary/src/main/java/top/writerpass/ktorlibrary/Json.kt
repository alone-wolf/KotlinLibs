package top.writerpass.ktorlibrary

import kotlinx.serialization.json.Json

val commonDefaultJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}