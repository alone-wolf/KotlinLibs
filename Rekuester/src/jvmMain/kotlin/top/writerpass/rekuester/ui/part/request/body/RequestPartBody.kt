package top.writerpass.rekuester.ui.part.request.body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.RadioButtonGroup
import top.writerpass.cmplibrary.compose.ables.Composables
import top.writerpass.cmplibrary.compose.ables.StateComposables
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes


@Composable
fun RequestPartBody() {
    val apiViewModel = LocalApiViewModel.current
    val bodyPart = remember { apiViewModel.bodyPart }
    val body = apiViewModel.ui.collectAsState().value.body
    Composables.Scope {
        StateComposables.Scope {
            FullWidthColumn {
                RadioButtonGroup(
                    options = BodyTypes.list,
                    selectedOption = body.type,
                    onOptionSelected = bodyPart::updateType,
                    modifier = Modifier.fillMaxWidth(),
                    label = { it.label }
                )
                FullWidthColumn {
                    when (body.type) {
                        BodyTypes.None -> {
                            "None of Body".Text()
                        }

                        BodyTypes.FormData -> RequestPartBodyFormData()
                        BodyTypes.FormUrlencoded -> RequestPartBodyFormUrlencoded()
                        BodyTypes.Binary -> RequestPartBodyBinary()
                        BodyTypes.Raw -> {
                            // sub-selector
                            val rawType by remember { derivedStateOf(bodyPart.rawTypeCalculator) }
                            val rawText by remember { derivedStateOf(bodyPart.rawContentCalculator) }

                            RadioButtonGroup(
                                options = RawBodyTypes.list,
                                selectedOption = rawType,
                                onOptionSelected = bodyPart::updateRawType,
                                modifier = Modifier.fillMaxWidth(),
                                label = { it.label }
                            )
                            OutlinedTextField(
                                value = rawText,
                                onValueChange = bodyPart::updateRawContent,
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
    }
}