package top.writerpass.micromessage.client.navigation.pages

import top.writerpass.micromessage.client.navigation.pages.global.AudioCallPage
import top.writerpass.micromessage.client.navigation.pages.global.LoginPage
import top.writerpass.micromessage.client.navigation.pages.global.MyAvatarPage
import top.writerpass.micromessage.client.navigation.pages.global.MyProfilePage
import top.writerpass.micromessage.client.navigation.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.navigation.pages.global.PrivateChatDetailPage
import top.writerpass.micromessage.client.navigation.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.navigation.pages.global.RegisterPage
import top.writerpass.micromessage.client.navigation.pages.global.ResetPasswordPage
import top.writerpass.micromessage.client.navigation.pages.global.SearchPage
import top.writerpass.micromessage.client.navigation.pages.global.SettingsPage
import top.writerpass.micromessage.client.navigation.pages.global.UserProfilePage
import top.writerpass.micromessage.client.navigation.pages.global.VideoCallPage
import top.writerpass.micromessage.client.navigation.pages.main.ContactPage
import top.writerpass.micromessage.client.navigation.pages.main.ExplorerPage
import top.writerpass.micromessage.client.navigation.pages.main.MePage
import top.writerpass.micromessage.client.navigation.pages.main.MessagePage

object Pages {

    /** 主 Tab
     *    - 消息列表 v
     *    - 联系人 v
     *    - 发现 v
     *    - 我的 v¸
     */
    val mainPages = listOf(
        MessagePage,
        ContactPage,
        ExplorerPage,
        MePage
    )
    val globalPages = listOf(
        // 1.登录/注册/重置密码
        LoginPage,
        RegisterPage,
        ResetPasswordPage,

        // 3.聊天界面
        PrivateChatPage,
        // GroupChatPage
        PrivateChatDetailPage,


        UserProfilePage,


        MyProfilePage,
        MyQrCodePage,
        MyAvatarPage,


        SearchPage,
        SettingsPage,
        AudioCallPage,
        VideoCallPage
    )

    val pages = mainPages + globalPages
    val pageRouteMap = pages.associateBy { it.routeBase }
}