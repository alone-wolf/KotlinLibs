package top.writerpass.kmplibrary.utils

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