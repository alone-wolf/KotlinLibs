package top.writerpass.klogger.cmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.klogger.KLogger
import top.writerpass.klogger.Log
import top.writerpass.kmplibrary.datetime.DateTimeUtils.formatUnixMs
import java.time.format.DateTimeFormatter

@Composable
fun KLoggerManager.KLoggerWindows(
    applicationScope: ApplicationScope
) {
    applicationScope.apply {
        loggersMap.forEach { (key, value) ->
            var showWindow by remember { mutableStateOf(true) }
            val logs = remember { mutableStateListOf<Log>() }
            val state = rememberLazyListState()
            LaunchedEffectOdd {
                logs.addAll(value.logList)
                value.logFlow.collect {
                    logs.add(it)
                    state.animateScrollToItem(logs.size)
                }
            }
            Window(
                onCloseRequest = { showWindow = false },
                state = rememberWindowState(
                    placement = WindowPlacement.Floating,
                    isMinimized = false,
                    position = WindowPosition.Aligned(Alignment.TopEnd),
                    size = DpSize(800.dp, 600.dp)
                ),
                visible = showWindow,
                title = "KLogger - $key",
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                        state = state
                    ) {
                        items(
                            items = logs,
                            key = { it -> it.hashCode() },
                            itemContent = {
                                "${it.timestamp.formatUnixMs()} - ${it.level} - ${it.tag} - ${it.message}".Text()
                            }
                        )
                    }
                }
            )
        }
    }
}

fun main() = application {
    KLoggerManager.KLoggerWindows(this)
    LaunchedEffectOdd {
        launch {
            repeat(100) {
                delay(700)
                KLogger.default.debug("WH_", "debug - $it")
            }
        }
    }
    LaunchedEffectOdd {
        launch {
            KLoggerManager.default.logFlow.collect {
                println(it)
            }
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            placement = WindowPlacement.Floating,
            isMinimized = false,
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(800.dp, 600.dp)
        ),
        visible = true,
        title = "KLoggerCMP",
        content = {
        }
    )
}