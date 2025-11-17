package top.writerpass.micromessage.client.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.micromessage.client.pages.base.MainPages

object Contact : MainPages {
    override val routeRaw: String
        get() = route
    override val route: String
        get() = "contact-page"
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.List
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.List
    override val label: String
        get() = "Contact"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = { Icons.Default.AddCircle.CxIconButton { } }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {}
}