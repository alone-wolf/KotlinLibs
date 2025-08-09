package top.writerpass.cmpframework.navigation

import androidx.navigation.NavHostController
import top.writerpass.cmpframework.page.MainPage
import top.writerpass.cmpframework.page.Page

fun NavHostController.gotoMainPage(page: MainPage) {
    navigate(page.route) {
        graph.startDestinationRoute?.let { route ->
            popUpTo(route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}


fun NavHostController.gotoPage(page: Page) {
    navigate(page.route)
}