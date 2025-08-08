package top.writerpass.cmpframework

import top.writerpass.cmpframework.page.IPages
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeRow
import top.writerpass.cmplibrary.compose.Text

object Pages : IPages {
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