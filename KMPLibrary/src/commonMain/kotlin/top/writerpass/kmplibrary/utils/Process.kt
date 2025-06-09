package top.writerpass.kmplibrary.utils

//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.io.BufferedWriter
//import java.io.InputStreamReader
//
//suspend fun Process.collect(): ShellTaskResult {
//    return withContext(Dispatchers.IO) {
//        val stdReader = InputStreamReader(inputStream)
//        val std = stdReader.readLines()
//        val errReader = InputStreamReader(errorStream)
//        val err = errReader.readLines()
//        val code = waitFor()
//        ShellTaskResult(std, err, code)
//    }
//}
//
//fun Process.collectAwait(
//    scope: CoroutineScope,
//    onStdOut: suspend (String) -> Unit,
//    onStdErr: suspend (String) -> Unit
//) {
//    scope.launchDefault {
//        inputReader().use { reader ->
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                line?.let { onStdOut(it) }
//            }
//        }
//    }
//    scope.launchDefault {
//        errorReader().use { reader ->
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                line?.let { onStdErr(it) }
//            }
//        }
//    }
//}
//
//fun Process.collectAndWrite(
//    scope: CoroutineScope,
//    onStdOut: suspend (String) -> Unit,
//    onStdErr: suspend (String) -> Unit
//): BufferedWriter {
//    scope.launchDefault {
//        inputReader().use { reader ->
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                line?.let { onStdOut(it) }
//            }
//        }
//    }
//    scope.launchDefault {
//        errorReader().use { reader ->
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                line?.let { onStdErr(it) }
//            }
//        }
//    }
//    return outputWriter()
//}