package top.writerpass.micromessage.client

import top.writerpass.micromessage.client.pages.global.ChatDetailPage
import top.writerpass.micromessage.client.pages.global.MyProfilePage
import top.writerpass.micromessage.client.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.pages.global.SearchPage
import top.writerpass.micromessage.client.pages.global.UserAvatarPage
import top.writerpass.micromessage.client.pages.main.ContactPage
import top.writerpass.micromessage.client.pages.main.ExplorerPage
import top.writerpass.micromessage.client.pages.main.MePage
import top.writerpass.micromessage.client.pages.main.MessagePage

object Singleton {

    val mainPages = listOf(
        MessagePage,
        ContactPage,
        ExplorerPage,
        MePage
    )
    val globalPages = listOf(
        UserAvatarPage,
        PrivateChatPage,
        MyProfilePage,
        MyQrCodePage,
        ChatDetailPage,
        SearchPage
    )

    val pages = mainPages + globalPages

    val mainRouteMap = mainPages.associateBy { it.routeBase }
    val globalRouteMap = globalPages.associateBy { it.routeBase }
    val pageRouteMap = pages.associateBy { it.routeBase }

//    val mainPageMap = mapOf(
//        MessagePage::class.qualifiedName!! to MessagePage,
//        ContactPage::class.qualifiedName!! to ContactPage,
//        ExplorerPage::class.qualifiedName!! to ExplorerPage,
//        MePage::class.qualifiedName!! to MePage,
//    )
//    val globalPageMap = mapOf(
//        UserAvatarPage::class.qualifiedName!! to UserAvatarPage,
//        PrivateChatPage::class.qualifiedName!! to PrivateChatPage,
//        MyProfilePage::class.qualifiedName!! to MyProfilePage,
//        MyQrCodePage::class.qualifiedName!! to MyQrCodePage,
//        ChatDetailPage::class.qualifiedName!! to ChatDetailPage,
//        SearchPage::class.qualifiedName!! to SearchPage
//    )
//
//    val pageRouteContentMap = mainPageMap + globalPageMap
}