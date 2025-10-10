package top.writerpass.rekuester.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.ktor.http.HttpMethod
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.OutlinedTextFiled1
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.ApiHeader
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiState
import top.writerpass.rekuester.tables.v8.HeaderTableSheet
import top.writerpass.rekuester.ui.componment.TabBarWithContent
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
                    apiState.label.OutlinedTextFiled1(maxLines = 1)
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

@Composable
fun RequestPartParams(apiState: ApiState) {
    "Params".Text()

    HeaderTableSheet(
        rowCount = apiState.params.list.size,
        columnCount = 3,
        headers = listOf("Key", "Value", "Description"),
        onItem = { rowId, columnId ->
            val row = apiState.params.list[rowId]
            when (columnId) {
                0 -> row.key
                1 -> row.value
                2 -> row.description
                else -> "--"
            }
        },
        onItemChange = { rowId, columnId, item ->
            val apiParam = apiState.params.list[rowId]
            val newApiParam = when(columnId){
                0-> apiParam.copy(key = item)
                1-> apiParam.copy(value = item)
                2-> apiParam.copy(description = item)
                else -> apiParam
            }
            apiState.params.list[rowId] = newApiParam
        }
    )

    FullWidthRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val k = Mutable.someString()
        val v = Mutable.someString()
        val d = Mutable.someString()
        k.OutlinedTextFiled1(
            placeholder = "Key",
            modifier = Modifier.weight(1f)
        )
        v.OutlinedTextFiled1(
            placeholder = "Value",
            modifier = Modifier.weight(1f)
        )
        d.OutlinedTextFiled1(
            placeholder = "Description",
            modifier = Modifier.weight(1f)
        )
        Icons.Default.Save.IconButton {
            if (k.value.isNotBlank()) {

                apiState.params.list.add(
                    ApiParam(
                        k.value,
                        v.value,
                        d.value
                    )
                )
                k.value = ""
                v.value = ""
                d.value = ""
            }
        }
    }
}

@Composable
fun RequestPartHeaders(apiState: ApiState) {
    "Headers".Text()

    apiState.headers.list.forEachIndexed { index, (k, v, d) ->
        FullWidthRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val kk = Mutable.someString(k)
            val vv = Mutable.someString(v)
            val dd = Mutable.someString(d)
            OutlinedTextField(
                value = kk.value,
                placeholder = { "Key".Text() },
                onValueChange = { kk.value = it },
                modifier = Modifier.weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            apiState.urlBinding.onParamsEditStart()
                        }
                    }
            )
            OutlinedTextField(
                value = vv.value,
                placeholder = { "Value".Text() },
                onValueChange = { vv.value = it },
                modifier = Modifier.weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            apiState.urlBinding.onParamsEditStart()
                        }
                    }
            )
            OutlinedTextField(
                value = dd.value,
                placeholder = { "Description".Text() },
                onValueChange = { dd.value = it },
                modifier = Modifier.weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            apiState.urlBinding.onParamsEditStart()
                        }
                    }
            )
            Icons.Default.Delete.IconButton {
                apiState.headers.removeAt(index)
            }
            Icons.Default.Save.IconButton {
                apiState.headers.list[index] = ApiHeader(
                    key = kk.value,
                    value = vv.value,
                    description = dd.value
                )
            }
        }
    }
    FullWidthRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val k = Mutable.someString()
        val v = Mutable.someString()
        val d = Mutable.someString()
        k.OutlinedTextFiled1(
            placeholder = "Key",
            modifier = Modifier.weight(1f)
        )
        v.OutlinedTextFiled1(
            placeholder = "Value",
            modifier = Modifier.weight(1f)
        )
        d.OutlinedTextFiled1(
            placeholder = "Description",
            modifier = Modifier.weight(1f)
        )
        Icons.Default.Save.IconButton {
            if (k.value.isNotBlank()) {

                apiState.headers.list.add(
                    ApiHeader(
                        k.value,
                        v.value,
                        d.value
                    )
                )
                k.value = ""
                v.value = ""
                d.value = ""
            }
        }
    }
}