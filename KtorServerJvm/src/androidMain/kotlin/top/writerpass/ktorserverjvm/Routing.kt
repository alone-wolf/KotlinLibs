package top.writerpass.ktorserverjvm

import android.content.Context
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.net.URLConnection

fun Application.assetsRouting(context: Context, assetsPath: String = "web") {
    routing {
        get("/{...}") {
            val relativePath = call.parameters.getAll("...")?.joinToString("/") ?: "index.html"
            val assetPath = "$assetsPath/$relativePath"

            try {
                val inputStream = context.assets.open(assetPath)
                val bytes = inputStream.readBytes()
                inputStream.close()

                // 根据扩展名推断 MIME 类型
                val contentType = ContentType.parse(
                    URLConnection.guessContentTypeFromName(assetPath) ?: "application/octet-stream"
                )

                call.respondBytes(bytes, contentType)
            } catch (e: Exception) {
                // 如果没找到对应资源，尝试返回 index.html (用于SPA前端)
                try {
                    val inputStream = context.assets.open("$assetsPath/index.html")
                    val bytes = inputStream.readBytes()
                    inputStream.close()

                    call.respondBytes(bytes, ContentType.Text.Html)
                } catch (_: Exception) {
                    call.respond(HttpStatusCode.NotFound, "Resource not found: $relativePath")
                }
            }
        }
    }
}
