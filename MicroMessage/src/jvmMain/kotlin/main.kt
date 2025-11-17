import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.Singleton
import top.writerpass.micromessage.client.pages.base.MainPages
import top.writerpass.micromessage.client.pages.main.Message


@OptIn(ExperimentalMaterial3Api::class)
fun main(){



    singleWindowApplication(
        title = "MicroMessage",
    ) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentPage by remember {
            derivedStateOf {
                navBackStackEntry
                    ?.destination
                    ?.route
                    ?.split("/")
                    ?.first()
                    .let { Singleton.pageMap[it] ?: Message }
            }
        }
        val showBottomBar by remember {
            derivedStateOf { currentPage is MainPages }
        }
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            val navController = LocalNavController.current
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { currentPage.label.CxText() },
                        navigationIcon = { currentPage.leftTopIcon() },
                        actions = { currentPage.actions(this) }
                    )
                },
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (showBottomBar) {
                        NavigationBar {
                            Singleton.mainPages.forEach { page ->
                                val isSelected by remember(currentPage) {
                                    derivedStateOf {
                                        currentPage == page
                                    }
                                }
                                NavigationBarItem(
                                    selected = isSelected,
                                    icon = { (if (isSelected) page.iconSelected else page.icon).CxIcon() },
                                    label = { page.label.CxText() },
                                    onClick = {
                                        navController.navigate(page.route)
                                    }
                                )
                            }
                        }
                    }
                },
                snackbarHost = {
                    val snackbarHostState = remember { SnackbarHostState() }
                    SnackbarHost(
                        hostState = snackbarHostState,
                        snackbar = {}
                    )
                },
                floatingActionButton = {
                    currentPage.fab()
                },
                floatingActionButtonPosition = FabPosition.End,
                content = { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = Message.route,
                        modifier = Modifier.padding(padding)
                    ) {
                        Singleton.pageMap.forEach { (route, page) ->
                            composable(
                                route = route,
                                arguments = emptyList(),
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None },
                            ) { page.content(this, it) }
                        }
                    }
                },
            )
        }
    }
}