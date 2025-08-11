package top.writerpass.cmpframework.page

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry


open class Page(
    open val route: String,
    open val label: String? = null,
    open val showBackButton: Boolean = true,
    open val showTopAppBar: Boolean = true,
    open val actions: @Composable RowScope.() -> Unit = {},
    open val content: @Composable (NavBackStackEntry) -> Unit
)
