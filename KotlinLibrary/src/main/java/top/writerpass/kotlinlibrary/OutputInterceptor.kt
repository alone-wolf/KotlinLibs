package top.writerpass.kotlinlibrary

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.*

class OutputInterceptor(
    private val interceptStdOut: Boolean = true,
    private val interceptStdErr: Boolean = true,
    private val alsoPrintToConsole: Boolean = true,
    private val onStdOutLine: ((String) -> Unit)? = null,
    private val onStdErrLine: ((String) -> Unit)? = null,
) {
    private val originalOut = System.out
    private val originalErr = System.err

    private val outContent = PipedOutputStream()
    private val errContent = PipedOutputStream()

    private val stdoutReader = PipedInputStream(outContent)
    private val stderrReader = PipedInputStream(errContent)

    private val _capturedOut = mutableListOf<String>()
    private val _capturedErr = mutableListOf<String>()
    private val mutexOut = Mutex()
    private val mutexErr = Mutex()

    val capturedOut: List<String> get() = runBlocking { mutexOut.withLock { _capturedOut.toList() } }
    val capturedErr: List<String> get() = runBlocking { mutexErr.withLock { _capturedErr.toList() } }

    private var job: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun start() {
        if (job != null) return // already running

        job = scope.launch {
            val jobs = mutableListOf<Job>()
            if (interceptStdOut) {
                System.setOut(
                    PrintStream(
                        MultiOutputStream(
                            outContent,
                            if (alsoPrintToConsole) originalOut else null
                        ), true
                    )
                )
                jobs += launchReader(stdoutReader, _capturedOut, mutexOut, onStdOutLine)
            }
            if (interceptStdErr) {
                System.setErr(
                    PrintStream(
                        MultiOutputStream(
                            errContent,
                            if (alsoPrintToConsole) originalErr else null
                        ), true
                    )
                )
                jobs += launchReader(stderrReader, _capturedErr, mutexErr, onStdErrLine)
            }
            jobs.joinAll()
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        System.setOut(originalOut)
        System.setErr(originalErr)
        stdoutReader.close()
        stderrReader.close()
    }

    private fun launchReader(
        input: InputStream,
        target: MutableList<String>,
        mutex: Mutex,
        onLine: ((String) -> Unit)?
    ) = scope.launch {
        input.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                mutex.withLock { target.add(line) }
                onLine?.invoke(line)
            }
        }
    }

    private class MultiOutputStream(
        private val primary: OutputStream,
        private val secondary: OutputStream? = null
    ) : OutputStream() {
        override fun write(b: Int) {
            primary.write(b)
            secondary?.write(b)
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            primary.write(b, off, len)
            secondary?.write(b, off, len)
        }

        override fun flush() {
            primary.flush()
            secondary?.flush()
        }

        override fun close() {
            primary.close()
            secondary?.close()
        }
    }
}


//fun main() = runBlocking {
//
//    launch {
//        var index = 0
//        while (true) {
//            println("Hello from background ${index++}")
//            System.err.println("Oops! An error ${index}")
//            delay(10)
//        }
//    }
//
//    val interceptor = OutputInterceptor(
//        onStdOutLine = { println("Captured stdout: $it") },
//        onStdErrLine = { println("Captured stderr: $it") }
//    )
//    interceptor.start()
//
//    delay(10000) // 等待后台协程处理输出
//
//    interceptor.stop()
//
////    println("Captured stdout:")
////    interceptor.capturedOut.forEach(::println)
////
////    println("Captured stderr:")
////    interceptor.capturedErr.forEach(::println)
//}