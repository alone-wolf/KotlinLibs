package top.writerpass.rekuester.data

import top.writerpass.kmplibrary.utils.fill2Number
import top.writerpass.rekuester.Collection
import top.writerpass.rekuester.data.dao.EmmListDao

class CollectionsRepository() {
    private val dao = object : EmmListDao<String, Collection>() {}
    suspend fun initialLoadData(){
        val collections = (0 until 10).map { index ->
            Collection(
                label = "Collection#${index.fill2Number}",
                uuid = "uuid-${index.fill2Number}"
            )
        }.toTypedArray()
        dao.inserts(*collections)
        dao.insert(
            Collection(
                label = "Default Collection",
                uuid = "default"
            )
        )
    }
    val allFlow = dao.allFlow
    suspend fun findAll() = dao.findAll()
    suspend fun findById(id: String) = dao.findById(id)
    fun findByIdFlow(id: String) = dao.findByIdFlow(id)
    suspend fun delete(index: Int) = dao.delete(index)
    suspend fun delete(id: String) = dao.delete(id)
    suspend fun delete(item: Collection) = dao.delete(item)
    suspend fun insert(item: Collection) = dao.insert(item)
    suspend fun inserts(vararg items: Collection) = dao.inserts(*items)
    suspend fun update(item: Collection) = dao.update(item)

    suspend fun updateOrInsert(items: Collection) = dao.updateOrInsert(items)

    suspend fun overWriteItems(items: List<Collection>) = dao.overWriteItems(items)
}