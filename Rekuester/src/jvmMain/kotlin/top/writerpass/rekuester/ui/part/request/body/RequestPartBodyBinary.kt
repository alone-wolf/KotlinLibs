package top.writerpass.rekuester.ui.part.request.body

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import top.writerpass.cmplibrary.compose.ables.Composables
import top.writerpass.cmplibrary.compose.ables.StateComposables
import top.writerpass.kmplibrary.file.friendlySize
import top.writerpass.kmplibrary.utils.ifNotNull
import java.io.File

@Composable
fun RequestPartBodyBinary() {
    // file selector
    Composables.Scope {
        StateComposables.Scope {
            var selectedFile: File? by remember { mutableStateOf(null) }
            Row {
                "Pick a File".FilePicker {
                    selectedFile = it?.platformFile as? File
                }
                if (selectedFile != null) {
                    Icons.Default.Clear.IconButton { selectedFile = null }
                }
            }
            selectedFile.ifNotNull { it ->
                val filename by remember(it) {
                    derivedStateOf { it.name ?: "??unknown??" }
                }
                val size by remember(it) {
                    derivedStateOf { it.friendlySize() }
                }
                filename.Text()
                size.Text()
            }
        }
    }
}


