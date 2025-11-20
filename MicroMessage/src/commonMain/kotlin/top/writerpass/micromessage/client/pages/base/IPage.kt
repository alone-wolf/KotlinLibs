package top.writerpass.micromessage.client.pages.base

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController

interface PageWithArgs {

}

interface IPage {
    val routeTemplate: String // routeName/{id}
        get() = StringBuilder(routeBase).apply {
            if (arguments.isNotEmpty()) {
                arguments.forEach { argument ->
                    append("/{")
                    append(argument.name)
                    append("}")
                }
            }
        }.toString()
    val routeBase: String // routeName
    val deepLink: List<NavDeepLink>
        get() = emptyList()
    val arguments: List<NamedNavArgument>
        get() = emptyList()
    val label: String
    val labelCompose: @Composable () -> Unit
        get() = { label.CxText() }
    val leftTopIcon: @Composable () -> Unit
        get() = {
            val navController = LocalNavController.current
            Icons.Default.ArrowBackIosNew.CxIconButton {
                navController.c.popBackStack()
            }
        }
    val actions: @Composable RowScope.() -> Unit
        get() = {}

    val fab: @Composable () -> Unit
        get() = {}
    val fabPosition: FabPosition
        get() = FabPosition.End

    val content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
}
