//package top.writerpass.rekuester.tables
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScope
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.TextRange
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.style.TextOverflow
//import top.writerpass.cmplibrary.LaunchedEffectOdd
//import top.writerpass.cmplibrary.utils.Mutable
//import top.writerpass.cmplibrary.utils.Mutable.setFalse
//import top.writerpass.cmplibrary.utils.Mutable.setTrue
//import top.writerpass.rekuester.tables.v7.Table7
//import top.writerpass.rekuester.tables.v7.dataSet
//import top.writerpass.rekuester.tables.v7.default
//
//@Composable
//inline fun <reified T : Any> DataTable(
//    modifier: Modifier = Modifier,
//    rowCount: Int,
//    columnCount: Int,
//    noinline onItem: (rowId: Int, columnId: Int) -> T,
//    noinline onItemContent: @Composable BoxScope.(rowId: Int, columnId: Int, isHeader: Boolean, item: T) -> Unit,
//) {
//    Table7(
//        modifier = modifier,
//        rowCount = rowCount,
//        columnCount = columnCount,
//        onItem = onItem,
//        onItemContent = onItemContent
//    )
//}
//
//@Composable
//inline fun <reified T : Any> DataTableImmutable(
//    modifier: Modifier = Modifier,
//    rowCount: Int,
//    columnCount: Int,
//    noinline onItem: (rowId: Int, columnId: Int) -> T,
//) {
//    Table7(
//        modifier = modifier,
//        rowCount = rowCount,
//        columnCount = columnCount,
//        onItem = onItem,
//        onItemContent = { rowId, columnId, isHeader, item ->
//            val itemValue = remember { item.toString() }
//            Box(
//                modifier = Modifier.fillMaxSize().then(
//                    if (isHeader) {
//                        Modifier.align(Alignment.Center)
//                    } else {
//                        Modifier
//                    }
//                )
//            ) {
//                Text(
//                    text = itemValue,
//                    maxLines = 1,
//                    overflow = TextOverflow.Clip,
//                    modifier = Modifier.then(
//                        if (isHeader) Modifier.align(Alignment.Center) else Modifier
//                    )
//                )
//            }
//        }
//    )
//}
//
//@Composable
//inline fun <reified T : Any> DataTableEditable(
//    modifier: Modifier = Modifier,
//    rowCount: Int,
//    columnCount: Int,
//    noinline onItem: (rowId: Int, columnId: Int) -> T,
//    noinline onItemChange: (rowId: Int, columnId: Int, itemString: String) -> Unit
//) {
//    Table7(
//        modifier = modifier,
//        rowCount = rowCount,
//        columnCount = columnCount,
//        onItem = onItem,
//        onItemContent = { rowId, columnId, isHeader, item ->
//            val itemValue: String = remember { item.toString() }
//            if (isHeader){
//                Text(
//                    text = itemValue,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }else{
//                val textFieldValue = Mutable.something(
//                    TextFieldValue(
//                        itemValue,
//                        TextRange(itemValue.length)
//                    )
//                )
//                val focusRequester = remember { FocusRequester() }
//
//                LaunchedEffectOdd { focusRequester.requestFocus() }
//
//                BasicTextField(
//                    value = textFieldValue.value,
//                    onValueChange = {
//                        textFieldValue.value = it
//                        onItemChange(rowId,columnId,it.text)
//                    },
//                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester),
//                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
//                    keyboardActions = KeyboardActions(
//                        onGo = { }
//                    ),
//                    singleLine = true,
//                )
//            }
//
//        }
//    )
//}