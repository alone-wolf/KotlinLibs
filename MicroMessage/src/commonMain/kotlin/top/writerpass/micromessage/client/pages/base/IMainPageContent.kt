package top.writerpass.micromessage.client.pages.base

import androidx.compose.ui.graphics.vector.ImageVector

interface IMainPageContent : IPageContent {
    val icon: ImageVector
    val iconSelected: ImageVector
}