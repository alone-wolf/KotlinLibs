package top.writerpass.rekuester.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import top.writerpass.rekuester.Singletons
import top.writerpass.rekuester.data.ApiRepository
import top.writerpass.rekuester.data.CollectionsRepository

class CollectionsViewModel : BaseViewModel() {
    private val collectionsRepository: CollectionsRepository = Singletons.collectionsRepository
    private val apiRepository: ApiRepository = Singletons.apiRepository
    val collectionsFlow = collectionsRepository.allFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    init {
        runInScope {
            collectionsRepository.initialLoadData()
            apiRepository.initialLoadData()
            currentCollectionUUID = collectionsFlow.value.firstOrNull()?.uuid ?: "default"
        }
    }

    var currentCollectionUUID by mutableStateOf("default")
    val currentCollection by derivedStateOf {
        collectionsFlow.value.find { it.uuid == currentCollectionUUID }
    }
}