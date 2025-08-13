package top.writerpass.kmplibrary.path

enum class PathStyle(val separator: String) {
    WINDOWS("\\"),
    LINUX("/")
}

class Path(private val path: String, private val style: PathStyle) {

    /** 当前风格的路径分隔符 */
    val separator: String get() = style.separator

    /** 将路径标准化为当前风格分隔符 */
    private fun normalize(path: String): String {
        val fixed = path
            .replace("\\", "/") // 先统一为 /
            .split("/")
            .filter { it.isNotEmpty() && it != "." }
            .fold(mutableListOf<String>()) { acc, part ->
                if (part == ".." && acc.isNotEmpty() && acc.last() != "..") {
                    acc.removeLast()
                } else {
                    acc.add(part)
                }
                acc
            }
            .joinToString("/")

        return if (style == PathStyle.WINDOWS) {
            fixed.replace("/", "\\")
        } else {
            if (fixed.startsWith("/")) fixed else "/$fixed"
        }
    }

    fun normalize(): Path {
        return Path(normalize(path), style)
    }

    /** 拼接路径（自动添加分隔符） */
    fun resolve(base: String, child: String): String {
        val baseNorm = normalize(base)
        val childNorm = normalize(child)
        return normalize(
            if (isAbsolute(childNorm)) childNorm
            else baseNorm.trimEnd(separator[0]) + separator + childNorm.trimStart(separator[0])
        )
    }

    /** 是否绝对路径（兼容 Windows 和 Linux） */
    fun isAbsolute(path: String): Boolean {
        return when (style) {
            PathStyle.LINUX -> path.startsWith("/")
            PathStyle.WINDOWS -> path.matches(Regex("^[A-Za-z]:[\\\\/].*"))
        }
    }

    /** 获取文件名 */
//    fun getName(path: String): String = Path(normalize(path)).name
//
//    /** 获取文件名（不带扩展名） */
//    fun getNameWithoutExtension(path: String): String = Path(normalize(path)).nameWithoutExtension
//
//    /** 获取文件扩展名 */
//    fun getExtension(path: String): String = Path(normalize(path)).extension
//
//    /** 获取父目录 */
//    fun getParent(path: String): String? =
//        Path(normalize(path)).parent?.invariantSeparatorsPathString
}
