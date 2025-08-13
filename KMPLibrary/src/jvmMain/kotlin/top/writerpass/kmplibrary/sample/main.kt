package top.writerpass.kmplibrary.sample

import top.writerpass.kmplibrary.path.KPath
import top.writerpass.kmplibrary.path.PathStyle

fun main() {
    // 创建 Windows 路径对象
    val windowsPath = KPath.of("C:\\Users\\Admin\\file.txt", PathStyle.WINDOWS)
    println("Windows Path: $windowsPath")

    // 创建 Unix 路径对象
    val unixPath = KPath.of("/home/user/file.txt", PathStyle.UNIX)
    println("Unix Path: $unixPath")

    // 自动识别操作系统类型
    val autoPath = KPath.of("/usr/local/bin", PathStyle.AUTO)
    println("Auto Path (Unix): $autoPath")

    // 规范化路径
    val normalizedPath = KPath.of("/usr//bin/../local/./file.txt", PathStyle.UNIX).normalize()
    println("Normalized Path: $normalizedPath")

    // 路径拼接
    val resolvedPath = windowsPath.resolve("Desktop\\newfile.txt")
    println("Resolved Path: $resolvedPath")

    // 获取路径的父路径
    val parentPath = unixPath.parent
    println("Parent Path: $parentPath")

    // 获取路径的根路径
    val rootPath = windowsPath.root
    println("Root Path: $rootPath")

    // 转换路径风格
    val convertedPath = windowsPath.toStyle(PathStyle.UNIX)
    println("Converted Path (Unix): $convertedPath")
}
