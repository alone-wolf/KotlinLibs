package top.writerpass.micromessage.client.pages.base

import androidx.compose.ui.graphics.vector.ImageVector


interface IMainPage : IPage {
    val icon: ImageVector
    val iconSelected: ImageVector
}