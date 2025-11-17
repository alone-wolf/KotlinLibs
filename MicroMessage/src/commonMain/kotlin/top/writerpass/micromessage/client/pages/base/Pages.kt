package top.writerpass.micromessage.client.pages.base

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.micromessage.client.LocalNavController

interface Pages {
    val routeRaw: String
    val route: String
    val label: String
    val leftTopIcon: @Composable () -> Unit
        get() = {
            val navController = LocalNavController.current
            Icons.Default.ArrowBackIosNew.CxIconButton {
                navController.popBackStack()
            }
        }
    val actions: @Composable RowScope.() -> Unit
    val fab: @Composable () -> Unit
        get() = {}
    val content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
}