package top.writerpass.rekuester.ui.part.request.params

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.ables.Icon
import top.writerpass.cmplibrary.compose.ables.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.tables.v12.CommonTableFrame
import top.writerpass.rekuester.tables.v12.TableAxisIds
import top.writerpass.rekuester.tables.v12.TableState
import top.writerpass.rekuester.tables.v12.TableStrategies


private val headers = listOf("Key", "Value", "Description")

private val tableState = TableState(
    strategies = TableStrategies(
        horizontal = TableStrategies.Size.FillContainer,
        vertical = TableStrategies.Size.WrapContent,
        defaultRow = TableStrategies.Axis.Fixed(40.dp),
        defaultColumn = TableStrategies.Axis.Fixed(120.dp)
    ),
    extras = {
        preSetColumnState(
            id = -1,
            strategy = TableStrategies.Axis.Fixed(30.dp)
        )
        preSetColumnState(
            id = 0,
            strategy = TableStrategies.Axis.Ranged(
                min = 100.dp,
                max = 300.dp,
                default = 120.dp
            )
        )
        preSetColumnState(
            id = 1,
            strategy = TableStrategies.Axis.Ranged(
                min = 100.dp,
                max = 300.dp,
                default = 120.dp
            )
        )
        preSetColumnState(
            id = 2,
            strategy = TableStrategies.Axis.Ranged(
                min = 100.dp,
                max = 300.dp,
                default = 120.dp
            )
        )
    }
)

@Composable
fun RequestPartParams() {
    val apiViewModel = LocalApiViewModel.current
    val ui by apiViewModel.ui.collectAsState()

    CommonTableFrame(
        modifier = Modifier.border(
            width = 2.dp,
            color = Color.Green,
            shape = RoundedCornerShape(4.dp)
        ),
        listState = rememberLazyListState(),
        tableState = tableState,
        dataRowCount = ui.params.size,
        dataColumnCount = 3,
        headerItemContent = { columnId ->
            val header = remember { headers[columnId] }
            header.Text(modifier = Modifier.align(Alignment.Center))
        },
        footerItemContent = { columnId ->
            when (columnId) {
                0 -> BasicTextField(
                    value = apiViewModel.newParamKey,
                    onValueChange = { apiViewModel.newParamKey = it },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = 1
                )

                1 -> BasicTextField(
                    value = apiViewModel.newParamValue,
                    onValueChange = { apiViewModel.newParamValue = it },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = 1
                )

                2 -> BasicTextField(
                    value = apiViewModel.newParamDescription,
                    onValueChange = { apiViewModel.newParamDescription = it },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = 1
                )
            }
        },
        leadingItemContent = { rowId ->
            when (rowId) {
                TableAxisIds.HeaderRowId -> {
                    val allEnabled by remember(ui.params.asReadOnly()) {
                        derivedStateOf {
                            ui.params.asReadOnly().all { it.enabled }
                        }
                    }
                    Checkbox(
                        checked = allEnabled,
                        onCheckedChange = { enabled ->
                            ui.params.asReadOnly()
                                .map { apiParam -> apiParam.copy(enabled = enabled) }
                                .forEachIndexed { index, param ->
                                    ui.params[index] = param
                                }
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                TableAxisIds.FooterRowId -> {
                    Checkbox(
                        checked = apiViewModel.newParamEnabled,
                        onCheckedChange = { apiViewModel.newParamEnabled = it },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    val item by remember(rowId) {
                        derivedStateOf {
                            ui.params[rowId]
                        }
                    }
                    Checkbox(
                        checked = item.enabled,
                        onCheckedChange = {
                            val newApiParam = item.copy(enabled = item.enabled.not())
                            apiViewModel.updateApiParam(rowId, newApiParam)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        tailItemContent = { rowId ->
            when (rowId) {
                TableAxisIds.HeaderRowId -> {
                    "Actions".Text(modifier = Modifier.align(Alignment.Center))
                }

                TableAxisIds.FooterRowId -> {
                    Row {
                        Icons.Default.Clear.Icon(
                            modifier = Modifier.clickable {
                                apiViewModel.clearNewParam()
                            }
                        )
                        Icons.Default.Add.Icon(
                            modifier = Modifier.clickable {
                                if (apiViewModel.saveNewApiParam()) {
                                    apiViewModel.clearNewParam()
                                }
                            }
                        )
                    }
                }

                else -> {
                    Row {
                        Icons.Default.Delete.Icon(
                            modifier = Modifier.clickable {
                                apiViewModel.deleteApiParam(rowId)
                            }
                        )
                    }
                }
            }
        },
        dataItemContent = { rowId, columnId ->
            val density = LocalDensity.current
            val item1 = remember { ui.params[rowId] }
            val item = remember {
                when (columnId) {
                    0 -> item1.key
                    1 -> item1.value
                    2 -> item1.description
                    else -> "------"
                }
            }
            val isEditing = Mutable.someBoolean()
            val textFieldValue = Mutable.something(
                default = TextFieldValue(
                    text = item,
                    selection = TextRange(item.length)
                )
            )
            var lineHeightSp by remember { mutableStateOf(TextUnit.Unspecified) }

            isEditing.When(
                isTrue = {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffectOdd { focusRequester.requestFocus() }
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = textFieldValue.value,
                            onValueChange = {
                                textFieldValue.value = it
                                val apiParam = ui.params[rowId]
                                val newApiParam = when (columnId) {
                                    0 -> apiParam.copy(key = item)
                                    1 -> apiParam.copy(value = item)
                                    2 -> apiParam.copy(description = item)
                                    else -> apiParam
                                }
                                apiViewModel.updateApiParam(rowId, newApiParam)
                            },
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
                            keyboardActions = KeyboardActions(
                                onGo = { isEditing.setFalse() }
                            ),
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(
                                lineHeight = lineHeightSp,
                                fontSize = 14.sp
                            ),
                        )
                        Icons.Default.Check.Icon(modifier = Modifier.size(16.dp).clickable {
                            isEditing.setFalse()
                        })
                    }
                },
                isFalse = {
                    Text(
                        text = textFieldValue.value.text,
                        modifier = Modifier.fillMaxSize().clickable { isEditing.setTrue() },
                        onTextLayout = { textLayoutResult ->
                            // 获取文本高度（像素）
                            lineHeightSp =
                                with(density) { textLayoutResult.size.height.toFloat().toSp() }
                        },
                        lineHeight = lineHeightSp,
                        fontSize = 14.sp
                    )
                }
            )
        }
    )
}
