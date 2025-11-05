package top.writerpass.rekuester.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextDecorationLineStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxOutlinedButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.ApiStateHolder
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.models.ApiParam
import top.writerpass.rekuester.models.ApiStateBodyContainer
import top.writerpass.rekuester.ui.componment.TabBarWithContent
import top.writerpass.rekuester.ui.part.request.authorization.RequestPartAuthorization
import top.writerpass.rekuester.ui.part.request.body.RequestPartBody
import top.writerpass.rekuester.ui.part.request.headers.RequestPartHeaders
import top.writerpass.rekuester.ui.part.request.params.RequestPartParams

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

//class UrlBieBinding(
//    initial: String,
//    initialParams: List<ApiParam>
//) {
//    private var fullAddress by mutableStateOf("")
//
//    fun buildFullUrl(address: String, params: List<ApiParam>): String {
//        return URLBuilder(address).apply {
//            params.filter { it.enabled && it.key.isNotEmpty() }.forEach { param ->
//                parameters.append(param.key, param.value)
//            }
//        }.buildString()
//    }
//
//    fun praseUrl(url: String): Pair<String, List<Pair<String, String>>>? {
//        return try {
//            val builder = URLBuilder(url)
//            val newParams = builder.parameters.entries()
//                .flatMap { item -> item.value.map { it -> item.key to it } }
//            val newAddress = builder.apply { parameters.clear() }.buildString()
//            newAddress to newParams
//        } catch (e: Exception) {
//            null // 格式错误时不影响现有状态
//        }
//    }
//
//    init {
//        fullAddress = buildFullUrl(initial, initialParams)
//    }
//}

@Composable
fun ApiRequestPage() {
    val apiViewModel = LocalApiViewModel.current
    val ui = apiViewModel.ui.collectAsState().value

//    var fullUrl by remember { mutableStateOf("") }
//
//    LaunchedEffect(ui) {
//        if (ui != ApiStateHolder.BLANK) {
//            fullUrl = URLBuilder(ui.address).apply {
//                ui.params.toList().filter { it.enabled && it.key.isNotEmpty() }
//                    .forEach { param ->
//                        parameters.append(param.key, param.value)
//                    }
//            }.buildString()
//        }
//    }

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
                    ui.label.CxTextButton {
                        editLabel.setTrue()
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (ui.isModified) "Save".CxTextButton {
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
            ui.method.value.CxTextButton {
                expanded = true
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
            OutlinedTextField(
                value = ui.address,
                placeholder = { "Address".CxText() },
                onValueChange = {
                    ui.address = it
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) { }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        apiViewModel.request()
                    }
                ),
                maxLines = 3,
                textStyle = LocalTextStyle.current.copy(
                    platformStyle = PlatformTextStyle(
                        spanStyle = null,
                        paragraphStyle = null
                    ),
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            "Send".CxOutlinedButton {
                apiViewModel.request()
            }
        }
        FullSizeColumn {
            "Request".CxText()
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
                            requestPartEntities[pageId].CxText()
                            "Not Implemented".CxText()
                        }
                    }
                }
            )
            "Response".CxText()
            FullSizeColumn(modifier = Modifier.verticalScroll(rememberScrollState())) {

                TabBarWithContent(
                    modifier = Modifier.fillMaxWidth(),
                    responsePartEntities
                ) { pageId ->
                    ui.requestResult?.let { reqResult ->
                        reqResult.response?.let { result ->
                            when (pageId) {
                                0 -> {
                                    "Overview".CxText()
                                    "Status: ${result.code}".CxText()
                                    "Requested at: ${result.reqTime}".CxText()
                                    "Responded at: ${result.respTime}".CxText()
                                }

                                1 -> {
                                    "Requested at: ${result.reqTime}".CxText()
                                    "Responded at: ${result.respTime}".CxText()
                                    result.body.CxText()
                                }

                                2 -> {}
                                3 -> {
                                    result.headers.forEach { (key, values) ->
                                        values.forEach { value ->
                                            FullWidthRow(modifier = Modifier.clickable {}) {
                                                key.CxText(modifier = Modifier.weight(0.4f))
                                                value.CxText(modifier = Modifier.weight(1f))
                                            }
                                            HorizontalDivider()
                                        }
                                    }
                                }
                            }
                        } ?: FullSizeColumn {
                            "Error: ${reqResult.error}".CxText()
                        }
                    }
                }
            }
        }
    }
}



