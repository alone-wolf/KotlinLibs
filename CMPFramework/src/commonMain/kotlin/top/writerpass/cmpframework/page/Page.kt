package top.writerpass.cmpframework.page

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry


open class Page(
    open val route: String,
    open val label: String? = null,
    open val showBackButton: Boolean = true,
    open val content: @Composable (NavBackStackEntry) -> Unit
)
