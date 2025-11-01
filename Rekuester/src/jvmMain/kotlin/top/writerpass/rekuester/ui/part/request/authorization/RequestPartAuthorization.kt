package top.writerpass.rekuester.ui.part.request.authorization

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.OutlinedBasicTextField
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.compose.ables.Composables
import top.writerpass.cmplibrary.compose.ables.StateComposables
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.models.AuthTypes

@Composable
fun RequestPartAuthorization() {
    val apiViewModel = LocalApiViewModel.current
    val ui by apiViewModel.ui.collectAsState()

    val authType by remember(ui.auth) {
        derivedStateOf { ui.auth.type }
    }

    Composables.Scope {
        StateComposables.Scope {
            FullWidthRow {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    authType.label.TextButton { expanded = !expanded }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                    ) {
                        AuthTypes.typeMap.forEach { (k, v) ->
                            DropdownMenuItem(
                                onClick = {
                                    apiViewModel.updateAuth {
                                        copy(type = v)
                                    }
                                    expanded = false
                                },
                                text = { k.Text() }
                            )
                        }
                    }
                }
                FullWidthColumn {
                    when (authType) {
                        AuthTypes.InheritAuthFromParent -> {
                            "Inherit Auth From Parent".Text()
                        }

                        AuthTypes.NoAuth -> {
                            "No Auth".Text()
                        }

                        AuthTypes.Basic -> {
                            ui.auth.basic?.let { basic ->
                                OutlinedTextField(
                                    value = basic.username,
                                    onValueChange = {
                                        val new = basic.copy(username = it)
                                        ui.auth = ui.auth.copy(basic = new)
                                    },
                                )
                                OutlinedTextField(
                                    value = basic.password,
                                    onValueChange = {
                                        val new = basic.copy(password = it)
                                        ui.auth = ui.auth.copy(basic = new)
                                    },
                                )
                            }
                        }

                        AuthTypes.Bearer -> {
                            ui.auth.bearer?.let { bearer ->
                                OutlinedTextField(
                                    value = bearer.token,
                                    onValueChange = {
                                        val new = bearer.copy(token = it)
                                        ui.auth = ui.auth.copy(bearer = new)
                                    },
                                )
                            }
                        }

                        AuthTypes.JWT -> {
                            ui.auth.jwt?.let { jwt ->
                                OutlinedTextField(
                                    value = jwt.secret,
                                    onValueChange = {
                                        val new = jwt.copy(secret = it)
                                        ui.auth = ui.auth.copy(jwt = new)
                                    },
                                )
                            }
                        }

                        AuthTypes.ApiKey -> {
                            val key = Mutable.someString("")
                            val value = Mutable.someString("")
                            val addTo = Mutable.someString("")
                            key.OutlinedBasicTextField(label = "Key")
                            value.OutlinedBasicTextField(label = "Value")
                            addTo.OutlinedBasicTextField(label = "Add to")
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}