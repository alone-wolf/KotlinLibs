package top.writerpass.rekuester

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ApplicationScope
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import top.writerpass.rekuester.viewmodel.CollectionApiViewModel
import top.writerpass.rekuester.viewmodel.CollectionsViewModel
import top.writerpass.rekuester.viewmodel.MainUiViewModel
import top.writerpass.rekuester.viewmodel.MainViewModel

val LocalMainViewModel =
    staticCompositionLocalOf<MainViewModel> { error("No MainViewModel provided") }
val LocalMainUiViewModel =
    staticCompositionLocalOf<MainUiViewModel> { error("No MainUiViewModel provided") }
val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("No NavHostController provided") }

val LocalCollectionsViewModel =
    staticCompositionLocalOf<CollectionsViewModel> { error("No CollectionsViewModel provided") }

val LocalCollectionApiViewModel =
    compositionLocalOf<CollectionApiViewModel> { error("No CollectionApiViewModel provided") }

val LocalApplicationScope =
    staticCompositionLocalOf<ApplicationScope> { error("No ApplicationScope provided") }

val LocalAppViewModelStoreOwner = staticCompositionLocalOf<ViewModelStoreOwner> {
    error("No ViewModelStoreOwner #1 provided")
}

