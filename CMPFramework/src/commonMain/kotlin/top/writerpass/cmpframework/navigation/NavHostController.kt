package top.writerpass.cmpframework.navigation

import androidx.navigation.NavHostController
import top.writerpass.cmpframework.page.MainPage
import top.writerpass.cmpframework.page.Page

val NavHostController.wrapper get() = NavControllerWrapper(this)

class NavControllerWrapper(val controller: NavHostController)

fun NavControllerWrapper.gotoMainPage(page: MainPage) {
    controller.navigate(page.route) {
        controller.graph.startDestinationRoute?.let { route ->
            popUpTo(route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}


fun NavControllerWrapper.gotoPage(page: Page) {
    controller.navigate(page.route)
}

fun NavControllerWrapper.back() {
    controller.popBackStack()
}