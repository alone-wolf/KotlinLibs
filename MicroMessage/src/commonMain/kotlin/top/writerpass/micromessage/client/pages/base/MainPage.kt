package top.writerpass.micromessage.client.pages.base

import androidx.compose.ui.graphics.vector.ImageVector

interface MainPage : Page {
    val icon: ImageVector
    val iconSelected: ImageVector
    override val label: String
}