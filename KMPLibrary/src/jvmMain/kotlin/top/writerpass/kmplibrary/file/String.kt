package top.writerpass.kmplibrary.file

import java.io.File

val String.file: File
    get() = File(this)

fun File.child(name: String): File {
    return File(this, name)
}