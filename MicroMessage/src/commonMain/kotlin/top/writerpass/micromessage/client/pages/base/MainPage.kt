package top.writerpass.micromessage.client.pages.base

import kotlinx.serialization.Serializable

//interface MainPage : Page {
//    val icon: ImageVector
//    val iconSelected: ImageVector
//    override val label: String
//}


@Serializable
object MyProfile : IPage

@Serializable
object MePage : IMainPage

@Serializable
object MessagePage : IMainPage

@Serializable
object ExplorerPage : IMainPage

@Serializable
object ContactPage : IMainPage