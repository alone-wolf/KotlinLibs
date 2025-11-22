@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Voicemail
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.KeyboardVoice
import androidx.compose.material.icons.outlined.Voicemail
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.pages.base.IPage
import top.writerpass.micromessage.client.viewmodels.PrivateChatMessage
import top.writerpass.micromessage.client.viewmodels.PrivateChatViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


object PrivateChatPage : IPage {
    override val routeBase: String
        get() = "private-chat"
    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("chatId") {
                type = NavType.LongType
                defaultValue = -1L
            })
    override val label: String
        get() = "Private Chat"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {
            val navController = LocalNavController.current
            Icons.Default.ArrowBackIosNew.CxIconButton {
                navController.popBackStack()
            }
        }
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {
            val navController = LocalNavController.current
            Icons.Default.Person.CxIconButton {
                navController.open(UserProfilePage, 100L)
            }
        }
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = { navBackStackEntry ->
            val chatId = navBackStackEntry.arguments?.read {
                getLong("chatId")
            } ?: -1L
            val privateChatViewModel = viewModel(key = chatId.toString()) {
                PrivateChatViewModel(chatId = chatId)
            }

            if (chatId == -1L) {
                FullSizeBox {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        "Something wrong:".CxText()
                        "chatId == -1".CxText()
                    }
                }
            } else {
                FullSizeColumn {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        reverseLayout = false,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        items(
                            items = privateChatViewModel.chatMessages,
                            key = { it },
                            contentType = { "PrivateChatMessage" },
                            itemContent = { message ->
                                ChatBubble(message)
                            }
                        )
                    }
                    FullWidthRow(
                        modifier = Modifier.padding(top = 2.dp).background(Color.LightGray),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icons.Outlined.Voicemail.CxIconButton { }
                        TextField(
                            value = privateChatViewModel.text,
                            onValueChange = privateChatViewModel::updateText,
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                Icons.Outlined.KeyboardVoice.CxIconButton { }
                            }
                        )
                        Icons.Outlined.EmojiEmotions.CxIconButton { }
                        Icons.Outlined.AddCircle.CxIconButton { }
                        "Send".CxTextButton(
                            enabled = privateChatViewModel.textNotEmpty,
                            onClick = privateChatViewModel::sendMessage
                        )
                    }
                }
            }
        }
}


@Composable
private fun ChatBubble(
    message: PrivateChatMessage,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (message.isMe) {
        // 微信自己的绿色（近似）
        Color(0xFF95EC6A)
    } else {
        // 微信对方的浅灰
        Color(0xFFEDEDED)
    }

    // 不同方向的圆角，模仿微信“对话框尖角”效果
    val bubbleShape = if (message.isMe) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 4.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!message.isMe) {
            // 对方头像占位（微信是左侧）
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFCCCCCC), CircleShape)
            )
            Spacer(Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 260.dp) // 控制气泡最大宽度
                .background(bubbleColor, bubbleShape)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = message.content,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (message.isMe) {
            Spacer(Modifier.width(8.dp))
            // 自己头像占位（微信是右侧）
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFCCCCCC), CircleShape)
            )
        }
    }
}