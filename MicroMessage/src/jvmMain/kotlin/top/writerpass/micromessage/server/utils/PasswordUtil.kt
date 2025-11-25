package top.writerpass.micromessage.server.utils

import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtil {
    fun generateSalt(): String =
        SecureRandom().let { rnd ->
            ByteArray(16).apply { rnd.nextBytes(this) }.encodeBase64()
        }

    fun hash(password: String, salt: String): String {
        val keySpec = PBEKeySpec(password.toCharArray(), salt.decodeBase64Bytes(), 20000, 256)
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = keyFactory.generateSecret(keySpec).encoded
        return hash.encodeBase64()
    }
}