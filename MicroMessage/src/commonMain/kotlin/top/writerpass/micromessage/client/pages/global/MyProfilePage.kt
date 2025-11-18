package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.pages.base.IPageContent

object MyProfilePage : IPageContent {
    override val label: String
        get() = "Profile"
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            Icons.Default.MoreHoriz.CxIconButton { }
        }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            fun LazyListScope.infoItem(
                label: String,
                extra: @Composable () -> Unit,
                onClick: () -> Unit = {}
            ) {
                item {
                    FullWidthRow(
                        modifier = Modifier
                            .clickable(onClick = onClick)
                            .padding(vertical = 18.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            label.CxText(fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            extra()
                            Icons.Default.ArrowRight.CxIcon()
                        }
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                infoItem(
                    label = "头像",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "名字",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "性别",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "地区",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "手机号",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "微信号",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "我的二维码",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "拍一拍",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "签名",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "来电铃声",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "我的地址",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "我的发票抬头",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "微信豆",
                    extra = {},
                    onClick = {

                    }
                )
            }
        }
}
