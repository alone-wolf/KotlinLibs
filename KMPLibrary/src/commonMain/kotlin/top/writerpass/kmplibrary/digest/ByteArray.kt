package top.writerpass.kmplibrary.digest

import org.kotlincrypto.hash.sha2.SHA256
import top.writerpass.kmplibrary.coroutine.withContextDefault
import top.writerpass.kmplibrary.utils.toHexString

//suspend fun ByteArray.calcSHA256String(): String {
//    return withContextDefault {
//        val digest = SHA256()
//        digest.update(this@calcSHA256String)
//        digest.digest().toHexString()
//    }
//}



