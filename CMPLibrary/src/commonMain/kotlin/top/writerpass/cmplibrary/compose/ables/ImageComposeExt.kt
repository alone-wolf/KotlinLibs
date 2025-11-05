package top.writerpass.cmplibrary.compose.ables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

interface ImageComposeExt

object IconComposeExt : ImageComposeExt {
    @Composable
    fun ImageVector.CxIcon(
        description: String? = null,
        modifier: Modifier = Modifier.Companion,
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
    fun Painter.CxIcon(
        description: String? = null,
        modifier: Modifier = Modifier.Companion,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            painter = this,
            contentDescription = description,
            modifier = modifier,
            tint = tint
        )
    }

    @Composable
    fun Painter.CxIconButton(
        description: String? = null,
        modifier: Modifier = Modifier.Companion,
        enabled: Boolean = true,
        colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
            content = {
                this.CxIcon(description)
            }
        )
    }

    @Composable
    fun ImageVector.CxIconButton(
        description: String? = null,
        modifier: Modifier = Modifier.Companion,
        enabled: Boolean = true,
        colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit
    ) {
        androidx.compose.material3.IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
            content = {
                this.CxIcon(description)
            }
        )
    }
}

@Composable
fun WpIcon(
    imageVector: ImageVector,
    description: String? = null,
    modifier: Modifier = Modifier.Companion
) = Icon(
    imageVector = imageVector,
    contentDescription = description,
    modifier = modifier
)

