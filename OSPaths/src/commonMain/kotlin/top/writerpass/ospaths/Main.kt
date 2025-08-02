package top.writerpass.ospaths

fun main() {
    val downloadsPath = SystemPathResolver.resolve(SystemPaths.DOWNLOADS)
    println("Downloads path: ${downloadsPath.absolutePath}")

    val cachePath = SystemPathResolver.resolve(SystemPaths.CACHE)
    println("Cache path: ${cachePath.absolutePath}")
}
