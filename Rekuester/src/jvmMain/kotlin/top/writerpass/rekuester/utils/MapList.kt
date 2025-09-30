package top.writerpass.rekuester.utils

fun <K, V> Map<K, List<V>>.flatToList(): List<Pair<K, V>> {
    return this.flatMap { (k, v) -> v.map { Pair(k, it) } }
}

fun <K, V> List<Pair<K, V>>.pairListToMap(): Map<K, List<V>> =
    groupBy({ it.first }, { it.second })

