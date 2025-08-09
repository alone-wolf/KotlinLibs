package top.writerpass.cmpframework

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import top.writerpass.cmpframework.builtin.LocalLoginManager
import top.writerpass.cmpframework.builtin.LoginManager
import top.writerpass.cmpframework.navigation.gotoPage
import top.writerpass.cmpframework.navigation.wrapper
import top.writerpass.cmpframework.page.LocalNavControllerWrapper

fun main() = application {
    Window(
        title = "CMPFramework",
        onCloseRequest = ::exitApplication,
        content = {
            val navController = rememberNavController()
            val navControllerWrapper = remember { navController.wrapper }
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
                    navControllerWrapper.gotoPage(MainPages.homePage)
                }

                override fun gotoRegister() {
                    navControllerWrapper.gotoPage(Pages.registerPage)
                }

            }
            CompositionLocalProvider(
                LocalLoginManager provides loginManager,
                LocalNavControllerWrapper provides navControllerWrapper
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