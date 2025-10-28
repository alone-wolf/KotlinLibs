package top.writerpass.rekuester.models

import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import top.writerpass.rekuester.ApiStateAuthContainer
import top.writerpass.rekuester.HttpMethodSerializer
import top.writerpass.rekuester.data.dao.ItemWithId
import top.writerpass.rekuester.ui.page.AuthTypes
import java.util.UUID

@Serializable
data class Api(
    val uuid: String = UUID.randomUUID().toString(),
    val collectionUUID: String = "default",
    val label: String,
    @Serializable(with = HttpMethodSerializer::class)
    val method: HttpMethod,
    val address: String,
    val params: List<ApiParam> = emptyList(),
    val headers: List<ApiHeader> = emptyList(),
    val requestBody: String? = null,
    val auth: ApiStateAuthContainer = ApiStateAuthContainer.Inherit,
    val bodyType: BodyType = BodyType.None
) : ItemWithId<String> {
    override val id: String = uuid

    companion object {
        val BLANK = Api(
            uuid = "--",
            collectionUUID = "--",
            label = "untitled",
            method = HttpMethod.Get,
            address = "http://",
            params = emptyList(),
            headers = emptyList(),
            requestBody = null,
        )
    }
}