package top.writerpass.rekuester.models

import kotlinx.serialization.Serializable
import top.writerpass.rekuester.data.dao.ItemWithId
import java.util.UUID

@Serializable
data class Collection(
    val uuid: String = UUID.randomUUID().toString(),
    val label: String,
    val createdAt: Long = System.currentTimeMillis()
) : ItemWithId<String> {
    override val id: String = uuid

    companion object {
        val BLANK = Collection(
            uuid = "--",
            label = "untitled",
            createdAt = System.currentTimeMillis(),
        )
    }
}