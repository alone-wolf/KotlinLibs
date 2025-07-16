package top.writerpass.jiebajvm.resources

import top.writerpass.kmplibrary.utils.println
import java.io.InputStream

actual fun getInputStream(path: String): InputStream {
    assert(assetManager!=null)
    assetManager!!.list("jieba")?.forEach { it.println() }
    return assetManager!!.open(path)
}