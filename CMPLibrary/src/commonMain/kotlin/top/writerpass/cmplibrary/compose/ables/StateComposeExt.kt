package top.writerpass.cmplibrary.compose.ables

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton

interface StateComposeExt

object MutableStateComposeExt : StateComposeExt {
    @Composable
    fun MutableState<String>.CxBasicTextField(
        modifier: Modifier = Modifier,
        maxLines: Int = Int.MAX_VALUE,
        enabled: Boolean = true,
        visualTransformation: VisualTransformation = VisualTransformation.None,
//    textStyle: TextStyle =
    ) {
        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = { value = it },
            modifier = modifier,
            enabled = enabled,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
//        textStyle = textStyle
        )
    }

    @Composable
    fun MutableState<String>.CxOutlinedBasicTextField(
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
            placeholder = placeholder?.let { { it.CxText() } },
            enabled = enabled,
            maxLines = maxLines,
            label = label?.let { { it.CxText() } },
            modifier = modifier,
            visualTransformation = visualTransformation
        )
    }

    @Composable
    fun <T : Any> MutableState<T>.CxOutlinedBasicTextField(
        label: String? = null,
        maxLines: Int = Int.MAX_VALUE,
        enabled: Boolean = true,
        modifier: Modifier = Modifier,
        string2Any: String.() -> T,
        any2String: T.() -> String
    ) {
        OutlinedTextField(
            label = label?.let { { it.CxText() } },
            maxLines = maxLines,
            enabled = enabled,
            value = value.any2String(),
            onValueChange = { value = it.string2Any() },
            modifier = modifier
        )
    }

    @Composable
    fun MutableState<String>.CxDropDownMenu(
        values: List<String>,
        modifier: Modifier = Modifier.Companion,
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            value.CxTextButton(modifier = modifier) { expanded = !expanded }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                values.forEach {
                    DropdownMenuItem(
                        onClick = {
                            value = it
                            expanded = false
                        },
                        text = { it.CxText() }
                    )
                }
            }
        }
    }

    @Composable
    fun MutableState<String>.CxDropDownMenu(
        entities: Map<String, String>,
        modifier: Modifier = Modifier.Companion,
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            value.CxTextButton { expanded = !expanded }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier
            ) {
                entities.forEach { (k, v) ->
                    DropdownMenuItem(
                        onClick = {
                            value = v
                            expanded = false
                        },
                        text = { k.CxText() }
                    )
                }
            }
        }
    }

    @Composable
    fun <T : Any> MutableState<T>.CxDropDownMenu(
        entities: Map<String, T>,
        any2String: T.() -> String = { toString() },
        modifier: Modifier = Modifier.Companion,
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            value.any2String().CxTextButton { expanded = !expanded }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier
            ) {
                entities.forEach { (k, v) ->
                    DropdownMenuItem(
                        onClick = {
                            value = v
                            expanded = false
                        },
                        text = { k.CxText() }
                    )
                }
            }
        }
    }
}

