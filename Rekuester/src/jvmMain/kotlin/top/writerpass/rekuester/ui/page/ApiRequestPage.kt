package top.writerpass.rekuester.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.http.HttpMethod
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.ui.componment.TabBarWithContent
import top.writerpass.rekuester.ui.part.RequestPartAuthorization
import top.writerpass.rekuester.ui.part.RequestPartBody
import top.writerpass.rekuester.ui.part.RequestPartHeaders
import top.writerpass.rekuester.ui.part.RequestPartParams

private val requestPartEntities = listOf(
    "Params",
    "Authorization",
    "Headers",
    "Body",
    "Scripts",
    "Settings"
)


private val responsePartEntities = listOf(
    "Overview", "Body", "Cookies", "Headers"
)

@Composable
fun ApiRequestPage() {
    val apiViewModel = LocalApiViewModel.current
    val ui by apiViewModel.ui.collectAsState()

    FullSizeColumn(modifier = Modifier) {
        FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
            val editLabel = remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (editLabel.value) {
                    BasicTextField(
                        value = ui.label,
                        onValueChange = { ui.label = it },
                        maxLines = 1,
                        modifier = Modifier.height(48.dp),
                    )
                } else {
                    ui.label.TextButton {
                        editLabel.setTrue()
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (ui.isModified) "Save".TextButton {
                ui.toApi().let {
                    apiViewModel.updateOrInsertApi(it)
                    ui.updateModifyState(false)
                    editLabel.setFalse()
                }
            }
        }
        FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
            var expanded by remember {
                mutableStateOf(false)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                content = {
                    remember {
                        HttpMethod.DefaultMethods.associateBy { it.value }
                    }.forEach { (key, value) ->
                        DropdownMenuItem(
                            text = { Text(key) },
                            onClick = {
                                ui.method = value
                                expanded = false
                            }
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
//            OutlinedTextField(
//                value = ui.urlBinding.text.value,
//                placeholder = { "Address".Text() },
//                onValueChange = { ui.urlBinding.onTextChange(it) },
//                modifier = Modifier
//                    .weight(1f)
//                    .onFocusChanged { focusState ->
//                        if (focusState.isFocused) {
//                            ui.urlBinding.onUrlEditStart()
//                        }
//                    },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
//                keyboardActions = KeyboardActions(
//                    onGo = {
//                        apiViewModel.request()
//                    }
//                ),
//                maxLines = 1
//            )
            Spacer(modifier = Modifier.width(8.dp))
            "Send".OutlinedButton {
                apiViewModel.request()
            }
        }
        FullSizeColumn {
            "Request".Text()
            TabBarWithContent(
                modifier = Modifier.fillMaxWidth(),
                entities = requestPartEntities,
                onPage = { pageId ->
                    when (pageId) {
                        0 -> RequestPartParams()
                        1 -> RequestPartAuthorization()
                        2 -> RequestPartHeaders()
                        3 -> RequestPartBody()
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
                    ui.requestResult?.let { reqResult ->
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



