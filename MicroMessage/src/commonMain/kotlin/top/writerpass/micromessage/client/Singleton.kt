package top.writerpass.micromessage.client

import top.writerpass.micromessage.client.pages.base.Page
import top.writerpass.micromessage.client.pages.global.ChatDetailPage
import top.writerpass.micromessage.client.pages.global.MyProfilePage
import top.writerpass.micromessage.client.pages.global.MyQrCodePage
import top.writerpass.micromessage.client.pages.global.PrivateChatPage
import top.writerpass.micromessage.client.pages.global.SearchPage
import top.writerpass.micromessage.client.pages.global.UserAvatarPage
import top.writerpass.micromessage.client.pages.main.Contact
import top.writerpass.micromessage.client.pages.main.Explorer
import top.writerpass.micromessage.client.pages.main.Me
import top.writerpass.micromessage.client.pages.main.Message

object Singleton{
    val mainPages = listOf(
        Message,
        Contact,
        Explorer,
        Me,
    )
    val globalPages = listOf(
        UserAvatarPage,
        PrivateChatPage,
        MyProfilePage,
        MyQrCodePage,
        ChatDetailPage,
        SearchPage
    )

    val pages = (mainPages + globalPages)

    val pageMap: Map<String, Page> = pages.associateBy { it.route }
}