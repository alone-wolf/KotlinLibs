package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.pages.base.Page

object PrivateChatPage : Page {
    override val routeRaw: String
        get() = "private-chat-page"
    override val route: String
        get() = "private-chat-page/{id}"
    override val label: String
        get() = "Private Chat"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {
            val navController = LocalNavController.current
            Icons.Default.ArrowBackIosNew.CxIconButton {
                navController.popBackStack()
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