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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.Icon
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.cmplibrary.utils.Mutable.When
import top.writerpass.cmplibrary.utils.Mutable.setFalse
import top.writerpass.cmplibrary.utils.Mutable.setTrue
import top.writerpass.kmplibrary.file.friendlySize
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.models.ApiFormData
import top.writerpass.rekuester.models.BodyTypes
import top.writerpass.rekuester.models.RawBodyTypes
import top.writerpass.rekuester.tables.v12.CommonTableFrame
import top.writerpass.rekuester.tables.v12.TableAxisIds
import top.writerpass.rekuester.tables.v12.TableState
import top.writerpass.rekuester.tables.v12.TableStrategies
import java.io.File

private val headers = listOf("Key", "Value", "Description")


@Composable
fun RequestPartBody() {
    val apiViewModel = LocalApiViewModel.current
    val ui by apiViewModel.ui.collectAsState()
    val bodyType by remember {
        derivedStateOf { ui.body.type }
    }
    FullWidthRow {
        var expanded by remember { mutableStateOf(false) }
        bodyType.label.TextButton {
            expanded = true
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = {
                BodyTypes.map.forEach { (t, u) ->
                    DropdownMenuItem(
                        text = { t.Text() },
                        onClick = {
                            ui.body = ui.body.copy(type = u)
                            expanded = false
                        }
                    )
                }
            }
        )
        FullWidthColumn {
            when (bodyType) {
                BodyTypes.None -> {
                    "None of Body".Text()
                }

                BodyTypes.FormData -> {
                    // form data table
                    val formDataList = remember { mutableStateListOf<ApiFormData>() }
                    val newE = Mutable.someBoolean()
                    val newK = Mutable.something("")
                    val newV = Mutable.something("")
                    val newD = Mutable.something("")
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
                        },
                        dataRowCount = formDataList.size,
                        dataColumnCount = 3,
                        headerItemContent = { columnId ->
                            val header = remember { headers[columnId] }
                            header.Text(modifier = Modifier.align(Alignment.Center))
                        },
                        footerItemContent = { columnId ->
                            // TODO change apiViewModel.newHeaderXXX to apiViewModel.newBodyXXX
                            when (columnId) {
                                0 -> BasicTextField(
                                    value = newK.value,
                                    onValueChange = { newK.value = it },
                                    modifier = Modifier.fillMaxSize(),
                                    maxLines = 1
                                )

                                1 -> BasicTextField(
                                    value = newV.value,
                                    onValueChange = { newV.value = it },
                                    modifier = Modifier.fillMaxSize(),
                                    maxLines = 1
                                )

                                2 -> BasicTextField(
                                    value = newD.value,
                                    onValueChange = { newD.value = it },
                                    modifier = Modifier.fillMaxSize(),
                                    maxLines = 1
                                )
                            }
                        },
                        leadingItemContent = { rowId ->
                            when (rowId) {
                                TableAxisIds.HeaderRowId -> {
//                                    val allEnabled by remember(apiState.headers.asReadOnly()) {
//                                        derivedStateOf {
//                                            apiState.headers.asReadOnly().all { it.enabled }
//                                        }
//                                    }
                                    val allEnabled = true
                                    Checkbox(
                                        checked = allEnabled,
                                        onCheckedChange = { enabled ->
//                                            apiState.headers.asReadOnly()
//                                                .map { apiHeader -> apiHeader.copy(enabled = enabled) }
//                                                .forEachIndexed { index, header ->
//                                                    apiState.headers[index] = header
//                                                }
                                        },
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }

                                TableAxisIds.FooterRowId -> {
                                    Checkbox(
                                        checked = newE.value,
                                        onCheckedChange = { newE.value = it },
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }

                                else -> {
                                    val item by remember(rowId) {
                                        derivedStateOf {
                                            formDataList[rowId]
                                        }
                                    }
                                    Checkbox(
                                        checked = item.enabled,
                                        onCheckedChange = {
                                            val newItem = item.copy(enabled = item.enabled.not())
                                            formDataList[rowId] = newItem
//                                            apiViewModel.updateApiHeader(rowId, newApiHeader)
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
//                                                apiViewModel.clearNewHeader()
                                            }
                                        )
                                        Icons.Default.Add.Icon(
                                            modifier = Modifier.clickable {
//                                                if (apiViewModel.saveNewApiHeader()) {
//                                                    apiViewModel.clearNewHeader()
//                                                }
                                            }
                                        )
                                    }
                                }

                                else -> {
                                    Row {
                                        Icons.Default.Delete.Icon(
                                            modifier = Modifier.clickable {
//                                                apiViewModel.deleteApiHeader(rowId)
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        dataItemContent = { rowId, columnId ->
                            val density = LocalDensity.current
//                            val item1 = remember { apiState.headers[rowId] }
                            val item = remember {
                                val item1 = formDataList[rowId]
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
                                                val apiHeader = formDataList[rowId]
                                                val newApiHeader = when (columnId) {
                                                    0 -> apiHeader.copy(key = item)
                                                    1 -> apiHeader.copy(value = item)
                                                    2 -> apiHeader.copy(description = item)
                                                    else -> apiHeader
                                                }
                                                formDataList[rowId] = newApiHeader
//                                                apiViewModel.updateApiHeader(rowId, newApiHeader)
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
                                        Icons.Default.Check.Icon(
                                            modifier = Modifier.size(16.dp).clickable {
                                                isEditing.setFalse()
                                            })
                                    }
                                },
                                isFalse = {
                                    androidx.compose.material3.Text(
                                        text = textFieldValue.value.text,
                                        modifier = Modifier.fillMaxSize()
                                            .clickable { isEditing.setTrue() },
                                        onTextLayout = { textLayoutResult ->
                                            // 获取文本高度（像素）
                                            lineHeightSp =
                                                with(density) {
                                                    textLayoutResult.size.height.toFloat().toSp()
                                                }
                                        },
                                        lineHeight = lineHeightSp,
                                        fontSize = 14.sp
                                    )
                                }
                            )
                        },
                    )
                }

                BodyTypes.FormUrlencoded -> {
                    // urlencoded data table
                    val formDataList = remember { mutableStateListOf<ApiFormData>() }
                    val newE = Mutable.someBoolean()
                    val newK = Mutable.something("")
                    val newV = Mutable.something("")
                    val newD = Mutable.something("")
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
                        },
                        dataRowCount = formDataList.size,
                        dataColumnCount = 3,
                        headerItemContent = { columnId ->
                            val header = remember { headers[columnId] }
                            header.Text(modifier = Modifier.align(Alignment.Center))
                        },
                        footerItemContent = { columnId ->
                            // TODO change apiViewModel.newHeaderXXX to apiViewModel.newBodyXXX
                            when (columnId) {
                                0 -> BasicTextField(
                                    value = newK.value,
                                    onValueChange = { newK.value = it },
                                    modifier = Modifier.fillMaxSize(),
                                    maxLines = 1
                                )

                                1 -> BasicTextField(
                                    value = newV.value,
                                    onValueChange = { newV.value = it },
                                    modifier = Modifier.fillMaxSize(),
                                    maxLines = 1
                                )

                                2 -> BasicTextField(
                                    value = newD.value,
                                    onValueChange = { newD.value = it },
                                    modifier = Modifier.fillMaxSize(),
                                    maxLines = 1
                                )
                            }
                        },
                        leadingItemContent = { rowId ->
                            when (rowId) {
                                TableAxisIds.HeaderRowId -> {
//                                    val allEnabled by remember(apiState.headers.asReadOnly()) {
//                                        derivedStateOf {
//                                            apiState.headers.asReadOnly().all { it.enabled }
//                                        }
//                                    }
                                    val allEnabled = true
                                    Checkbox(
                                        checked = allEnabled,
                                        onCheckedChange = { enabled ->
//                                            apiState.headers.asReadOnly()
//                                                .map { apiHeader -> apiHeader.copy(enabled = enabled) }
//                                                .forEachIndexed { index, header ->
//                                                    apiState.headers[index] = header
//                                                }
                                        },
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }

                                TableAxisIds.FooterRowId -> {
                                    Checkbox(
                                        checked = newE.value,
                                        onCheckedChange = { newE.value = it },
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }

                                else -> {
                                    val item by remember(rowId) {
                                        derivedStateOf {
                                            formDataList[rowId]
                                        }
                                    }
                                    Checkbox(
                                        checked = item.enabled,
                                        onCheckedChange = {
                                            val newItem = item.copy(enabled = item.enabled.not())
                                            formDataList[rowId] = newItem
//                                            apiViewModel.updateApiHeader(rowId, newApiHeader)
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
//                                                apiViewModel.clearNewHeader()
                                            }
                                        )
                                        Icons.Default.Add.Icon(
                                            modifier = Modifier.clickable {
//                                                if (apiViewModel.saveNewApiHeader()) {
//                                                    apiViewModel.clearNewHeader()
//                                                }
                                            }
                                        )
                                    }
                                }

                                else -> {
                                    Row {
                                        Icons.Default.Delete.Icon(
                                            modifier = Modifier.clickable {
//                                                apiViewModel.deleteApiHeader(rowId)
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        dataItemContent = { rowId, columnId ->
                            val density = LocalDensity.current
//                            val item1 = remember { apiState.headers[rowId] }
                            val item = remember {
                                val item1 = formDataList[rowId]
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
                                                val apiHeader = formDataList[rowId]
                                                val newApiHeader = when (columnId) {
                                                    0 -> apiHeader.copy(key = item)
                                                    1 -> apiHeader.copy(value = item)
                                                    2 -> apiHeader.copy(description = item)
                                                    else -> apiHeader
                                                }
                                                formDataList[rowId] = newApiHeader
//                                                apiViewModel.updateApiHeader(rowId, newApiHeader)
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
                                        Icons.Default.Check.Icon(
                                            modifier = Modifier.size(16.dp).clickable {
                                                isEditing.setFalse()
                                            })
                                    }
                                },
                                isFalse = {
                                    androidx.compose.material3.Text(
                                        text = textFieldValue.value.text,
                                        modifier = Modifier.fillMaxSize()
                                            .clickable { isEditing.setTrue() },
                                        onTextLayout = { textLayoutResult ->
                                            // 获取文本高度（像素）
                                            lineHeightSp =
                                                with(density) {
                                                    textLayoutResult.size.height.toFloat().toSp()
                                                }
                                        },
                                        lineHeight = lineHeightSp,
                                        fontSize = 14.sp
                                    )
                                }
                            )
                        },
                    )
                }

                BodyTypes.Binary -> {
                    // file selector
                    var showFilePicker by remember { mutableStateOf(false) }
                    var selectedFile: File? by remember { mutableStateOf(null) }
                    FilePicker(
                        show = showFilePicker,
                        fileExtensions = emptyList()
                    ) { platformFile ->
                        showFilePicker = false
                        selectedFile = platformFile?.platformFile as? File
                    }
                    Row {
                        "Select File".TextButton {
                            showFilePicker = true
                        }
                        if (selectedFile != null) {
                            Icons.Default.Clear.IconButton {
                                selectedFile = null
                            }
                        }
                    }
                    if (selectedFile != null) {
                        val filename by remember(selectedFile) {
                            derivedStateOf {
                                selectedFile?.name ?: "??unknown??"
                            }
                        }
                        val size by remember(selectedFile) {
                            derivedStateOf {
                                selectedFile?.friendlySize() ?: "-1B"
                            }
                        }
                        filename.Text()
                        size.Text()
                    }
                }

                BodyTypes.Raw -> {
                    // sub-selector
                    val rawType = Mutable.something<RawBodyTypes>(RawBodyTypes.Text)
                    rawType.DropDownMenu(
                        entities = RawBodyTypes.map,
                        any2String = { label },
                    )
                }

                BodyTypes.GraphQL -> {
                    // not implementation
                }
            }
        }
    }
}