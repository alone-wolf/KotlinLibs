package top.writerpass.micromessage.client.pages.base

import androidx.compose.ui.graphics.vector.ImageVector

interface MainPages : Pages {
    val icon: ImageVector
    val iconSelected: ImageVector
    override val label: String
}