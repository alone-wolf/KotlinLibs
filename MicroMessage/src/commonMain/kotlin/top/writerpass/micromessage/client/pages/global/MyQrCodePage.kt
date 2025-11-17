package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.micromessage.client.pages.base.Pages

object MyQrCodePage : Pages {
    override val routeRaw: String
        get() = "my-qrcode-page"
    override val route: String
        get() = "my-qrcode-page"
    override val label: String
        get() = "QRCode"
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {}
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            FullSizeColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FullSizeColumn {

                }
                Icons.Default.QrCode2.CxIcon(modifier = Modifier.size(100.dp))
            }
        }
}