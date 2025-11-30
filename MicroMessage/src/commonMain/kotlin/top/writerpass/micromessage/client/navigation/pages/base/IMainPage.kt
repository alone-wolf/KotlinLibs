package top.writerpass.micromessage.client.navigation.pages.base

import androidx.compose.ui.graphics.vector.ImageVector


interface IMainPage : IPage {
    val icon: ImageVector
    val iconSelected: ImageVector

    val showOnBottomBar: Boolean
        get() = true
}