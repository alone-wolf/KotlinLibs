package top.writerpass.micromessage.client.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.micromessage.client.pages.base.MainPages

object Message : MainPages {
    override val routeRaw: String
        get() = route
    override val route: String
        get() = "message-page"
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.Message
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.Message
    override val label: String
        get() = "Message"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = { Icons.Default.AddCircleOutline.CxIconButton {} }
    override val fab: @Composable (() -> Unit)
        get() = {
            FloatingActionButton(
                onClick = {},
                content = {
                    Icons.Default.Search.CxIcon()
                }
            )
        }
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {}
}