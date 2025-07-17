package top.writerpass.cmplibrary

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(
            title = "Test",
            onCloseRequest = ::exitApplication,
            content = {

                CatScreen()
            }
        )
    }
}