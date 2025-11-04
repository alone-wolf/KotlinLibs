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
import top.writerpass.cmplibrary.compose.ables.CxFilePicker
import top.writerpass.cmplibrary.compose.ables.CxIconButton
import top.writerpass.cmplibrary.compose.ables.CxText
import top.writerpass.kmplibrary.file.friendlySize
import top.writerpass.kmplibrary.utils.ifNotNull
import java.io.File

@Composable
fun RequestPartBodyBinary() {
    // file selector
    var selectedFile: File? by remember { mutableStateOf(null) }
    Row {
        "Pick a File".CxFilePicker {
            selectedFile = it?.file
        }
        if (selectedFile != null) {
            Icons.Default.Clear.CxIconButton { selectedFile = null }
        }
    }
    selectedFile.ifNotNull { it ->
        val filename by remember(it) {
            derivedStateOf { it.name ?: "??unknown??" }
        }
        val size by remember(it) {
            derivedStateOf { it.friendlySize() }
        }
        filename.CxText()
        size.CxText()
    }
}


