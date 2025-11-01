package top.writerpass.cmplibrary.compose.ables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

interface ImageComposeExt {
    @Composable
    fun Icon(
        imageVector: ImageVector,
        description: String? = null,
        modifier: Modifier = Modifier.Companion
    ) = androidx.compose.material3.Icon(
        imageVector = imageVector,
        contentDescription = description,
        modifier = modifier
    )

    @Composable
    fun ImageVector.Icon(
        description: String? = null,
        modifier: Modifier = Modifier.Companion,
        tint: Color = LocalContentColor.current
    ) {
        androidx.compose.material3.Icon(
            imageVector = this,
            contentDescription = description,
            modifier = modifier,
            tint = tint
        )
    }

    @Composable
    fun Painter.Icon(
        description: String? = null,
        modifier: Modifier = Modifier.Companion,
        tint: Color = LocalContentColor.current
    ) {
        androidx.compose.material3.Icon(
            painter = this,
            contentDescription = description,
            modifier = modifier,
            tint = tint
        )
    }

    @Composable
    fun Painter.IconButton(
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
                this.Icon(description)
            }
        )
    }

    @Composable
    fun ImageVector.IconButton(
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
                this.Icon(description)
            }
        )
    }
}