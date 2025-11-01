package top.writerpass.rekuester.ui.part.request.body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.RadioButtonGroup
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.models.ApiStateBodyContainer
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes


@Composable
fun RequestPartBody() {
    val apiViewModel = LocalApiViewModel.current
    val ui by apiViewModel.ui.collectAsState()
    val bodyType by remember { derivedStateOf { ui.body.type } }
    FullWidthColumn {
        RadioButtonGroup(
            options = BodyTypes.list,
            selectedOption = bodyType,
            onOptionSelected = { ui.body = ui.body.copy(type = it) },
            modifier = Modifier.fillMaxWidth(),
            label = { it.label }
        )
        FullWidthColumn {
            when (bodyType) {
                BodyTypes.None -> { "None of Body".Text() }
                BodyTypes.FormData -> RequestPartBodyFormData()
                BodyTypes.FormUrlencoded -> RequestPartBodyFormUrlencoded()
                BodyTypes.Binary -> RequestPartBodyBinary()
                BodyTypes.Raw -> {
                    // sub-selector
                    val rawType1 by remember { derivedStateOf { ui.body.raw?.type ?: RawBodyTypes.Text } }
                    RadioButtonGroup(
                        options = RawBodyTypes.list,
                        selectedOption = rawType1,
                        onOptionSelected = { newType->
                            val body = ui.body
                            val raw = body.raw

                            val newRaw = raw?.copy(type = newType) ?: ApiStateBodyContainer.Raw(newType,"")

                            ui.body.raw ?: ApiStateBodyContainer.Raw(newType,"")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { it.label }
                    )

                    val rawType = Mutable.something(ui.body.raw?.type ?: RawBodyTypes.Text)
                    rawType.DropDownMenu(
                        entities = RawBodyTypes.map,
                        any2String = { label },
                    )
                    val rawText by remember {
                        derivedStateOf { ui.body.raw?.content ?: "" }
                    }
                    OutlinedTextField(
                        value = rawText,
                        onValueChange = {
//                            ui.body = ui.body.copy(raw = ui.body.raw?.copy(content = it))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                BodyTypes.GraphQL -> {
                    // not implementation
                }
            }
        }
    }
}