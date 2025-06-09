package top.writerpass.kmplibrary.utils


fun <K, V> MutableMap<K, V>.getOrCreate(k: K, block: () -> V): V {
    return if (contains(k)) {
        get(k)!!
    } else {
        val v = block()
        set(k, v)
        v
    }
}

fun MutableMap<String, MutableList<String>>.autoAppend(
    k: String, v: String
) {
    val list = getOrCreate(k) { mutableListOf() }
    list.add(v)
}