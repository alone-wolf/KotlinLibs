package top.writerpass.kotlinlibrary.file.utils

import java.io.File

val String.namedFile: File
    get() = File(this)

fun File.child(name: String): File {
    return File(this, name)
}

fun File.children(vararg name: String): File {
    var new = this
    name.forEach {
        new = new.child(it)
    }
    return new
}