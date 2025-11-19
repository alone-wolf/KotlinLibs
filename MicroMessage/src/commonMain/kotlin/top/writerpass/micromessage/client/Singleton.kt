package top.writerpass.micromessage.client

import top.writerpass.micromessage.client.pages.global.ChatDetailPage
import top.writerpass.micromessage.client.pages.global.MyProfilePage
import top.writerpass.micromessage.client.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.pages.global.SearchPage
import top.writerpass.micromessage.client.pages.global.UserAvatarPage
import top.writerpass.micromessage.client.pages.main.ContactPage
import top.writerpass.micromessage.client.pages.main.ExplorerPage
import top.writerpass.micromessage.client.pages.main.MePageContent
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
        MyQRCodePage,
        ChatDetailPage,
        SearchPage
    )
    
    val pages = mainPages + globalPages
    
    val pageRouteMap = pages.associateBy { it::class.qualifiedName!! }

    val mainPageMap = mapOf(
        MessagePage::class.qualifiedName!! to MessagePage,
        ContactPage::class.qualifiedName!! to ContactPage,
        ExplorerPage::class.qualifiedName!! to ExplorerPage,
        MePage::class.qualifiedName!! to MePageContent,
    )
    val globalPageMap = mapOf(
        UserAvatarPage::class.qualifiedName!! to UserAvatarPage,
        PrivateChatPage::class.qualifiedName!! to PrivateChatPage,
        MyProfilePage::class.qualifiedName!! to MyProfilePage,
        MyQRCodePage::class.qualifiedName!! to MyQrCodePage,
        ChatDetailPage::class.qualifiedName!! to ChatDetailPage,
        SearchPage::class.qualifiedName!! to SearchPage
    )

    val pageRouteContentMap = mainPageMap + globalPageMap

//    val pages = (mainPages + globalPages)

//    val pageMap: Map<String, Page> = pages.associateBy { it.route }
}