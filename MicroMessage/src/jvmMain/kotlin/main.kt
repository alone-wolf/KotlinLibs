import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.ApplicationState
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.Singleton
import top.writerpass.micromessage.client.pages.base.IMainPage
import top.writerpass.micromessage.client.pages.main.MessagePage
import top.writerpass.micromessage.client.rememberNavControllerWrapper


@Composable
fun BoxScope.PlatformScrollBar(
    lazyListState: LazyListState
) {

}

@OptIn(ExperimentalMaterial3Api::class)
fun main() {


    val viewModelStoreOwner = object : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore = ViewModelStore()
    }
    application {
        val mainWindowState = rememberWindowState(
            width = 400.dp,
            height = 800.dp
        )

        LaunchedEffect(ApplicationState.showMainWindow) {
            if (ApplicationState.showMainWindow.not()) {
                ::exitApplication.invoke()
            }
        }

        val navController = rememberNavControllerWrapper()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRouteBase by remember {
            derivedStateOf {
                (navBackStackEntry?.destination?.route ?: MessagePage.routeBase)
                    .split("/").first()
                    .also { println("currentRoute: $it") }
            }
        }
        val currentPage by remember {
            derivedStateOf {
                Singleton.pageRouteMap[currentRouteBase]!!
            }
        }


        Window(
            state = mainWindowState,
            onCloseRequest = { ApplicationState.showMainWindow = false },
            visible = ApplicationState.showMainWindow,
            title = "MicroMessage",
            resizable = true,
            enabled = true,
            focusable = true,
            alwaysOnTop = ApplicationState.pingOnTop,
            content = {
                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalViewModelStoreOwner provides viewModelStoreOwner
                ) {
                    val navController = LocalNavController.current
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = currentPage.labelCompose,
                                navigationIcon = currentPage.leftTopIcon,
                                actions = currentPage.actions
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            val showBottomBar by remember {
                                derivedStateOf { currentPage is IMainPage }
                            }
                            if (showBottomBar) {
                                NavigationBar {
                                    Singleton.mainRouteMap.forEach { (routeBase, page) ->
                                        val isSelected by remember(currentPage) {
                                            derivedStateOf {
                                                currentRouteBase == routeBase
                                            }
                                        }
                                        NavigationBarItem(
                                            selected = isSelected,
                                            icon = { (if (isSelected) page.iconSelected else page.icon).CxIcon() },
                                            label = { page.label.CxText() },
                                            onClick = { navController.open(page) }
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
                        floatingActionButton = currentPage.fab,
                        floatingActionButtonPosition = FabPosition.End,
                        content = { padding ->
                            NavHost(
                                navController = navController.c,
                                startDestination = MessagePage.routeBase,
                                modifier = Modifier.padding(padding)
                            ) {
                                Singleton.pages.forEach { page ->
                                    noAnimeComposable(
                                        routeTemplate = page.routeTemplate,
                                        arguments = page.arguments,
                                        deepLinks = page.deepLink,
                                        content = page.content
                                    )
                                }
                            }
                        },
                    )
                }
            }
        )
    }
}

private fun NavGraphBuilder.noAnimeComposable(
    routeTemplate: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable(
    route = routeTemplate,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
    content = content
)