package top.writerpass.cmpframework.navigation

import androidx.navigation.NavHostController
import top.writerpass.cmpframework.page.MainPage

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