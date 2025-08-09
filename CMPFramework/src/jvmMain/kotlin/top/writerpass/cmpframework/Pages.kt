package top.writerpass.cmpframework

import top.writerpass.cmpframework.page.IPages
import top.writerpass.cmpframework.page.LocalNavController
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.Text

object Pages : IPages {
    val loginPage = Page(
        route = "login",
        content = {
            FullSizeColumn {
                "Login".Text()
                val navController = LocalNavController.current
                "Login".OutlinedButton { navController.navigate("home") }
                "Register".OutlinedButton { navController.navigate("register") }
            }
        },
        showBackButton = false
    )

    val registerPage = Page(
        route = "register",
        content = { FullSizeColumn { "Register".Text() } },
        showBackButton = true
    )

    override val pages: List<Page> = listOf(
        loginPage,
        registerPage
    )
}