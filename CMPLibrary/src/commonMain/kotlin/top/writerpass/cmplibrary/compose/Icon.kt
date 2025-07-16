package top.writerpass.cmplibrary.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun Icon(
    imageVector: ImageVector,
    description: String? = null,
    modifier: Modifier = Modifier
) = Icon(
    imageVector = imageVector,
    contentDescription = description,
    modifier = modifier
)

@Composable
fun ImageVector.Icon(
    description: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = this,
        contentDescription = description,
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun Painter.Icon(
    description: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = this,
        contentDescription = description,
        modifier = modifier,
        tint = tint
    )
}

