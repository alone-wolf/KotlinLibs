package top.writerpass.rekuester.data.dao

import kotlinx.coroutines.flow.Flow

interface BaseDataDao<ID, ITEM : ItemWithId<ID>> {
    suspend fun findById(id: ID): ITEM?
    fun findByIdFlow(id: ID): Flow<ITEM?>
    suspend fun findAll(): List<ITEM>
    val allFlow: Flow<List<ITEM>>
    suspend fun delete(id: ID)
    suspend fun delete(item: ITEM)
    suspend fun insert(item: ITEM)
    suspend fun update(item: ITEM)
}