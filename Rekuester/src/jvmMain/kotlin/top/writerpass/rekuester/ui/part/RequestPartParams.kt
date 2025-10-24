package top.writerpass.rekuester.ui.part

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import top.writerpass.cmplibrary.compose.BasicTextField
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiState
import top.writerpass.rekuester.tables.v12.CommonTableFrame
import top.writerpass.rekuester.tables.v12.TableAxisIds
import top.writerpass.rekuester.tables.v12.TableState
import top.writerpass.rekuester.tables.v12.TableStrategies

@Composable
fun RequestPartParams(apiState: ApiState) {
    "Params".Text()

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

    val updateItemEnable: (rowId: Int, newApiParam: ApiParam) -> Unit = remember {
        { rowId, newApiParam ->
            apiState.params.list[rowId] = newApiParam
        }
    }

    val e = Mutable.someBoolean()
    val k = Mutable.someString("")
    val v = Mutable.someString("")
    val d = Mutable.someString("")

    CommonTableFrame(
        modifier = Modifier.border(
            width = 2.dp,
            color = Color.Green,
            shape = RoundedCornerShape(4.dp)
        ),
        listState = rememberLazyListState(),
        tableState = remember {
            TableState(
                strategies = TableStrategies(
                    horizontal = TableStrategies.Size.FillContainer,
                    vertical = TableStrategies.Size.FillContainer,
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
        },
        dataRowCount = apiState.params.list.size,
        dataColumnCount = 3,
        headerItemContent = { columnId ->
            val headers = remember { listOf("Key", "Value", "Description") }
            val header = remember { headers[columnId] }
            header.Text(modifier = Modifier.align(Alignment.Center))
        },
        footerItemContent = { columnId ->
            when (columnId) {
                0 -> k.BasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
                1 -> v.BasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
                2 -> d.BasicTextField(modifier = Modifier.fillMaxSize(), maxLines = 1)
            }
        },
        leadingItemContent = { rowId ->
            when (rowId) {
                TableAxisIds.HeaderRowId -> {
                    Checkbox(false, {}, modifier = Modifier.align(Alignment.Center))
                }

                TableAxisIds.FooterRowId -> {
                    Checkbox(e.value, { e.value = it }, modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    val item = remember { apiState.params.list[rowId] }
                    var enabled by Mutable.someBoolean(item.enabled)
                    Checkbox(enabled, {
                        enabled = it
                        val newApiParam = item.copy(enabled = enabled)
                        updateItemEnable(rowId,newApiParam)
                    }, modifier = Modifier.align(Alignment.Center))
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
                        Icons.Default.Clear.Icon(modifier = Modifier.clickable {})
                        Icons.Default.Add.Icon(modifier = Modifier.clickable {})
                    }
                }

                else -> {
                    Row {
                        Icons.Default.Delete.Icon()
                        Icons.Default.Add.Icon()
                    }
                }
            }
        },
        dataItemContent = { rowId, columnId ->
            val density = LocalDensity.current
            val item = remember(rowId, columnId) {
                when (columnId) {
                    0 -> "$rowId - 000"
                    1 -> "$rowId - 111"
                    2 -> "$rowId - 222"
                    else -> "$rowId - ---"
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
                                onItemChange(rowId, columnId, it.text)
                            },
                            modifier = Modifier.fillMaxHeight()
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

//    FullWidthRow(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        val k = Mutable.someString()
//        val v = Mutable.someString()
//        val d = Mutable.someString()
//        k.OutlinedBasicTextField(
//            placeholder = "Key",
//            modifier = Modifier.weight(1f)
//        )
//        v.OutlinedBasicTextField(
//            placeholder = "Value",
//            modifier = Modifier.weight(1f)
//        )
//        d.OutlinedBasicTextField(
//            placeholder = "Description",
//            modifier = Modifier.weight(1f)
//        )
//        Icons.Default.Save.IconButton {
//            if (k.value.isNotBlank()) {
//
//                apiState.params.list.add(
//                    ApiParam(
//                        k.value,
//                        v.value,
//                        d.value
//                    )
//                )
//                k.value = ""
//                v.value = ""
//                d.value = ""
//            }
//        }
//    }
}