package top.writerpass.ktorlibrary.utils

import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders

fun HeadersBuilder.contentDispositionFormFile(name: String, filename: String) {
    append(HttpHeaders.ContentDisposition, "form-data; name=\"${name}\"; filename=\"${filename}\"")
}

fun HeadersBuilder.contentType(type: ContentType) {
    append(HttpHeaders.ContentType, type.mime)
}