package top.writerpass.rekuester.ui.part.request.body

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes

@Composable
fun RequestPartBody() {
    val apiViewModel = LocalApiViewModel.current
    val ui by apiViewModel.ui.collectAsState()
    val bodyType by remember {
        derivedStateOf { ui.body.type }
    }
    FullWidthRow {
        var expanded by remember { mutableStateOf(false) }
        bodyType.label.TextButton {
            expanded = true
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = {
                BodyTypes.map.forEach { (t, u) ->
                    DropdownMenuItem(
                        text = { t.Text() },
                        onClick = {
                            ui.body = ui.body.copy(type = u)
                            expanded = false
                        }
                    )
                }
            }
        )
        FullWidthColumn {
            when (bodyType) {
                BodyTypes.None -> {
                    "None of Body".Text()
                }

                BodyTypes.FormData -> RequestPartBodyFormData()

                BodyTypes.FormUrlencoded -> RequestPartBodyFormUrlencoded()

                BodyTypes.Binary -> RequestPartBodyBinary()

                BodyTypes.Raw -> {
                    // sub-selector
                    val rawType = Mutable.something<RawBodyTypes>(RawBodyTypes.Text)
                    rawType.DropDownMenu(
                        entities = RawBodyTypes.map,
                        any2String = { label },
                    )
                }

                BodyTypes.GraphQL -> {
                    // not implementation
                }
            }
        }
    }
}