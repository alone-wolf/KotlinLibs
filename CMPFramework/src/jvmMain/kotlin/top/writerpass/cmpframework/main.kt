package top.writerpass.cmpframework

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import top.writerpass.cmpframework.page.Framework
import top.writerpass.cmpframework.page.IMainPages
import top.writerpass.cmpframework.page.IPages
import top.writerpass.cmpframework.page.MainPage
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.Text

object OtherPages : IPages {
    val loginPage = Page(
        route = "login",
        content = { FullSizeRow { "Login".Text() } }
    )

    val registerPage = Page(
        route = "register",
        content = { FullSizeRow { "Register".Text() } }
    )

    override val pages: List<Page> = listOf(
        loginPage,
        registerPage
    )
}

object MainPages : IMainPages {
    val homePage = MainPage(
        route = "home",
        label = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        content = { it ->
            FullSizeRow {
                "Home".Text()
                val navController = LocalNavController.current
                "LoginPage".OutlinedButton {
                    navController.navigate("login")
                }
            }
        }
    )

    val listPage = MainPage(
        route = "list",
        label = "List",
        icon = Icons.AutoMirrored.Filled.List,
        selectedIcon = Icons.AutoMirrored.Outlined.List,
        content = { FullSizeRow { "List".Text() } }
    )

    val profilePage = MainPage(
        route = "profile",
        label = "Profile",
        icon = Icons.AutoMirrored.Outlined.Article,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        content = { FullSizeRow { "Profile".Text() } }
    )

    override val pages: List<MainPage> = listOf(
        homePage, listPage, profilePage
    )

}

fun main() = application {
    Window(
        title = "CMPFramework",
        onCloseRequest = ::exitApplication,
        content = {
            Framework(
                startDestination = "home",
                pages = OtherPages,
                mainPages = MainPages
            )
        }
    )
}