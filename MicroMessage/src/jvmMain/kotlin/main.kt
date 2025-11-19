import androidx.compose.animation.AnimatedContentScope
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
import top.writerpass.micromessage.client.pages.global.ChatDetailPage
import top.writerpass.micromessage.client.pages.global.MyProfilePage
import top.writerpass.micromessage.client.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.pages.global.SearchPage
import top.writerpass.micromessage.client.pages.global.UserAvatarPage
import top.writerpass.micromessage.client.pages.main.ContactPage
import top.writerpass.micromessage.client.pages.main.ExplorerPage
import top.writerpass.micromessage.client.pages.main.MePageContent
import top.writerpass.micromessage.client.pages.main.MessagePage
import top.writerpass.micromessage.client.rememberNavControllerWrapper


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
        val navBackStackEntry by navController.c.currentBackStackEntryAsState()

        LaunchedEffect(navBackStackEntry) {
            val r = navBackStackEntry?.destination?.route ?: MessagePage::class.qualifiedName!!
            val rr = r.split("/").first()

            println("currentRoute: $r $rr")
        }
        val currentRoute by remember {
            derivedStateOf {
                navBackStackEntry
                    ?.destination
                    ?.route
                    ?: MessagePage::class.qualifiedName!!
                        .split("/")
                        .first()
                        .also { println("currentRoute: $it") }
            }
        }
        val currentPage by remember {
            derivedStateOf {
                Singleton.pageRouteMap[currentRoute]!!
            }
        }
        val currentPageContent by remember {
            derivedStateOf {
                Singleton.pageRouteContentMap[currentRoute]!!
            }
        }
        val showBottomBar by remember {
            derivedStateOf { currentPage is IMainPage }
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
                                title = { currentPageContent.label.CxText() },
                                navigationIcon = { currentPageContent.leftTopIcon() },
                                actions = { currentPageContent.actions(this) }
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (showBottomBar) {
                                NavigationBar {
                                    Singleton.mainPageMap.forEach { (pageRoute, pageContent) ->
                                        val isSelected by remember(currentPageContent) {
                                            derivedStateOf {
                                                currentRoute == pageRoute
                                            }
                                        }
                                        NavigationBarItem(
                                            selected = isSelected,
                                            icon = { (if (isSelected) pageContent.iconSelected else pageContent.icon).CxIcon() },
                                            label = { pageContent.label.CxText() },
                                            onClick = { navController.open(pageRoute) }
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
                            currentPageContent.fab()
                        },
                        floatingActionButtonPosition = FabPosition.End,
                        content = { padding ->
                            NavHost(
                                navController = navController,
                                startDestination = MessagePage,
                                modifier = Modifier.padding(padding)
                            ) {
                                noAnimeComposable<MessagePage>(content = MessagePage.content)
                                noAnimeComposable<ContactPage>(content = ContactPage.content)
                                noAnimeComposable<ExplorerPage>(content = ExplorerPage.content)
                                noAnimeComposable<MePage>(content = MePageContent.content)
                                noAnimeComposable<UserAvatarPage>(content = UserAvatarPage.content)
                                noAnimeComposable<PrivateChatPage>(content = PrivateChatPage.content)
                                noAnimeComposable<PrivateChatPage>(content = PrivateChatPage.content)
                                noAnimeComposable<MyProfilePage>(content = MyProfilePage.content)
                                noAnimeComposable<MyQRCodePage>(content = MyQrCodePage.content)
                                noAnimeComposable<ChatDetailPage>(content = ChatDetailPage.content)
                                noAnimeComposable<SearchPage>(content = SearchPage.content)
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
    arguments = emptyList(),
    deepLinks = emptyList(),
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
    content = content
)