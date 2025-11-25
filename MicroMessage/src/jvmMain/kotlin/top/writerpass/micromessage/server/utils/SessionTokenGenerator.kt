package top.writerpass.micromessage.server.utils

import java.security.SecureRandom
import kotlin.io.encoding.Base64

object SessionTokenGenerator {

    private val secureRandom = SecureRandom()  // 线程安全单例

    /**
     * 生成安全的随机 session-token
     * @param byteSize token 原始字节长度（16 → 128bit，32 → 256bit）
     * @return URL-safe 字符串，不含 + / =
     */
    fun generate(byteSize: Int = 32): String {
        val bytes = ByteArray(byteSize)
        secureRandom.nextBytes(bytes)

        // Base64 URL-safe，去掉 =
        return Base64.UrlSafe
            .withPadding(Base64.PaddingOption.ABSENT)
            .encode(bytes)
    }
}