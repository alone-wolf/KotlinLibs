package top.writerpass.micromessage.client.windows

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.window.core.layout.WindowSizeClass
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.AppMainWindowNavHost
import top.writerpass.micromessage.client.ApplicationState
import top.writerpass.micromessage.client.LocalCurrentPage
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.LocalSnackbarHostState
import top.writerpass.micromessage.client.Pages
import top.writerpass.micromessage.client.pages.base.IMainPage
import top.writerpass.micromessage.client.pages.global.LoginPage

object AdaptiveWindow {
    sealed interface X {
        object Compact : X
        object Medium : X
        object Large : X
    }

    sealed interface Y {
        object Compact : Y
        object Medium : Y
        object Large : Y
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainWindow() {
    val currentPage = LocalCurrentPage.current
    val snackbarHostState = LocalSnackbarHostState.current
    Window(
        state = rememberWindowState(
            width = 400.dp,
            height = 800.dp
        ),
        onCloseRequest = { ApplicationState.showMainWindow = false },
        visible = ApplicationState.showMainWindow,
        title = "MicroMessage",
        resizable = true,
        enabled = true,
        focusable = true,
        alwaysOnTop = ApplicationState.pinMainWindowOnTop,
        content = {
            val wai = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
            val wsc = wai.windowSizeClass
            val currentWindowY = when {
                wsc.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND) -> AdaptiveWindow.Y.Large
                wsc.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND) -> AdaptiveWindow.Y.Medium
                else -> AdaptiveWindow.Y.Compact
            }

            val currentWindowX = when {
                wsc.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> AdaptiveWindow.X.Large
                wsc.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> AdaptiveWindow.X.Medium
                else -> AdaptiveWindow.X.Compact
            }
            val showTopAppBar = wsc.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
            )


            val navController = LocalNavController.current
            Scaffold(
                topBar = {
                    if (showTopAppBar) {
                        if (currentPage.showTopAppBar) {
                            TopAppBar(
                                title = currentPage.labelCompose,
                                navigationIcon = currentPage.leftTopIcon,
                                actions = currentPage.actions
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    val showBottomBar by remember(currentPage) { derivedStateOf { currentPage is IMainPage } }
                    if (showBottomBar) {
                        NavigationBar {
                            Pages.mainPages.forEach { page ->
                                if (page.showOnBottomBar) {
                                    val routeBase = page.routeBase
                                    val isSelected by remember(currentPage) {
                                        derivedStateOf {
                                            currentPage.routeBase == routeBase
                                        }
                                    }
                                    NavigationBarItem(
                                        selected = isSelected,
                                        icon = { (if (isSelected) page.iconSelected else page.icon).CxIcon() },
                                        label = { page.label.CxText() },
                                        onClick = {
                                            if (isSelected.not()) {
                                                navController.open(page)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                floatingActionButton = currentPage.fab,
                floatingActionButtonPosition = FabPosition.End,
                content = { padding ->
                    AppMainWindowNavHost(
                        navControllerWrapper = navController,
                        startDestination = LoginPage,
                        modifier = Modifier.padding(padding),
                        pages = Pages.pages
                    )
                },
            )
        }
    )
}