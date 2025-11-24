package top.writerpass.micromessage.server.core.data.service.user.request

data class SearchQuery(
    val keyword: String?,
    val page: Int = 1,
    val size: Int = 20
)