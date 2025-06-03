package top.writerpass.ktorlibrary.utils

import io.ktor.http.ContentType

val ContentType.mime: String
    get() = "${contentType}/${contentSubtype}"