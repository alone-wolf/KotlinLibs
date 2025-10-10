package top.writerpass.rekuester.ui.part

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.OutlinedTextFiled1
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.ApiParam
import top.writerpass.rekuester.ApiState
import top.writerpass.rekuester.tables.v8.HeaderTableSheet

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