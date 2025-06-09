package top.writerpass.kmplibrary.singleton

import kotlin.reflect.KClass

//typealias SingletonPair = Pair<String, Any>
//
//infix fun <T : Any> KClass<T>.pair(value: T): SingletonPair {
//    return this.java.name to value
//}
//
//infix fun <T : Any> String.pair(value: T): Pair<String, T> {
//    return this to value
//}
//
//
//
//object Singleton {
//    val store = ConcurrentHashMap<String, Any>()
//
//    fun set(vararg values: SingletonPair) {
//        values.forEach { v -> store[v.first] = v.second }
//    }
//
//    operator fun <T : Any> set(
//        modelClass: KClass<T>,
//        item: T
//    ) {
//        store[modelClass.java.name] = item
//    }
//
//    operator fun <T : Any> set(
//        key: String,
//        item: T
//    ) {
//        store[key] = item
//    }
//
//    inline fun <reified T : Any> inject(): T {
//        return store[T::class.java.name] as T
//    }
//
//    fun <T : Any> get(key: KClass<T>): T {
//        return store[key.java.name] as T
//    }
//
//    inline fun <reified T : Any> get(key: String): T {
//        return store[key] as T
//    }
//}