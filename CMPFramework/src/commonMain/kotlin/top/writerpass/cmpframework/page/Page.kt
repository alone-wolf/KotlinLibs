package top.writerpass.cmpframework.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.Text

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavHostController found")
}

open class Page(
    open val route: String,
    open val label: String? = null,
    open val content: @Composable (NavBackStackEntry) -> Unit
)

class MainPage(
    override val route: String,
    override val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    override val content: @Composable (NavBackStackEntry) -> Unit
) : Page(route = route, label = label, content = content)

interface IPages {
    val pages: List<Page>
}

interface IMainPages {
    val pages: List<MainPage>
    val routes: List<String>
        get() = pages.map { it.route }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Framework(
    startDestination: String,
    pages: IPages,
    mainPages: IMainPages
) {
    val navController = rememberNavController()
    val mainRoutes = remember { mainPages.routes }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            // 只在 Main 区域显示 BottomBar
            if (currentRoute in mainRoutes) {
                NavigationBar {
                    mainPages.pages.forEach { p ->
                        val selected = p.route == currentRoute
                        NavigationBarItem(
                            icon = {
                                if (selected) {
                                    p.selectedIcon.Icon()
                                } else {
                                    p.icon.Icon()
                                }
                            },
                            label = { p.label.Text() },
                            selected = selected,
                            onClick = {
                                navController.navigate(p.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    "CMPFramework".Text()
                },
                navigationIcon = {
                    if (!mainRoutes.contains(currentRoute)) {
                        Icons.Default.ArrowBack.IconButton {
                            navController.popBackStack()
                        }
                    }
                },
                actions = {},
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = startDestination,
        ) {

            pages.pages.forEach { p ->
                composable(route = p.route) { it ->
                    p.content(it)
                }
            }

            mainPages.pages.forEach { p ->
                composable(route = p.route) { it ->
                    p.content(it)
                }
            }
        }
    }
}