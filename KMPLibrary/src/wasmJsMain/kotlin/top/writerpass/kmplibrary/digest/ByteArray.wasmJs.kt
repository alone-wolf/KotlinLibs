package top.writerpass.kmplibrary.digest

import top.writerpass.kmplibrary.coroutine.withContextDefault

//actual suspend fun ByteArray.calcSHA256String(): String {
////    return withContextDefault {
////        MessageDigest.getInstance("SHA-256")
////            .digest(this@calcSHA256String)
////            .joinToString("") { "%02x".format(it) }
////    }
//    error("Not implemented ByteArray.calcSHA256String(): String for WasmJS")
//}