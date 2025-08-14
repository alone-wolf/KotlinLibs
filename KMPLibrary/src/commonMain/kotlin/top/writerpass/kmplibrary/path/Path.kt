package top.writerpass.kmplibrary.path

interface PathStrategy {
    // 返回该平台的路径分隔符
    val separator: Char

    // 判断路径是否为绝对路径
    fun isAbsolute(path: String): Boolean

    // 对路径进行规范化，去除多余的`/`或`\`，以及`..`等
    fun normalize(path: String): String

    // 将路径分割成多个部分，返回一个路径的组件列表
    fun split(path: String): List<String>

    // 将多个路径组件合并成一个路径
    fun join(parts: List<String>): String
}

class WindowsPathStrategy : PathStrategy {
    override val separator: Char = '\\'

    override fun isAbsolute(path: String): Boolean {
        // 判断是否以盘符开头，比如C:\ 或 \\server\share
        return path.matches(Regex("[A-Za-z]:\\\\.*")) || path.startsWith("\\\\")
    }

    override fun normalize(path: String): String {
        // 规范化路径，替换多个反斜杠为一个，并处理 `..` 等
        return path.replace("\\\\", "\\").replace("/\\.", "").replace("\\..", "")
    }

    override fun split(path: String): List<String> {
        // 分割路径
        return path.split(separator)
    }

    override fun join(parts: List<String>): String {
        // 用反斜杠连接路径组件
        return parts.joinToString(separator.toString())
    }
}

class UnixPathStrategy : PathStrategy {
    override val separator: Char = '/'

    override fun isAbsolute(path: String): Boolean {
        // 判断是否以 `/` 开头
        return path.startsWith("/")
    }

    override fun normalize(path: String): String {
        // 规范化路径，替换多个斜杠为一个，并处理 `..` 等
        return path.replace("/\\.", "").replace("/..", "").replace("//", "/")
    }

    override fun split(path: String): List<String> {
        // 分割路径
        return path.split(separator)
    }

    override fun join(parts: List<String>): String {
        // 用正斜杠连接路径组件
        return parts.joinToString(separator.toString())
    }
}


enum class PathStyle {
    WINDOWS,
    UNIX,
}


class KPath private constructor(
    private val rawPath: String,
    private val style: PathStyle,
    private val strategy: PathStrategy = when (style) {
        PathStyle.WINDOWS -> WindowsPathStrategy()
        PathStyle.UNIX -> UnixPathStrategy()
    }
) {


    val separator: Char = strategy.separator

    // 静态方法创建 KPath 实例
    companion object {
        fun of(path: String, style: PathStyle): KPath {
            return KPath(path, style)
        }

        fun of(parts: List<String>, style: PathStyle): KPath {
            return KPath(strategyJoin(parts, style), style)
        }

        private fun strategyJoin(parts: List<String>, style: PathStyle): String {
            val strategy = when (style) {
                PathStyle.WINDOWS -> WindowsPathStrategy()
                PathStyle.UNIX -> UnixPathStrategy()
                else -> throw IllegalArgumentException("Unsupported PathStyle: $style")
            }
            return strategy.join(parts)
        }
    }

    // 获取路径的父路径
    val parent: KPath?
        get() {
            val parts = strategy.split(rawPath)
            return if (parts.size > 1) {
                KPath.of(parts.dropLast(1), style)
            } else {
                null
            }
        }

    // 获取路径的根路径
    val root: KPath?
        get() {
            val parts = strategy.split(rawPath)
            return if (parts.isNotEmpty()) KPath.of(parts.first(), style) else null
        }

    // 获取路径中的文件名（最后一部分）
    val name: String
        get() = rawPath.substringAfterLast(separator)

    // 判断是否为绝对路径
    val isAbsolute: Boolean
        get() = strategy.isAbsolute(rawPath)

    // 规范化路径
    fun normalize(): KPath {
        val normalizedPath = strategy.normalize(rawPath)
        return KPath(normalizedPath, style)
    }

    // 将当前路径与另一个路径合并
    fun resolve(other: String): KPath {
        val resolvedPath = if (isAbsolute) rawPath else "$rawPath$separator$other"
        return KPath(resolvedPath, style)
    }

    // 转换为指定风格的路径
    fun toStyle(style: PathStyle): KPath {
        val newPath = when (style) {
            PathStyle.WINDOWS -> WindowsPathStrategy().normalize(rawPath)
            PathStyle.UNIX -> UnixPathStrategy().normalize(rawPath)
            else -> rawPath
        }
        return KPath(newPath, style)
    }

    // 返回路径字符串
    override fun toString(): String = rawPath

    // 比较两个路径是否相等
    override fun equals(other: Any?): Boolean {
        return other is KPath && rawPath == other.rawPath
    }

    override fun hashCode(): Int {
        return rawPath.hashCode()
    }
}

