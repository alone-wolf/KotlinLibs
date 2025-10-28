package top.writerpass.rekuester.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiStateAuthContainer(
    val type: AuthTypes,
    val basic: Basic? = null,
    val bearer: Bearer? = null,
    val jwt: Jwt? = null,
    val apiKey: ApiKey? = null,
) {
    companion object {
        val Inherit = ApiStateAuthContainer(AuthTypes.InheritAuthFromParent)
    }

    @Serializable
    data class Basic(val username: String, val password: String)

    @Serializable
    data class Bearer(val token: String)

    @Serializable
    data class Jwt(
        val addTo: AddTo,
        val algorithm: Algorithm,
        val secret: String,
        val secretBase64Encoded: Boolean,
        val payload: String,
        val requestPrefix: String,
        val jwtHeaders: String,
    ) {
        companion object {
            @Serializable
            enum class AddTo {
                Header, Param
            }

            @Serializable
            enum class Algorithm {
                HS256, HS384, HS512,
                RS256, RS384, RS512,
                PS256, PS384, PS512,
                ES256, ES384, ES512
            }
        }
    }

    @Serializable
    data class ApiKey(
        val key: String,
        val value: String,
        val addTo: AddTo
    ) {
        companion object {
            @Serializable
            enum class AddTo {
                Header, Param
            }
        }
    }
}