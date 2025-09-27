package top.writerpass.rekuester.data.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

abstract class EmmListDao<ID, ITEM : ItemWithId<ID>> : BaseDataDao<ID, ITEM> {
    private val dataStore = object : BaseDataStore<ID, ITEM> {
        override val items = mutableListOf<ITEM>()
        override val itemsFlow = MutableStateFlow<List<ITEM>>(emptyList())
    }
    private val allItems: List<ITEM> = java.util.List.copyOf(dataStore.items)
    override val allFlow: Flow<List<ITEM>> = dataStore.itemsFlow.asStateFlow()

    override suspend fun findAll(): List<ITEM> = allItems
    override suspend fun findById(id: ID): ITEM? {
        return dataStore.items.find { it.id == id }
    }

    override fun findByIdFlow(id: ID): Flow<ITEM?> {
        return allFlow.map { list -> list.find { it.id == id } }
            .distinctUntilChanged()
    }

    suspend fun delete(index: Int) {
        dataStore.items.removeAt(index)
        dataStore.emitItems()
    }

    override suspend fun delete(id: ID) {
        val index = allItems.indexOfFirst { it.id == id }
        delete(index)
    }

    override suspend fun delete(item: ITEM) {
        val index = allItems.indexOfFirst { it.id == item.id }
        delete(index)
    }

    override suspend fun insert(item: ITEM) {
        dataStore.items.add(item)
        dataStore.emitItems()
    }

    suspend fun inserts(vararg items: ITEM) {
        dataStore.items.addAll(items)
        dataStore.emitItems()
    }

    override suspend fun update(item: ITEM) {
        val index = allItems.indexOfFirst { it.id == item.id }
        dataStore.items[index] = item
        dataStore.emitItems()
    }

    suspend fun overWriteItems(items: List<ITEM>){
        dataStore.items.clear()
        dataStore.items.addAll(items)
        dataStore.emitItems()
    }
}