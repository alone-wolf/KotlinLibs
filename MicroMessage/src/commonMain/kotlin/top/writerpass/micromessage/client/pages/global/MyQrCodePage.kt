package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.brush
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.micromessage.client.pages.base.Page

object MyQrCodePage : Page {
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
                val myQRCodeString = remember {
                    "AAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBCCCCCCCCCCCCCCCCCCDDDDDDDDDDDDDDDDD"
                }
                val aaa = rememberQrCodePainter("lalalala")
                FullSizeColumn {
                    Image(
                        painter = rememberQrCodePainter(myQRCodeString){
                            logo {
                                painter = aaa
                                padding = QrLogoPadding.Natural(.1f)
                                shape = QrLogoShape.circle()
                                size = 0.2f
                            }
                            shapes {
                                ball = QrBallShape.circle()
                                darkPixel = QrPixelShape.roundCorners()
                                frame = QrFrameShape.roundCorners(.25f)
                            }
                            colors {
                                dark = QrBrush.brush {
                                    Brush.linearGradient(
                                        0f to Color.Red,
                                        1f to Color.Blue,
                                        end = Offset(it, it)
                                    )
                                }
                                frame = QrBrush.solid(Color.Black)
                            }
                        },
                        contentDescription = "User's Personal QRCode",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Icons.Default.QrCode2.CxIcon(modifier = Modifier.size(100.dp))
            }
        }
}