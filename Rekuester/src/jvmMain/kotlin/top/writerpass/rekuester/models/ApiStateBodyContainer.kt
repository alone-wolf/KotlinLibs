package top.writerpass.rekuester.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiStateBodyContainer(
    val type: BodyTypes = BodyTypes.None,
    val formData: List<FormData>? = null,
    val formUrlEncoded: List<FormData>? = null,
    val raw: Raw? = null,
    val binary: Binary? = null,
    val graphQL: GraphQL? = null
) {

    companion object {
        val None = ApiStateBodyContainer(BodyTypes.None)
    }

    @Serializable
    data class FormData(
        val key: String,
        val value: String,
        val description: String,
        val enabled: Boolean
    )

    @Serializable
    data class Raw(
        val type: RawBodyTypes,
        val content: String
    )

    @Serializable
    data class Binary(
        val path: String,
        val filename: String,
        val size: Long
    )

    @Serializable
    data class GraphQL(
        val query: String,
        val variables: String
    )
}