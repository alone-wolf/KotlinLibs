package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.pages.base.IPage

object UserProfilePage : IPage {
    override val routeBase: String
        get() = "user-profile"
    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("userId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    override val label: String
        get() = "User Profile"
    override val labelCompose: @Composable (() -> Unit)
        get() = {
            super.labelCompose.invoke()
        }
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            var userId by remember { mutableStateOf(-1L) }
            LaunchedEffectOdd {
                it.arguments?.read {
                    userId = getLong("userId")
                }
            }
            "aaa ${userId}".CxText()
        }
}