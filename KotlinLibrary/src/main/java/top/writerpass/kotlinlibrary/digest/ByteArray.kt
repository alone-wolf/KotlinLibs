package top.writerpass.kotlinlibrary.digest

import top.writerpass.kotlinlibrary.coroutine.withContextDefault
import java.security.MessageDigest

suspend fun ByteArray.calcSHA256String(): String {
    return withContextDefault {
        MessageDigest.getInstance("SHA-256")
            .digest(this@calcSHA256String)
            .joinToString("") { "%02x".format(it) }
    }
}

