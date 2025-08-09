package top.writerpass.cmpframework.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry

class MainPage(
    override val route: String,
    override val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    override val showBackButton: Boolean = false,
    override val showTopAppBar: Boolean = true,
    val hideInMore: Boolean = false,
    override val content: @Composable (NavBackStackEntry) -> Unit
) : Page(
    route = route,
    showBackButton = showBackButton,
    showTopAppBar = showTopAppBar,
    label = label,
    content = content
)
