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
import top.writerpass.cmplibrary.compose.ables.MutableStateComposeExt.CxOutlinedBasicTextField
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton
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

    FullWidthRow {
        var expanded by remember { mutableStateOf(false) }
        Box {
            authType.label.CxTextButton { expanded = !expanded }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
            ) {
                AuthTypes.typeMap.forEach { (k, v) ->
                    DropdownMenuItem(
                        onClick = {
                            apiViewModel.authPart.updateType(v)
                            expanded = false
                        },
                        text = { k.CxText() }
                    )
                }
            }
        }
        FullWidthColumn {
            when (authType) {
                AuthTypes.InheritAuthFromParent -> {
                    "Inherit Auth From Parent".CxText()
                }

                AuthTypes.NoAuth -> {
                    "No Auth".CxText()
                }

                AuthTypes.Basic -> {
                    ui.auth.basic?.let { basic ->
                        OutlinedTextField(
                            value = basic.username,
                            onValueChange = {
                                val newBasic = basic.copy(username = it)
                                apiViewModel.authPart.updateBasic(newBasic)
                            },
                        )
                        OutlinedTextField(
                            value = basic.password,
                            onValueChange = {
                                val newBasic = basic.copy(password = it)
                                apiViewModel.authPart.updateBasic(newBasic)
                            },
                        )
                    }
                }

                AuthTypes.Bearer -> {
                    ui.auth.bearer?.let { bearer ->
                        OutlinedTextField(
                            value = bearer.token,
                            onValueChange = {
                                val newBearer = bearer.copy(token = it)
                                apiViewModel.authPart.updateBearer(newBearer)
                            },
                        )
                    }
                }

                AuthTypes.JWT -> {
                    ui.auth.jwt?.let { jwt ->
                        OutlinedTextField(
                            value = jwt.secret,
                            onValueChange = {
                                val newJwt = jwt.copy(secret = it)
                                apiViewModel.authPart.updateJwt(newJwt)
                            },
                        )
                    }
                }

                AuthTypes.ApiKey -> {
                    ui.auth.apiKey?.let { apiKey ->
                        val key = Mutable.someString("")
                        val value = Mutable.someString("")
                        val addTo = Mutable.someString("")
                        key.CxOutlinedBasicTextField(label = "Key"){
                            val newApiKey = apiKey.copy(key = it)
                            apiViewModel.authPart.updateApiKey(newApiKey)
                        }
                        value.CxOutlinedBasicTextField(label = "Value"){
                            val newApiKey = apiKey.copy(value = it)
                            apiViewModel.authPart.updateApiKey(newApiKey)
                        }
                        addTo.CxOutlinedBasicTextField(label = "Add to"){
//                            val newApiKey = apiKey.copy(addTo = it)
//                            apiViewModel.authPart.updateApiKey(newApiKey)
                        }
                    }

                }
            }
        }
    }
}