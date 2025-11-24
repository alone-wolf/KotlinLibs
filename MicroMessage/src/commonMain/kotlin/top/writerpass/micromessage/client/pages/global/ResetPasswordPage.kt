package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.pages.base.IPage

object ResetPasswordPage : IPage {
    override val routeBase: String
        get() = "reset-password"
    override val label: String
        get() = "Reset Password"
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            "Reset Password".CxText()
        }
}