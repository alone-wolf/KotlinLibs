package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.micromessage.client.pages.base.IPage

object UserAvatarPage : IPage {
    override val routeBase: String
        get() = "user-avatar"
    override val label: String
        get() = "Avatar"
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            Icons.Default.MoreHoriz.CxIconButton { }
        }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            FullSizeBox {
                Icons.Default.Person2.CxIcon(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
}