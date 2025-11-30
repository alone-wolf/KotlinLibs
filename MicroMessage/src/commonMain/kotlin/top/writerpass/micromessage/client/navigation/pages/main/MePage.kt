package top.writerpass.micromessage.client.navigation.pages.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IMainPage
import top.writerpass.micromessage.client.navigation.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.navigation.pages.global.SettingsPage


object MePage : IMainPage {
    override val icon: ImageVector
        get() = Icons.Outlined.Person
    override val iconSelected: ImageVector
        get() = Icons.Filled.Person
    override val routeBase: String
        get() = "me"
    override val label: String
        get() = "Me"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            val navController = LocalNavController.current
            Icons.Default.QrCode2.CxIconButton {
                navController.open(MyQrCodePage)
            }
        }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            val navController = LocalNavController.current
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    FullWidthRow(
                        modifier = Modifier
                            .background(Color.Yellow)
                            .clickable {}
                            .padding(vertical = 20.dp)
                            .padding(horizontal = 26.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icons.Default.Person.CxIcon(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Red)
                        )
                        Column {
                            "User's Nickname".CxText(fontSize = 18.sp)
                            "Username: aaaa".CxText()
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                fun LazyListScope.aa(
                    label: String,
                    icon: ImageVector,
                    onClick: () -> Unit
                ) {
                    item {
                        FullWidthRow(
                            modifier = Modifier
                                .height(48.dp)
                                .clickable(onClick = onClick)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            icon.CxIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                            label.CxText()
                        }
                    }
                }

                aa(
                    "Settings",
                    Icons.Default.Settings
                ) {
                    navController.open(SettingsPage)
                }
                aa(
                    "Logout",
                    Icons.AutoMirrored.Filled.Logout
                ) {
                    navController.logout()
                }
            }
        }
}