package top.writerpass.kmplibrary.utils

inline fun <reified T> T.singleItemList(): List<T> = listOf(this)

fun <T : Any> List<T>.getPrevious(item: T): T? {
    return when (val index = indexOf(item)) {
        -1 -> {
            null
        }

        0 -> {
            null
        }

        else -> {
            get(index - 1)
        }
    }
}

fun <T> MutableList<T>.slidingWindowInsert(item: T): MutableList<T> {
    removeFirst()
    add(item)
    return this
}

fun <T : Any> List<T>.clearItemsBeforeActions(): List<T> {
    return toMutableList()
        .apply { clear() }
        .toList()
}

operator fun <T> List<T>.times(int: Int): List<T> {
    val m = mutableListOf<T>()
    repeat(int) {
        m.addAll(this)
    }
    return m.toList()
}