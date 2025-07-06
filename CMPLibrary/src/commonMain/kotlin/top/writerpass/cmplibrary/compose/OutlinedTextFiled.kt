package top.writerpass.cmplibrary.compose

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun MutableState<String>.OutlinedTextFiled(modifier: Modifier = Modifier, label: String? = null) {
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = label?.let { { it.Text() } },
        modifier = modifier
    )
}

@Composable
fun <T : Any> MutableState<T>.OutlinedTextFiled(
    modifier: Modifier = Modifier,
    string2Any: String.() -> T,
    any2String: T.() -> String
) {
    OutlinedTextField(
        value = value.any2String(),
        onValueChange = { value = it.string2Any() },
        modifier = modifier
    )
}