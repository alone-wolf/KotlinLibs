package top.writerpass.cmpframework

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import top.writerpass.cmpframework.navigation.back
import top.writerpass.cmpframework.navigation.gotoMainPage
import top.writerpass.cmpframework.page.IMainPages
import top.writerpass.cmpframework.page.IPages
import top.writerpass.cmpframework.page.MainPage
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Framework(
    appName: String = "CMPFramework",
    startPage: Page,
    pages: IPages?,
    mainPages: IMainPages?
) {
    val navController = LocalNavControllerWrapper.current

    val navBackStackEntry by navController.controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentPage by remember(currentRoute) {
        derivedStateOf {
            pages?.pages?.find { it.route == currentRoute }
                ?: mainPages?.pages?.find { it.route == currentRoute }
        }
    }

    val currentPageLabel by remember(currentPage) {
        derivedStateOf {
            when (currentPage) {
                null -> appName
                is MainPage -> (currentPage as MainPage).label
                else -> (currentPage as Page).label ?: appName
            }
        }
    }

    val showBottomBar by remember(currentPage) {
        derivedStateOf {
            currentPage?.let { it is MainPage } ?: false && (mainPages?.pages?.size ?: 0) > 1
        }
    }

    CompositionLocalProvider {
        Scaffold(
            bottomBar = {
                // 只在 Main 区域显示 BottomBar
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
                ) {
                    NavigationBar {
                        mainPages?.pagesShow?.forEach { p ->
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
                                    navController.gotoMainPage(p)
                                }
                            )
                        }
                        if (mainPages?.hasMore ?: false) {
                            val selected = Mutable.SomeBoolean(false)
                            this@NavigationBar.NavigationBarItem(
                                icon = {
                                    if (selected.value) {
                                        Icons.Filled.Close.Icon()
                                    } else {
                                        Icons.Outlined.MoreHoriz.Icon()
                                    }
                                },
                                label = { "More".Text() },
                                selected = selected.value,
                                onClick = {
                                    selected.value = !selected.value
                                }
                            )
                            Box {
                                DropdownMenu(
                                    expanded = selected.value,
                                    onDismissRequest = { selected.value = false }
                                ) {
                                    mainPages.pagesInMore.forEach { p ->
                                        DropdownMenuItem(
                                            text = { p.label.Text() },
                                            leadingIcon = { p.icon.Icon() },
                                            onClick = {
                                                selected.value = false
                                                navController.gotoMainPage(p)
                                            }
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            },
            topBar = {
                if (currentPage?.showTopAppBar ?: true) {
                    TopAppBar(
                        title = {
                            currentPageLabel.Text()
                        },
                        navigationIcon = {
                            AnimatedVisibility(
                                visible = currentPage?.showBackButton ?: true,
                                enter = fadeIn() + slideInHorizontally() + expandHorizontally(),
                                exit = fadeOut() + slideOutHorizontally() + shrinkHorizontally(),
                            ) {
                                Icons.AutoMirrored.Filled.ArrowBack.IconButton {
                                    navController.back()
                                }
                            }
                        },
                        actions = {},
                    )
                }

            }
        ) { innerPadding ->
            NavHost(
                navController = navController.controller,
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                startDestination = startPage.route,
            ) {

                pages?.pages?.forEach { p ->
                    composable(route = p.route) { it ->
                        p.content(it)
                    }
                }

                mainPages?.pages?.forEach { p ->
                    composable(route = p.route) { it ->
                        p.content(it)
                    }
                }
            }
        }
    }
}
