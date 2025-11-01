package top.writerpass.cmplibrary.compose.ables

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue

interface StringComposeExt {

    @Composable
    fun String.Text(
        modifier: Modifier = Modifier.Companion,
        color: Color = Color.Companion.Unspecified,
        fontSize: TextUnit = TextUnit.Companion.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Companion.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Companion.Unspecified,
        overflow: TextOverflow = TextOverflow.Companion.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
        onTextLayout: ((TextLayoutResult) -> Unit)? = null,
        style: TextStyle = LocalTextStyle.current
    ) {
        androidx.compose.material3.Text(
            this,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style
        )
    }

    @Composable
    fun String.TextButton(
        modifier: Modifier = Modifier.Companion,
        enabled: Boolean = true,
        shape: Shape = ButtonDefaults.textShape,
        colors: ButtonColors = ButtonDefaults.textButtonColors(),
        elevation: ButtonElevation? = null,
        border: BorderStroke? = null,
        contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit
    ) {
        androidx.compose.material3.TextButton(
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { Text() },
            onClick = onClick
        )
    }

    @Composable
    fun String.OutlinedButton(
        modifier: Modifier = Modifier.Companion,
        enabled: Boolean = true,
        shape: Shape = ButtonDefaults.outlinedShape,
        colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
        elevation: ButtonElevation? = null,
        border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
        contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit,
    ) {
        androidx.compose.material3.OutlinedButton(
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { Text() },
            onClick = onClick
        )
    }

    @Composable
    fun String.OutlinedButtonWithConfirm(
        cancelLabel: String = "Cancel",
        doLabel: String = "$this !",
        modifier: Modifier = Modifier.Companion,
        enabled: Boolean = true,
        shape: Shape = ButtonDefaults.outlinedShape,
        colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Companion.Red
        ),
        elevation: ButtonElevation? = null,
        border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
        contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit,
    ) {
        var doubleCheck by remember { mutableStateOf(false) }
        AnimatedContent(doubleCheck) {
            if (it) {
                Row(modifier = Modifier.Companion.padding(horizontal = 8.dp)) {
                    cancelLabel.OutlinedButton(
                        modifier = modifier,
                        enabled = enabled,
                        shape = shape,
                        elevation = elevation,
                        border = border,
                        contentPadding = contentPadding,
                        interactionSource = interactionSource,
                    ) {
                        doubleCheck = false
                    }
                    doLabel.OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Companion.Red
                        )
                    ) {
                        doubleCheck = false
                        onClick()
                    }
                }
            } else {
                this@OutlinedButtonWithConfirm.OutlinedButton(
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    colors = colors,
                    elevation = elevation,
                    border = border,
                    contentPadding = contentPadding,
                    interactionSource = interactionSource,
                ) {
                    doubleCheck = true
                }
            }
        }
    }

    @Composable
    fun String.FilePicker(
        extensions: List<String> = emptyList(),
        title: String = "File Picker",
        onFileSelected: (MPFile<Any>?) -> Unit
    ) {
        val show = Mutable.someBoolean()
        this.TextButton { show.setTrue() }
        com.darkrockstudios.libraries.mpfilepicker.FilePicker(
            show = show.value,
            fileExtensions = extensions,
            title = title,
            onFileSelected = {
                onFileSelected(it)
                show.setFalse()
            }
        )
    }
}