package top.writerpass.rekuester.data.dao

import kotlinx.coroutines.flow.MutableStateFlow

interface BaseDataStore<ID, ITEM : ItemWithId<ID>> {
    val items: MutableList<ITEM>
    val itemsFlow: MutableStateFlow<List<ITEM>>
    suspend fun emitItems() {
        val newItems: List<ITEM> = java.util.List.copyOf(items)
        itemsFlow.emit(newItems)
    }
}