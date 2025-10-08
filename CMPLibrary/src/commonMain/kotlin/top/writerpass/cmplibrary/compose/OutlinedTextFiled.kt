package top.writerpass.cmplibrary.compose

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun MutableState<String>.OutlinedTextFiled1(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        placeholder = placeholder?.let { { it.Text() } },
        enabled = enabled,
        maxLines = maxLines,
        label = label?.let { { it.Text() } },
        modifier = modifier,
        visualTransformation = visualTransformation
    )
}

@Composable
fun <T : Any> MutableState<T>.OutlinedTextFiled1(
    label: String? = null,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    string2Any: String.() -> T,
    any2String: T.() -> String
) {
    OutlinedTextField(
        label = label?.let { { it.Text() } },
        maxLines = maxLines,
        enabled = enabled,
        value = value.any2String(),
        onValueChange = { value = it.string2Any() },
        modifier = modifier
    )
}