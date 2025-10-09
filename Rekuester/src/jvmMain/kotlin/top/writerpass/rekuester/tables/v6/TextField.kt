//package top.writerpass.rekuester.tables.v6
//
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsFocusedAsState
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.text.selection.LocalTextSelectionColors
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.TextFieldColors
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.graphics.takeOrElse
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.VisualTransformation
//
///**
// * <a href="https://m3.material.io/components/text-fields/overview" class="external"
// * target="_blank">Material Design filled text field</a>.
// *
// * Text fields allow users to enter text into a UI. They typically appear in forms and dialogs.
// * Filled text fields have more visual emphasis than outlined text fields, making them stand out
// * when surrounded by other content and components.
// *
// * ![Filled text field
// * image](https://developer.android.com/images/reference/androidx/compose/material3/filled-text-field.png)
// *
// * If you are looking for an outlined version, see [OutlinedTextField].
// *
// * A simple single line text field looks like:
// *
// * @sample androidx.compose.material3.samples.SimpleTextFieldSample
// *
// * You may provide a placeholder:
// *
// * @sample androidx.compose.material3.samples.TextFieldWithPlaceholder
// *
// * You can also provide leading and trailing icons:
// *
// * @sample androidx.compose.material3.samples.TextFieldWithIcons
// *
// * You can also provide a prefix or suffix to the text:
// *
// * @sample androidx.compose.material3.samples.TextFieldWithPrefixAndSuffix
// *
// * To handle the error input state, use [isError] parameter:
// *
// * @sample androidx.compose.material3.samples.TextFieldWithErrorState
// *
// * Additionally, you may provide additional message at the bottom:
// *
// * @sample androidx.compose.material3.samples.TextFieldWithSupportingText
// *
// * Password text field example:
// *
// * @sample androidx.compose.material3.samples.PasswordTextField
// *
// * Hiding a software keyboard on IME action performed:
// *
// * @sample androidx.compose.material3.samples.TextFieldWithHideKeyboardOnImeAction
// *
// * If apart from input text change you also want to observe the cursor location, selection range, or
// * IME composition use the TextField overload with the [TextFieldValue] parameter instead.
// *
// * @param value the input text to be shown in the text field
// * @param onValueChange the callback that is triggered when the input service updates the text. An
// *   updated text comes as a parameter of the callback
// * @param modifier the [Modifier] to be applied to this text field
// * @param enabled controls the enabled state of this text field. When `false`, this component will
// *   not respond to user input, and it will appear visually disabled and disabled to accessibility
// *   services.
// * @param readOnly controls the editable state of the text field. When `true`, the text field cannot
// *   be modified. However, a user can focus it and copy text from it. Read-only text fields are
// *   usually used to display pre-filled forms that a user cannot edit.
// * @param textStyle the style to be applied to the input text. Defaults to [LocalTextStyle].
// * @param label the optional label to be displayed inside the text field container. The default text
// *   style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
// *   [Typography.bodyLarge] when the text field is not in focus
// * @param placeholder the optional placeholder to be displayed when the text field is in focus and
// *   the input text is empty. The default text style for internal [Text] is [Typography.bodyLarge]
// * @param leadingIcon the optional leading icon to be displayed at the beginning of the text field
// *   container
// * @param trailingIcon the optional trailing icon to be displayed at the end of the text field
// *   container
// * @param prefix the optional prefix to be displayed before the input text in the text field
// * @param suffix the optional suffix to be displayed after the input text in the text field
// * @param supportingText the optional supporting text to be displayed below the text field
// * @param isError indicates if the text field's current value is in error. If set to true, the
// *   label, bottom indicator and trailing icon by default will be displayed in error color
// * @param visualTransformation transforms the visual representation of the input [value] For
// *   example, you can use
// *   [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation] to
// *   create a password text field. By default, no visual transformation is applied.
// * @param keyboardOptions software keyboard options that contains configuration such as
// *   [KeyboardType] and [ImeAction].
// * @param keyboardActions when the input service emits an IME action, the corresponding callback is
// *   called. Note that this IME action may be different from what you specified in
// *   [KeyboardOptions.imeAction].
// * @param singleLine when `true`, this text field becomes a single horizontally scrolling text field
// *   instead of wrapping onto multiple lines. The keyboard will be informed to not show the return
// *   key as the [ImeAction]. Note that [maxLines] parameter will be ignored as the maxLines
// *   attribute will be automatically set to 1.
// * @param maxLines the maximum height in terms of maximum number of visible lines. It is required
// *   that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [singleLine] is true.
// * @param minLines the minimum height in terms of minimum number of visible lines. It is required
// *   that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [singleLine] is true.
// * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
// *   emitting [Interaction]s for this text field. You can use this to change the text field's
// *   appearance or preview the text field in different states. Note that if `null` is provided,
// *   interactions will still happen internally.
// * @param shape defines the shape of this text field's container
// * @param colors [TextFieldColors] that will be used to resolve the colors used for this text field
// *   in different states. See [TextFieldDefaults.colors].
// */
//@Composable
//fun TextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    readOnly: Boolean = false,
//    textStyle: TextStyle = LocalTextStyle.current,
//    label: @Composable (() -> Unit)? = null,
//    placeholder: @Composable (() -> Unit)? = null,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    prefix: @Composable (() -> Unit)? = null,
//    suffix: @Composable (() -> Unit)? = null,
//    supportingText: @Composable (() -> Unit)? = null,
//    isError: Boolean = false,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    singleLine: Boolean = false,
//    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
//    minLines: Int = 1,
//    interactionSource: MutableInteractionSource? = null,
//    shape: Shape = TextFieldDefaults.shape,
//    colors: TextFieldColors = TextFieldDefaults.colors()
//) {
//    @Suppress("NAME_SHADOWING")
//    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
//    // If color is not provided via the text style, use content color as a default
//    val textColor =
//        textStyle.color.takeOrElse {
//            val focused = interactionSource.collectIsFocusedAsState().value
//            colors.textColor(enabled, isError, focused)
//        }
//    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
//
//    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
//        BasicTextField(
//            value = value,
//            modifier =
//                modifier
//                    .defaultErrorSemantics(isError, getString(Strings.DefaultErrorMessage))
//                    .defaultMinSize(
//                        minWidth = TextFieldDefaults.MinWidth,
//                        minHeight = TextFieldDefaults.MinHeight
//                    ),
//            onValueChange = onValueChange,
//            enabled = enabled,
//            readOnly = readOnly,
//            textStyle = mergedTextStyle,
//            cursorBrush = SolidColor(colors.cursorColor(isError)),
//            visualTransformation = visualTransformation,
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,
//            interactionSource = interactionSource,
//            singleLine = singleLine,
//            maxLines = maxLines,
//            minLines = minLines,
//            decorationBox =
//                @Composable { innerTextField ->
//                    // places leading icon, text field with label and placeholder, trailing icon
//                    TextFieldDefaults.DecorationBox(
//                        value = value,
//                        visualTransformation = visualTransformation,
//                        innerTextField = innerTextField,
//                        placeholder = placeholder,
//                        label = label,
//                        leadingIcon = leadingIcon,
//                        trailingIcon = trailingIcon,
//                        prefix = prefix,
//                        suffix = suffix,
//                        supportingText = supportingText,
//                        shape = shape,
//                        singleLine = singleLine,
//                        enabled = enabled,
//                        isError = isError,
//                        interactionSource = interactionSource,
//                        colors = colors
//                    )
//                }
//        )
//    }
//}