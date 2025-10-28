package top.writerpass.rekuester.ui.part

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.*
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.ui.page.AuthTypes

@Composable
fun RequestPartAuthorization() {
    val apiViewModel = LocalApiViewModel.current
    val apiState by apiViewModel.apiStateFlow.collectAsState()

    val authType by remember(apiState.auth.value) {
        derivedStateOf { apiState.auth.value.type }
    }

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
                    val userName = Mutable.someString("")
                    val password = Mutable.someString("")
                    userName.OutlinedBasicTextField(label = "Username")
                    password.OutlinedBasicTextField(label = "Password")
                }

                AuthTypes.Bearer -> {
                    val token = Mutable.someString("")
                    token.OutlinedBasicTextField(label = "Token")
                }

                AuthTypes.JWT -> {
                    val secret = Mutable.someString("")
                    secret.OutlinedBasicTextField(label = "Token")
                }

                AuthTypes.ApiKey -> {
                    val key = Mutable.someString("")
                    val value = Mutable.someString("")
                    val addTo = Mutable.someString("")
                    key.OutlinedBasicTextField(label = "Username")
                    value.OutlinedBasicTextField(label = "Password")
                    addTo.OutlinedBasicTextField(label = "Add to")
                }

                else -> {}
            }
        }
    }
}