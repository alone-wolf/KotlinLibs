package top.writerpass.micromessage.client

import top.writerpass.micromessage.client.pages.global.ChatDetailPage
import top.writerpass.micromessage.client.pages.global.LoginPage
import top.writerpass.micromessage.client.pages.global.MyProfilePage
import top.writerpass.micromessage.client.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.pages.global.RegisterPage
import top.writerpass.micromessage.client.pages.global.ResetPasswordPage
import top.writerpass.micromessage.client.pages.global.SearchPage
import top.writerpass.micromessage.client.pages.global.SettingsPage
import top.writerpass.micromessage.client.pages.global.UserAvatarPage
import top.writerpass.micromessage.client.pages.global.UserProfilePage
import top.writerpass.micromessage.client.pages.main.ContactPage
import top.writerpass.micromessage.client.pages.main.ExplorerPage
import top.writerpass.micromessage.client.pages.main.MePage
import top.writerpass.micromessage.client.pages.main.MessagePage

// MainPages:
// Message
// Contact
// Explorer
// Me

// GlobalPages:

// PrivateChatPage v
// GroupChatPage
// GlobalChannelPage
// UserProfilePage v
// GroupProfilePage
// GroupWizardPage
// QRCodeScannerPage
// NewFriendPage
// FriendRequestPage
// SettingPage v
// MyProfilePage v

object Pages {

    val mainPages = listOf(
        MessagePage,
        ContactPage,
        ExplorerPage,
        MePage
    )
    val globalPages = listOf(
        // login&misc
        LoginPage,
        RegisterPage,
        ResetPasswordPage,

        UserAvatarPage,
        PrivateChatPage,
        MyProfilePage,
        MyQrCodePage,
        ChatDetailPage,
        SearchPage,
        UserProfilePage,
        SettingsPage
    )

    val pages = mainPages + globalPages
    val pageRouteMap = pages.associateBy { it.routeBase }
}