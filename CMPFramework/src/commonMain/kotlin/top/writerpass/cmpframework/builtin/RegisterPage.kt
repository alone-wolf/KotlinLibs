package top.writerpass.cmpframework.builtin

import androidx.compose.runtime.staticCompositionLocalOf
import top.writerpass.cmpframework.LocalNavControllerWrapper
import top.writerpass.cmpframework.navigation.back
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxOutlinedButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText

internal val registerPage = Page(
    route = "register",
    label = "Register",
    showBackButton = true,
    showTopAppBar = true,
    content = {
        FullSizeColumn {
            "Register".CxText()
            val navController = LocalNavControllerWrapper.current
            "Back".CxOutlinedButton {
                navController.back()
            }
        }
    },
)

interface RegisterManager {
    suspend fun register(username: String, password: String): Boolean
}

val LocalRegisterManager = staticCompositionLocalOf<RegisterManager> {
    error("No RegisterManager provided")
}