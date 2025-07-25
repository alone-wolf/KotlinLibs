package top.writerpass.kmplibrary.utils

inline fun <T : Any> T?.switch(
    isNull: () -> Unit = {},
    isNotNull: (T) -> Unit
) = this?.let { isNotNull(it) } ?: isNull()