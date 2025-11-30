package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IPage

object AudioCallPage : IPage {
    override val routeBase: String
        get() = "call-audio"
    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("userId") {
                type = NavType.LongType
                defaultValue = -1L
            })
    override val label: String
        get() = "Call Audio"

    override val showTopAppBar: Boolean
        get() = false

    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val navController = LocalNavController.current
            FullSizeBox {
                Icons.Default.Minimize.CxIconButton(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {

                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icons.Default.Person.CxIcon(
                        modifier = Modifier.border(2.dp, Color.Black).size(60.dp)
                            .background(Color.Gray)
                    )
                    "Username/Remark".CxText()
                    Spacer(modifier = Modifier.height(60.dp))
                    "Calling...".CxText()
                }

                FullWidthRow(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icons.Default.Android.CxIconButton { }
                    Icons.Default.CallEnd.CxIconButton {
                        navController.popBackStack()
                    }
                    Icons.Default.Audiotrack.CxIconButton { }
                }
            }
        }
}