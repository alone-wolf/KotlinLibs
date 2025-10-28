package top.writerpass.rekuester.ui.part.request.body

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import top.writerpass.cmplibrary.compose.IconButton
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.TextButton
import top.writerpass.kmplibrary.file.friendlySize
import java.io.File

@Composable
fun RequestPartBodyBinary() {
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