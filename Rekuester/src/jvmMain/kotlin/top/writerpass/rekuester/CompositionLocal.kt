package top.writerpass.rekuester

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import top.writerpass.rekuester.viewmodel.MainUiViewModel
import top.writerpass.rekuester.viewmodel.MainViewModel

val LocalMainViewModel =
    staticCompositionLocalOf<MainViewModel> { error("No MainViewModel provided") }
val LocalMainUiViewModel =
    staticCompositionLocalOf<MainUiViewModel> { error("No MainUiViewModel provided") }
val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("No NavHostController provided") }

