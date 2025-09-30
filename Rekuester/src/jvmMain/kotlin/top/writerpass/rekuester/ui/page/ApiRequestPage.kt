package top.writerpass.rekuester.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import io.ktor.http.HttpMethod
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.OutlinedTextFiled
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiStateHolder.rememberApiState
import top.writerpass.rekuester.LocalNavController
import top.writerpass.rekuester.Pages
import top.writerpass.rekuester.ui.componment.TabBarWithContent
import top.writerpass.rekuester.viewmodel.ApiRequestViewModel

private val requestPartEntities = listOf(
    "Param",
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
fun ApiRequestPage(
    navBackStackEntry: NavBackStackEntry,
) {
    val navController = LocalNavController.current
    val uuid = navBackStackEntry.toRoute<Pages.ApiRequestPage>().uuid

    val apiRequestViewModel = viewModel(
        viewModelStoreOwner = navBackStackEntry,
        key = uuid
    ) { ApiRequestViewModel(uuid) }

    val apiNullable by apiRequestViewModel.api.collectAsState()
    apiNullable?.let { api ->
        FullSizeColumn(modifier = Modifier) {
            val apiState = rememberApiState(api)

            FullWidthRow(verticalAlignment = Alignment.CenterVertically) {
                Icons.Default.KeyboardArrowLeft.IconButton {
                    navController.popBackStack()
                }
                var editLabel by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (editLabel) {
                        apiState.label.OutlinedTextFiled()
                        Icons.Default.Check.IconButton { editLabel = false }
                    } else {
                        apiState.label.value.Text()
                        Icons.Default.Edit.IconButton { editLabel = true }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (apiState.isModified.value) "Save".TextButton {
                    apiState.composeNewApi().let {
                        apiRequestViewModel.updateOrInsert(it)
                        apiState.isModified.setFalse()
                        editLabel = false
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
                apiState.address.OutlinedTextFiled(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                "Send".OutlinedButton { apiRequestViewModel.request() }
            }
            FullSizeColumn {
                "Request".Text()
                TabBarWithContent(
                    entities = requestPartEntities,
                    onPage = { pageId ->
                        when (pageId) {
                            0 -> {
                                requestPartEntities[pageId].Text()

                                apiState.params.list.forEachIndexed { index, (k, v, d) ->
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
                                        )
                                        OutlinedTextField(
                                            value = vv.value,
                                            placeholder = { "Value".Text() },
                                            onValueChange = { vv.value = it },
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = dd.value,
                                            placeholder = { "Description".Text() },
                                            onValueChange = { dd.value = it },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icons.Default.Delete.IconButton {
                                            apiState.params.removeAt(index)
                                        }
                                        Icons.Default.Save.IconButton {
                                            apiState.params.list[index] =
                                                ApiParam(
                                                    key = kk.value,
                                                    value = vv.value,
                                                    description = dd.value
                                                )
                                            kk.value = ""
                                            vv.value = ""
                                            dd.value = ""
                                        }
                                    }
                                }
                                FullWidthRow(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val k = Mutable.someString()
                                    val v = Mutable.someString()
                                    val d = Mutable.someString()
                                    OutlinedTextField(
                                        value = k.value,
                                        placeholder = { "Key".Text() },
                                        onValueChange = { k.value = it },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = v.value,
                                        placeholder = { "Value".Text() },
                                        onValueChange = { v.value = it },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = d.value,
                                        placeholder = { "Description".Text() },
                                        onValueChange = { d.value = it },
                                        modifier = Modifier.weight(1f)
                                    )
                                    "Save".OutlinedButton(modifier = Modifier) {
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

                            1 -> {
                                requestPartEntities[pageId].Text()
                                "Not Implemented".Text()
                            }

                            else -> {
                                requestPartEntities[pageId].Text()
                                "Not Implemented".Text()
                            }
                        }
                    }
                )
                "Response".Text()
                FullSizeColumn(modifier = Modifier.verticalScroll(rememberScrollState())) {

                    TabBarWithContent(responsePartEntities) { pageId ->
                        apiRequestViewModel.currentResult?.let { reqResult ->
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
                                                Divider()
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

}