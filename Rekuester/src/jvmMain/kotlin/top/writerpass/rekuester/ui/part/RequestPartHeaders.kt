package top.writerpass.rekuester.ui.part

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.OutlinedBasicTextField
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.ApiHeader
import top.writerpass.rekuester.ApiState

@Composable
fun RequestPartHeaders(apiState: ApiState) {
    "Headers".Text()

//    HeaderTableSheet(
//        rowCount = apiState.headers.list.size,
//        columnCount = 3,
//        headers = listOf("Key", "Value", "Description"),
//        onItem = { rowId, columnId ->
//            val row = apiState.headers.list[rowId]
//            when (columnId) {
//                0 -> row.key
//                1 -> row.value
//                2 -> row.description
//                else -> "--"
//            }
//        },
//        onItemChange = { rowId, columnId, item ->
//            val apiHeader = apiState.headers.list[rowId]
//            val newApiHeader = when(columnId){
//                0-> apiHeader.copy(key = item)
//                1-> apiHeader.copy(value = item)
//                2-> apiHeader.copy(description = item)
//                else -> apiHeader
//            }
//            apiState.headers.list[rowId] = newApiHeader
//        }
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
                apiState.headers.add(
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