package top.writerpass.cmpframework.sample

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import top.writerpass.cmpframework.LocalNavControllerWrapper
import top.writerpass.cmpframework.navigation.gotoPage
import top.writerpass.cmpframework.page.IMainPages
import top.writerpass.cmpframework.page.MainPage
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxOutlinedButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText

internal object MainPages : IMainPages {

    val homePage = MainPage(
        route = "home",
        label = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        content = { it ->
            FullSizeRow {
                "Home".CxText()
                val navController = LocalNavControllerWrapper.current
                "LoginPage".CxOutlinedButton {
                    navController.gotoPage(Pages.loginPage)
                }
            }
        }
    )

    val listPage = MainPage(
        route = "list",
        label = "List",
        icon = Icons.AutoMirrored.Filled.List,
        selectedIcon = Icons.AutoMirrored.Outlined.List,
        content = { FullSizeRow { "List".CxText() } }
    )

    val profilePage = MainPage(
        route = "profile",
        label = "Profile",
        icon = Icons.AutoMirrored.Outlined.Article,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        content = { FullSizeRow { "Profile".CxText() } }
    )

    val settingsPage = MainPage(
        route = "settings",
        label = "Settings",
        icon = Icons.AutoMirrored.Outlined.Article,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        hideInMore = true,
        content = { FullSizeRow { "Settings".CxText() } }
    )

    val settingsPage1 = MainPage(
        route = "settings1",
        label = "Settings1",
        icon = Icons.AutoMirrored.Outlined.Article,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        hideInMore = true,
        content = { FullSizeRow { "Settings1".CxText() } }
    )

    val settingsPage2 = MainPage(
        route = "settings2",
        label = "Settings2",
        icon = Icons.AutoMirrored.Outlined.Article,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        hideInMore = true,
        content = { FullSizeRow { "Settings2".CxText() } }
    )

    override val pages: List<MainPage> = listOf(
        homePage, listPage, profilePage, settingsPage, settingsPage1, settingsPage2
    )

}