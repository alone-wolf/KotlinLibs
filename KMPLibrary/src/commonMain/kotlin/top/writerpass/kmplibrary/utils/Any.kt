package top.writerpass.kmplibrary.utils

inline fun <T : Any> T?.switch(
    isNull: () -> Unit = {},
    isNotNull: (T) -> Unit
) = this?.let { isNotNull(it) } ?: isNull()

inline fun <T : Any> T?.ifNull(block: () -> Unit) {
    if (this == null) block()
}

inline fun <T : Any> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) block(this)
}