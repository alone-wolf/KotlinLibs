package top.writerpass.rekuester.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.ktor.http.*
import top.writerpass.cmplibrary.compose.*
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.ui.componment.TabBarWithContent
import top.writerpass.rekuester.ui.part.RequestPartHeaders
import top.writerpass.rekuester.ui.part.RequestPartParams
import top.writerpass.rekuester.viewmodel.ApiViewModel

private val requestPartEntities = listOf(
    "Params",
    "Authorization",
    "Headers",
    "Body",
    "Scripts",
    "Settings"
)

sealed interface AuthTypes {
    val label: String

    object InheritAuthFromParent : AuthTypes {
        override val label: String = "Inherit auth from parent"
    }

    object NoAuth : AuthTypes {
        override val label: String = "No authentication"
    }

    object Basic : AuthTypes {
        override val label: String = "Basic Auth"
    }

    object Bearer : AuthTypes {
        override val label: String = "Bearer Token"
    }

    object JWT : AuthTypes {
        override val label: String = "JWT Token"
    }

    object ApiKey : AuthTypes {
        override val label: String = "API Key"
    }

    // 如果你希望支持自定义类型，可以用 data class
    data class Custom(val customLabel: String) : AuthTypes {
        override val label: String = customLabel
    }
}


private val responsePartEntities = listOf(
    "Overview", "Body", "Cookies", "Headers"
)

@Composable
fun ApiRequestPage(apiUuid: String) {
    val apiViewModel = ApiViewModel.instance(apiUuid)
    val api by apiViewModel.apiFlow.collectAsState()
    val apiState by apiViewModel.apiStateFlow.collectAsState()

    FullSizeColumn(modifier = Modifier) {
        FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
            val editLabel = remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (editLabel.value) {
                    apiState.label.OutlinedBasicTextField(maxLines = 1)
                } else {
                    apiState.label.value.Text(modifier = Modifier.clickable { editLabel.setTrue() })
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (apiState.isModified.value) "Save".TextButton {
                apiState.composeNewApi().let {
                    apiViewModel.updateOrInsert(it)
                    apiState.isModified.setFalse()
                    editLabel.setFalse()
                }
            }
        }
        FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
            apiState.method.DropDownMenu(
                entities = remember {
                    HttpMethod.DefaultMethods.associateBy { it.value }
                },
                any2String = { this.value }
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = apiState.urlBinding.text.value,
                placeholder = { "Address".Text() },
                onValueChange = { apiState.urlBinding.onTextChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            //
                            apiState.urlBinding.onUrlEditStart()
                        }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        apiViewModel.request()
                    }
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(8.dp))
            "Send".OutlinedButton {
                apiViewModel.request()
            }
        }
        FullSizeColumn {
            apiState.toString().Text()
            apiViewModel.toString().Text()
            "Request".Text()
            TabBarWithContent(
                modifier = Modifier.fillMaxWidth(),
                entities = requestPartEntities,
                onPage = { pageId ->
                    when (pageId) {
                        0 -> RequestPartParams(apiState)
                        1 -> {
                            // authorization
                        }

                        2 -> RequestPartHeaders(apiState)

                        else -> {
                            requestPartEntities[pageId].Text()
                            "Not Implemented".Text()
                        }
                    }
                }
            )
            "Response".Text()
            FullSizeColumn(modifier = Modifier.verticalScroll(rememberScrollState())) {

                TabBarWithContent(
                    modifier = Modifier.fillMaxWidth(),
                    responsePartEntities
                ) { pageId ->
                    apiState.requestResult?.let { reqResult ->
                        reqResult.response?.let { result ->

                            when (pageId) {
                                0 -> {
                                    "Overview".Text()
                                    "Status: ${result.code}".Text()
                                    "Requested at: ${result.reqTime}".Text()
                                    "Responded at: ${result.respTime}".Text()
                                }

                                1 -> {
                                    "Requested at: ${result.reqTime}".Text()
                                    "Responded at: ${result.respTime}".Text()
                                    result.body.Text()
                                }

                                2 -> {}
                                3 -> {
                                    result.headers.forEach { (key, values) ->
                                        values.forEach { value ->
                                            FullWidthRow(modifier = Modifier.clickable {}) {
                                                key.Text(modifier = Modifier.weight(0.4f))
                                                value.Text(modifier = Modifier.weight(1f))
                                            }
                                            HorizontalDivider()
                                        }
                                    }
                                }
                            }
                        } ?: FullSizeColumn {
                            "Error: ${reqResult.error}".Text()
                        }
                    }
                }
            }
        }
    }
}



