package top.writerpass.cmpframework

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "CMPFramework",
        onCloseRequest = ::exitApplication,
        content = {
            Framework(
                startPage = Pages.loginPage,
                pages = Pages,
                mainPages = MainPages
            )
        }
    )
}