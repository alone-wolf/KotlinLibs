package top.writerpass.jiebajvm.resources

import java.io.File

actual fun getExternalStorageDirectory(): File {
    return File("~")
}