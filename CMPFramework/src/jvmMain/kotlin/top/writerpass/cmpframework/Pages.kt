package top.writerpass.cmpframework

import top.writerpass.cmpframework.builtin.BuiltinPages
import top.writerpass.cmpframework.page.IPages
import top.writerpass.cmpframework.page.LocalNavController
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.Text

object Pages : IPages {
    val loginPage = BuiltinPages.loginPage
    val registerPage = BuiltinPages.registerPage

    override val pages: List<Page> = listOf(
        loginPage,
        registerPage
    )
}