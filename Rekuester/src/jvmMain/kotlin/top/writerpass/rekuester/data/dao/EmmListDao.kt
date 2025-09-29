package top.writerpass.rekuester.data.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

abstract class EmmListDao<Id, Item : ItemWithId<Id>> : BaseDataDao<Id, Item> {
    private val dataStore = ListDataStore<Id, Item>()
    override val allFlow: StateFlow<List<Item>> = dataStore.itemsFlow.asStateFlow()

    override suspend fun findAll(): List<Item> = dataStore.items.toList()
    override suspend fun findById(id: Id): Item? {
        return dataStore.items.find { it.id == id }
    }

    override fun findByIdFlow(id: Id): Flow<Item?> {
        return allFlow.map { list -> list.find { it.id == id } }
            .distinctUntilChanged()
    }

    suspend fun delete(index: Int) {
        dataStore.items.removeAt(index)
        dataStore.emitItems()
    }

    override suspend fun delete(id: Id) {
        val index = dataStore.items.indexOfFirst { it.id == id }
        delete(index)
    }

    override suspend fun delete(item: Item) {
        val index = dataStore.items.indexOfFirst { it.id == item.id }
        delete(index)
    }

    override suspend fun insert(item: Item) {
        dataStore.items.add(item)
        dataStore.emitItems()
    }

    suspend fun inserts(vararg items: Item) {
        dataStore.items.addAll(items)
        dataStore.emitItems()
    }

    override suspend fun update(item: Item) {
        val index = dataStore.items.indexOfFirst { it.id == item.id }
        dataStore.items[index] = item
        dataStore.emitItems()
    }

    suspend fun updateOrInsert(item: Item) {
        val index = dataStore.items.indexOfFirst { it.id == item.id }
        if (index == -1) {
            insert(item)
        } else {
            update(item)
        }
    }

    suspend fun overWriteItems(items: List<Item>) {
        dataStore.items.clear()
        dataStore.items.addAll(items)
        dataStore.emitItems()
    }
}