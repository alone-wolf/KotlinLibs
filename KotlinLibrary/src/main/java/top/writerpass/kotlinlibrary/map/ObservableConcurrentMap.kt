package top.writerpass.kotlinlibrary.map

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ConcurrentHashMap

class ObservableConcurrentMap<Key : Any, Value : Any>(initialCapacity: Int) {
    private val delegate = ConcurrentHashMap<Key, Value>(initialCapacity)
    private val _flow = MutableSharedFlow<Map<Key, Value>>(replay = 1)
    val flow = _flow.asSharedFlow()

    private suspend fun emitUpdate() {
        _flow.emit(HashMap(delegate))
    }

//    /**
//     * Computes [block] and inserts result in map. The [block] will be evaluated at most once.
//     *
//     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.util.collections.ConcurrentMap.computeIfAbsent)
//     */
//    fun computeIfAbsent(key: Key, block: () -> Value): Value = delegate.computeIfAbsent(key) {
//        block()
//    }

    val size: Int
        get() = delegate.size

    fun containsKey(key: Key): Boolean = delegate.containsKey(key)

    fun containsValue(value: Value): Boolean = delegate.containsValue(value)

    fun get(key: Key): Value? = delegate[key]

    fun isEmpty(): Boolean = delegate.isEmpty()

    val entries: MutableSet<MutableMap.MutableEntry<Key, Value>>
        get() = delegate.entries

    val keys: MutableSet<Key>
        get() = delegate.keys

    val values: MutableCollection<Value>
        get() = delegate.values

    suspend fun clear() {
        delegate.clear().also { emitUpdate() }
    }

    suspend operator fun set(key: Key, value: Value) = put(key, value)

    suspend fun put(key: Key, value: Value): Value? {
        return delegate.put(key, value).also { emitUpdate() }
    }

    suspend fun putAll(from: Map<out Key, Value>) {
        delegate.putAll(from).also { emitUpdate() }
    }

    suspend fun remove(key: Key): Value? {
        return delegate.remove(key).also { emitUpdate() }
    }

    suspend fun remove(key: Key, value: Value): Boolean {
        return delegate.remove(key, value).also { emitUpdate() }
    }

    override fun hashCode(): Int = delegate.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other !is Map<*, *>) return false
        return other == delegate
    }

    override fun toString(): String = "ConcurrentMapJvm by $delegate"

    suspend fun update(k: Key, block: (Value?) -> Value?) {
        val item = delegate[k]
        block(item)?.let {
            put(k, it)
        }
    }
}