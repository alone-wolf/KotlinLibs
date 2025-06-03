package top.writerpass.kotlinlibrary.utils

inline fun <T : Any> T?.switch(
    isNull: () -> Unit = {},
    isNotNull: (T) -> Unit
) = this?.let { isNotNull(it) } ?: isNull()