package top.writerpass.cmplibrary.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MutableState<String>.DropDownMenu(
    values: List<String>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        value.TextButton(modifier = modifier) { expanded = !expanded }
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
                    text = { it.Text() }
                )
            }
        }
    }
}

@Composable
fun MutableState<String>.DropDownMenu(
    entities: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        value.TextButton { expanded = !expanded }
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
                    text = { k.Text() }
                )
            }
        }
    }
}

@Composable
fun <T : Any> MutableState<T>.DropDownMenu(
    entities: Map<String, T>,
    any2String: T.() -> String = { toString() },
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        value.any2String().TextButton { expanded = !expanded }
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
                    text = { k.Text() }
                )
            }
        }
    }
}