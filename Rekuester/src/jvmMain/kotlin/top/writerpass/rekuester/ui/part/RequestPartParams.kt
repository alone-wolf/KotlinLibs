package top.writerpass.rekuester.ui.part

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.OutlinedBasicTextField
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiState

@Composable
fun RequestPartParams(apiState: ApiState) {
    "Params".Text()
    val headers = remember { listOf("", "Key", "Value", "Description", "Actions") }

    val rowCount by remember(apiState.params.list) {
        derivedStateOf {
            // header +1
            // footer +1
            apiState.params.list.size + 2
        }
    }

    val onItemChange: (rowId: Int, columnId: Int, item: String) -> Unit = remember {
        { rowId, columnId, item ->
            val apiParam = apiState.params.list[rowId]
            val newApiParam = when (columnId) {
                0 -> apiParam.copy(key = item)
                1 -> apiParam.copy(value = item)
                2 -> apiParam.copy(description = item)
                else -> apiParam
            }
            apiState.params.list[rowId] = newApiParam
        }
    }

//    CommonTableFrame(
//        modifier = Modifier,
//        listState = rememberLazyListState(),
//        tableState = remember {
//            TableState(
//                defaultHeight = 25.dp,
//                tableWidthStrategy = TableWidthStrategy.FillContainer
//            )
//        },
//        dataRowCount = apiState.params.list.size + 2,
//        dataColumnCount = 4,
//        onItemContent = { rowId, columnId, rowType, columnType ->
////            when(rowType){
////                RowType.Header -> TODO()
////                RowType.Footer -> TODO()
////                RowType.Normal -> TODO()
////            }
//            val firstRow = remember { rowId == 0 }
//            val firstColumn = remember { columnId == 0 }
//            val lastRow by remember(apiState.params.list.lastIndex) {
//                derivedStateOf {
//                    rowId > apiState.params.list.lastIndex - 1
//                }
//            }
//            if (firstRow) {
//                if (firstColumn) {
//                    Checkbox(
//                        checked = false,
//                        onCheckedChange = {},
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                } else {
//                    val header = remember { headers[columnId] }
//                    header.Text(
//                        modifier = Modifier.align(Alignment.Center),
//                        maxLines = 1,
//                        overflow = TextOverflow.Clip
//                    )
//                }
//            } else {
//                if (lastRow) {
//                    "---".Text(
//                        modifier = Modifier.align(Alignment.Center),
//                        maxLines = 1,
//                        overflow = TextOverflow.Clip
//                    )
//                } else {
//                    if (firstColumn) {
//                        Checkbox(
//                            checked = false,
//                            onCheckedChange = {},
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                    } else {
//                        val row = remember { apiState.params.list[rowId] }
//                        val item = remember {
//                            when (columnId) {
//                                1 -> row.key
//                                2 -> row.value
//                                3 -> row.description
//                                else -> "--"
//                            }
//                        }
//                        val isEditing = Mutable.someBoolean()
//                        val textFieldValue = Mutable.something(
//                            default = TextFieldValue(
//                                text = item,
//                                selection = TextRange(item.length)
//                            )
//                        )
//                        val density = LocalDensity.current
//                        var lineHeightSp by remember { mutableStateOf(TextUnit.Unspecified) }
//
//
//                        isEditing.When(
//                            isTrue = {
//                                val focusRequester = remember { FocusRequester() }
//                                LaunchedEffectOdd { focusRequester.requestFocus() }
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    BasicTextField(
//                                        value = textFieldValue.value,
//                                        onValueChange = {
//                                            textFieldValue.value = it
//                                            onItemChange(rowId, columnId, it.text)
//                                        },
//                                        modifier = Modifier.fillMaxHeight().weight(1f)
//                                            .focusRequester(focusRequester),
//                                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
//                                        keyboardActions = KeyboardActions(
//                                            onGo = { isEditing.setFalse() }
//                                        ),
//                                        singleLine = true,
//                                        textStyle = TextStyle.Default.copy(
//                                            lineHeight = lineHeightSp,
//                                            fontSize = 14.sp
//                                        )
//                                    )
//                                    Icons.Default.Check.Icon(modifier = Modifier.size(16.dp).clickable {
//                                        isEditing.setFalse()
//                                    })
//                                }
//                            },
//                            isFalse = {
//                                Text(
//                                    text = textFieldValue.value.text,
//                                    modifier = Modifier.fillMaxSize().clickable { isEditing.setTrue() },
//                                    onTextLayout = { textLayoutResult ->
//                                        // 获取文本高度（像素）
//                                        lineHeightSp =
//                                            with(density) { textLayoutResult.size.height.toFloat().toSp() }
//                                    },
//                                    lineHeight = lineHeightSp,
//                                    fontSize = 14.sp
//                                )
//                            }
//                        )
//                    }
//                }
//            }
//        },
//    )

    FullWidthRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val k = Mutable.someString()
        val v = Mutable.someString()
        val d = Mutable.someString()
        k.OutlinedBasicTextField(
            placeholder = "Key",
            modifier = Modifier.weight(1f)
        )
        v.OutlinedBasicTextField(
            placeholder = "Value",
            modifier = Modifier.weight(1f)
        )
        d.OutlinedBasicTextField(
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