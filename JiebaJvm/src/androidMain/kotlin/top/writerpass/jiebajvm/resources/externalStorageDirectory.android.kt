package top.writerpass.jiebajvm.resources

import android.os.Environment
import java.io.File

actual fun getExternalStorageDirectory(): File {
    return Environment.getExternalStorageDirectory()
}