package top.writerpass.micromessage.client.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import top.writerpass.micromessage.client.pages.base.IMainPageContent

object Explorer : IMainPageContent {
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.Article
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.Article
    override val label: String
        get() = "Explorer"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {}
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {}
}