package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.pages.base.IPage

object PrivateChatPage : IPage {
    override val routeBase: String
        get() = "private-chat"
    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    override val label: String
        get() = "Private Chat"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {
            val navController = LocalNavController.current
            Icons.Default.ArrowBackIosNew.CxIconButton {
                navController.c.popBackStack()
            }
        }
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            Icons.Default.Person.CxIconButton {

            }
        }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {}
}