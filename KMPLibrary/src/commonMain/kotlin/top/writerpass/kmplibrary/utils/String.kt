package top.writerpass.kmplibrary.utils

fun ByteArray.toHexString(): String {
    val hexChars = "0123456789abcdef"
    val result = StringBuilder(this.size * 2)

    for (byte in this) {
        val i = byte.toInt() and 0xFF
        result.append(hexChars[i ushr 4])
        result.append(hexChars[i and 0x0F])
    }

    return result.toString()
}