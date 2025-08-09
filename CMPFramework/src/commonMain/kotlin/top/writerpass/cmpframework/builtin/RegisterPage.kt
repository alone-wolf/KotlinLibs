package top.writerpass.cmpframework.builtin

import top.writerpass.cmpframework.page.LocalNavController
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.Text

internal val registerPage = Page(
    route = "register",
    showBackButton = true,
    hideTopAppBar = true,
    content = {
        FullSizeColumn {
            "Register".Text()
            val navController = LocalNavController.current
            "Back".OutlinedButton {
                navController.popBackStack()
            }
        }
    },
)