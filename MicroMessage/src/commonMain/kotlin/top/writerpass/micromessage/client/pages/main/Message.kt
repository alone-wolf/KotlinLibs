package top.writerpass.micromessage.client.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.ApplicationState
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.pages.base.MainPage
import top.writerpass.micromessage.client.pages.base.Page
import top.writerpass.micromessage.client.pages.global.ChatDetailPage
import top.writerpass.micromessage.client.pages.global.SearchPage

fun NavHostController.open(page: Page, vararg args: String) {
    val newRouteBuilder = StringBuilder(page.route)
    if (args.isNotEmpty()) {
        args.forEach { a ->
            newRouteBuilder.append("/").append(a)
        }
    }
    navigate(newRouteBuilder.toString())
}

object Message : MainPage {
    override val route: String
        get() = "message-page"
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.Message
    override val iconSelected: ImageVector
        get() = Icons.AutoMirrored.Filled.Message
    override val label: String
        get() = "Message"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            if (ApplicationState.pingOnTop) {
                Icons.Filled.PushPin.CxIconButton {
                    ApplicationState.pingOnTop = false
                }
            } else {
                Icons.Outlined.PushPin.CxIconButton {
                    ApplicationState.pingOnTop = true
                }
            }
            Icons.Default.AddCircleOutline.CxIconButton {}
        }
    override val fab: @Composable (() -> Unit)
        get() = {
            val navController = LocalNavController.current
            FloatingActionButton(
                onClick = {
                    navController.navigate(SearchPage.route)
                },
                content = {
                    Icons.Default.Search.CxIcon()
                }
            )
        }
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val navController = LocalNavController.current
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(100) { index ->
                    FullWidthRow(
                        modifier = Modifier.clickable {
                            navController.navigate("${ChatDetailPage.route}/${index}")
                        }.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icons.Default.Person2.CxIcon(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            "用户${index}用户${index}用户${index}用户${index}".CxText(
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                            "[18] 这是一条测试消息".CxText(
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
}