package top.writerpass.cmpframework.page

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry

open class MainPage(
    override val route: String,
    override val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    override val showBackButton: Boolean = false,
    override val showTopAppBar: Boolean = true,
    override val actions:  @Composable RowScope.() -> Unit = {},
    val hideInMore: Boolean = false,
    override val content: @Composable (NavBackStackEntry) -> Unit
) : Page(
    route = route,
    showBackButton = showBackButton,
    showTopAppBar = showTopAppBar,
    actions = actions,
    label = label,
    content = content
)
