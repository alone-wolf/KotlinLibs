package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.navigation.pages.base.IPage

object VideoCallPage : IPage {
    override val routeBase: String
        get() = "call-video"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("userId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    override val label: String
        get() = "Video Call"
    override val showTopAppBar: Boolean
        get() = false
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            FullSizeBox {
                "Video Call".CxText(modifier = Modifier.align(Alignment.Center))
            }
        }
}