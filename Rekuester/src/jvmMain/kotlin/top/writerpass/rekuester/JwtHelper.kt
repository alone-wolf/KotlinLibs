package top.writerpass.rekuester

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import top.writerpass.rekuester.models.ApiStateAuthContainer
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import kotlin.io.encoding.Base64

object JwtHelper {
    fun buildJwtToken(jwt: ApiStateAuthContainer.Jwt): String {
        val algorithm = when (jwt.algorithm) {
            // --- HMAC 系列 ---
            ApiStateAuthContainer.Jwt.Companion.Algorithm.HS256 -> Algorithm.HMAC256(getSecret(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.HS384 -> Algorithm.HMAC384(getSecret(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.HS512 -> Algorithm.HMAC512(getSecret(jwt))

            // --- RSA 系列 ---
            ApiStateAuthContainer.Jwt.Companion.Algorithm.RS256 -> Algorithm.RSA256(loadRSAPublicKey(jwt), loadRSAPrivateKey(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.RS384 -> Algorithm.RSA384(loadRSAPublicKey(jwt), loadRSAPrivateKey(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.RS512 -> Algorithm.RSA512(loadRSAPublicKey(jwt), loadRSAPrivateKey(jwt))

            // --- RSA-PSS 系列 ---
            ApiStateAuthContainer.Jwt.Companion.Algorithm.PS256 -> Algorithm.RSA256(loadRSAPublicKey(jwt), loadRSAPrivateKey(jwt)) // Auth0 SDK 不直接支持 PSS
            ApiStateAuthContainer.Jwt.Companion.Algorithm.PS384 -> Algorithm.RSA384(loadRSAPublicKey(jwt), loadRSAPrivateKey(jwt))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.PS512 -> Algorithm.RSA512(loadRSAPublicKey(jwt), loadRSAPrivateKey(jwt))

            // --- ECDSA 系列 ---
            ApiStateAuthContainer.Jwt.Companion.Algorithm.ES256 -> Algorithm.ECDSA256(loadECPublicKey(jwt, "secp256r1"), loadECPrivateKey(jwt, "secp256r1"))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.ES384 -> Algorithm.ECDSA384(loadECPublicKey(jwt, "secp384r1"), loadECPrivateKey(jwt, "secp384r1"))
            ApiStateAuthContainer.Jwt.Companion.Algorithm.ES512 -> Algorithm.ECDSA512(loadECPublicKey(jwt, "secp521r1"), loadECPrivateKey(jwt, "secp521r1"))
        }

        return JWT.create()
            .withPayload(jwt.payload)
            .withHeader(parseJwtHeaders(jwt.jwtHeaders))
            .sign(algorithm)
    }

    private fun getSecret(jwt: ApiStateAuthContainer.Jwt): ByteArray =
        if (jwt.secretBase64Encoded)
            Base64.decode(jwt.secret)
        else
            jwt.secret.toByteArray()

    private fun loadRSAPublicKey(jwt: ApiStateAuthContainer.Jwt): RSAPublicKey? {
        return try {
            val keyBytes = Base64.decode(jwt.jwtHeaders) // 假设 jwtHeaders 里放公钥字符串
            val spec = X509EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("RSA")
            kf.generatePublic(spec) as RSAPublicKey
        } catch (_: Exception) {
            null
        }
    }

    private fun loadRSAPrivateKey(jwt: ApiStateAuthContainer.Jwt): RSAPrivateKey? {
        return try {
            val keyBytes = Base64.decode(jwt.jwtHeaders)
            val spec = PKCS8EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("RSA")
            kf.generatePrivate(spec) as RSAPrivateKey
        } catch (_: Exception) {
            null
        }
    }

    private fun loadECPublicKey(jwt: ApiStateAuthContainer.Jwt, curve: String): ECPublicKey? {
        return try {
            val keyBytes = Base64.decode(jwt.jwtHeaders) // 假设放公钥
            val spec = X509EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("EC")
            kf.generatePublic(spec) as ECPublicKey
        } catch (_: Exception) {
            null
        }
    }

    private fun loadECPrivateKey(jwt: ApiStateAuthContainer.Jwt, curve: String): ECPrivateKey? {
        return try {
            val keyBytes = Base64.decode(jwt.jwtHeaders)
            val spec = PKCS8EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("EC")
            kf.generatePrivate(spec) as ECPrivateKey
        } catch (_: Exception) {
            null
        }
    }


    private fun parseJwtHeaders(json: String): Map<String, Any> {
        return try {
            Json.decodeFromString(
                MapSerializer(
                    serializer<String>(),
                    serializer<String>()
                ),
                json
            )
        } catch (_: Exception) {
            emptyMap()
        }
    }
}