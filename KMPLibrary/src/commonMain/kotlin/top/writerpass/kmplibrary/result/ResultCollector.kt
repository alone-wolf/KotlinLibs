package top.writerpass.kmplibrary.result

class ResultCollector<T> private constructor() {
    // 默认值为 {}，即使没有指定处理函数也不会报错
    var onSuccessCallback: suspend (T) -> Unit = {}
        private set

    // 同理
    var onFailedCallback: suspend (Throwable) -> Unit = {}
        private set

    // 用来设置结果的处理函数
    fun onSuccess(block: suspend (T) -> Unit) {
        onSuccessCallback = block
    }

    // 用来处理报错的处理函数
    fun onFailed(block: suspend (Throwable) -> Unit) {
        onFailedCallback = block
    }

    companion object {
        // 允许在逻辑执行阶段指定两个回调函数，允许回调函数访问执行过程中的一些中间数据，更加灵活
        suspend fun <T> collectWhich(block: suspend ResultCollector<T>.() -> T) {
            val r = ResultCollector<T>()
            try {
                val data = r.block()
                r.onSuccessCallback(data)
            } catch (e: Throwable) {
                r.onFailedCallback(e)
            }
        }
    }
}

//fun main() {
//    runBlocking {
//        ResultCollector.collectWhich {
//            val target = 0
//            onSuccess { it: Int? ->
//                it?.let { it.toString().println() }
//            }
//
//            onFailed { e: Throwable ->
//                "target:${target}".println()
//                e.printStackTrace()
//                e.message?.println()
//            }
//
//            if (Random.nextInt() % 2 == target) {
//                error("test exception")
//            } else {
//                1000
//            }
//        }
//    }
//}