package top.writerpass.cmplibrary.compose

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun MutableState<String>.OutlinedTextFiled(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    label: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        enabled = enabled,
        maxLines = maxLines,
        label = label?.let { { it.Text() } },
        modifier = modifier
    )
}

@Composable
fun <T : Any> MutableState<T>.OutlinedTextFiled(
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