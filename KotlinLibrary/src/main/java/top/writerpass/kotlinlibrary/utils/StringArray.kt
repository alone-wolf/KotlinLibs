package top.writerpass.kotlinlibrary.utils


fun Array<String>.execute(): Process {
    println(this.joinToString(" "))
    val runtime = Runtime.getRuntime()
    return runtime.exec(this)
}