package top.writerpass.kmplibrary.utils

fun String.println() {
    println(this)
}

fun String.log(tag: String) {
    println(tag, this)
}

fun Any.println() {
    toString().println()
}

fun println(vararg args: String) {
    kotlin.io.println(args.joinToString(" "))
}

fun println(vararg args: Any) {
    kotlin.io.println(args.joinToString(" "))
}