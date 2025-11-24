package top.writerpass.micromessage.server.core.data.base

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass


interface BaseRepository<DataClass : BaseDataClass, DataEntity : LongEntity> {
    val dataEntityClass: LongEntityClass<DataEntity>
    suspend fun insert(data: DataClass): DataEntity
    suspend fun inserts(vararg data: DataClass): List<DataEntity> {
        return data.map { insert(it) }
    }

    suspend fun delete(id: Long) {
        dataEntityClass.findById(id)?.delete()
    }

    suspend fun delete(data: DataClass) {
        delete(data.id)
    }

    suspend fun deletes(ids: LongArray) {
        ids.forEach { delete(it) }
    }

    suspend fun deletes(vararg data: DataClass) {
        data.forEach { delete(it) }
    }

    suspend fun update(data: DataClass): DataEntity?
    suspend fun update(id: Long, block: (DataEntity) -> Unit): DataEntity? {
        return dataEntityClass.findByIdAndUpdate(id, block)
    }

    suspend fun updates(vararg data: DataClass): List<DataEntity?> {
        return data.map { update(it) }
    }

    suspend fun updates(ids: LongArray, block: (DataEntity) -> Unit): List<DataEntity?> {
        return ids.map { update(it, block) }
    }


    suspend fun selectById(id: Long): DataEntity? {
        return dataEntityClass.findById(id)
    }

    suspend fun selectAll(): List<DataEntity> {
        return dataEntityClass.all().toList()
    }
}