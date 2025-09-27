package top.writerpass.rekuester.data.dao

import kotlinx.coroutines.flow.MutableStateFlow

class ListDataStore<Id, Item : ItemWithId<Id>> {
    val items = mutableListOf<Item>()
    val itemsFlow= MutableStateFlow(emptyList<Item>())
    suspend fun emitItems() {
        val newItems = items.toList()
        itemsFlow.emit(newItems)
    }
}