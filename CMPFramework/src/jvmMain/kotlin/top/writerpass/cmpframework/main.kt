package top.writerpass.cmpframework

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import top.writerpass.cmpframework.page.Framework

fun main() = application {
    Window(
        title = "CMPFramework",
        onCloseRequest = ::exitApplication,
        content = {
            Framework(
                startPage = "home",
                pages = Pages,
                mainPages = MainPages
            )
        }
    )
}