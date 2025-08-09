package top.writerpass.cmpframework

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import top.writerpass.cmpframework.builtin.LocalLoginManager
import top.writerpass.cmpframework.builtin.LoginManager
import top.writerpass.cmpframework.navigation.gotoPage
import top.writerpass.cmpframework.page.LocalNavController

fun main() = application {
    Window(
        title = "CMPFramework",
        onCloseRequest = ::exitApplication,
        content = {
            val navController = rememberNavController()
            val loginManager = object : LoginManager {
                override suspend fun check(
                    username: String,
                    password: String
                ): Boolean {
                    return true
                }

                override suspend fun login(
                    username: String,
                    password: String
                ): Boolean {
                    return true
                }

                override fun leaveLoginPage() {
                    navController.gotoPage(MainPages.homePage)
                }

                override fun gotoRegister() {
                    navController.gotoPage(Pages.registerPage)
                }

            }
            CompositionLocalProvider(
                LocalLoginManager provides loginManager,
                LocalNavController provides navController
            ) {
                Framework(
                    startPage = Pages.loginPage,
                    pages = Pages,
                    mainPages = MainPages
                )
            }
        }
    )
}