package top.writerpass.inputeventdispatcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.inputeventdispatcher.ui.theme.InputEventDispatcherTheme


// --- 提取工具函数 ---
fun KeyEvent.toEventLog(): InputEventLog {
    val device = this.device
    return InputEventLog(
        timestamp = System.currentTimeMillis(),
        eventType = InputEventLog.EventType.KEY_EVENT,
        action = when (this.action) {
            KeyEvent.ACTION_DOWN -> "DOWN"
            KeyEvent.ACTION_UP -> "UP"
            else -> "UNKNOWN"
        },
        keyCode = this.keyCode,
        keyLabel = KeyEvent.keyCodeToString(this.keyCode),
        deviceInfo = extractDeviceInfo(device)
    )
}

fun MotionEvent.toEventLog(): InputEventLog {
    val device = this.device
    val axisMap = mutableMapOf<String, Float>()

    listOf(
        MotionEvent.AXIS_X to "X",
        MotionEvent.AXIS_Y to "Y",
        MotionEvent.AXIS_HSCROLL to "HSCROLL",
        MotionEvent.AXIS_VSCROLL to "VSCROLL",
        MotionEvent.AXIS_Z to "Z",
        MotionEvent.AXIS_RX to "RX",
        MotionEvent.AXIS_RY to "RY"
    ).forEach { (axis, name) ->
        val value = getAxisValue(axis)
        if (value != 0f) axisMap[name] = value
    }

    return InputEventLog(
        timestamp = System.currentTimeMillis(),
        eventType = InputEventLog.EventType.MOTION_EVENT,
        action = MotionEvent.actionToString(this.action),
        motionInfo = InputEventLog.MotionInfo(
            x = this.x,
            y = this.y,
            axis = axisMap.takeIf { it.isNotEmpty() }
        ),
        deviceInfo = extractDeviceInfo(device)
    )
}

fun extractDeviceInfo(device: InputDevice?): InputEventLog.DeviceInfo {
    if (device == null) {
        return InputEventLog.DeviceInfo(
            id = -1,
            name = "Unknown",
            vendorId = 0,
            productId = 0,
            isVirtual = true,
            sources = listOf("None")
        )
    }

    val sourceList = buildList {
        val s = device.sources
        if (s and InputDevice.SOURCE_KEYBOARD == InputDevice.SOURCE_KEYBOARD) add("Keyboard")
        if (s and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) add("Gamepad")
        if (s and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) add("Joystick")
        if (s and InputDevice.SOURCE_TOUCHSCREEN == InputDevice.SOURCE_TOUCHSCREEN) add("Touchscreen")
        if (s and InputDevice.SOURCE_MOUSE == InputDevice.SOURCE_MOUSE) add("Mouse")
        if (s and InputDevice.SOURCE_DPAD == InputDevice.SOURCE_DPAD) add("D-Pad")
        if (s and InputDevice.SOURCE_TOUCHPAD == InputDevice.SOURCE_TOUCHPAD) add("Touchpad")
        if (s and InputDevice.SOURCE_STYLUS == InputDevice.SOURCE_STYLUS) add("Stylus")
    }

    return InputEventLog.DeviceInfo(
        id = device.id,
        name = device.name,
        vendorId = device.vendorId,
        productId = device.productId,
        isVirtual = device.isVirtual,
        sources = sourceList
    )
}

// --- 可读文本 ---
fun InputEventLog.toDisplayText(): String = buildString {
    appendLine("[$eventType][$action] ${keyLabel ?: ""}")
    motionInfo?.let {
        appendLine("Position: (${it.x}, ${it.y})")
        it.axis?.forEach { (axis, value) ->
            appendLine("Axis $axis: $value")
        }
    }
    appendLine("Device: ${deviceInfo.name}")
    appendLine("Type: ${deviceInfo.sources.joinToString()}")
}

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<InputEventLogViewModel>()

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val log = event.toEventLog()
        viewModel.log(log)
        return super.dispatchKeyEvent(event)
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val log = event.toEventLog()
        viewModel.log(log)
        return super.onGenericMotionEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InputEventDispatcherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FullSizeBox(modifier = Modifier.padding(innerPadding)) {
                        val logs by viewModel.logs.collectAsStateWithLifecycle()
                        val latestLog by remember {
                            derivedStateOf {
                                logs.lastOrNull()
                            }
                        }
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            latestLog?.let {
                                OutlinedCard(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
//                                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    it.toDisplayText().Text(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            } ?: "Please Press Any Button".Text()
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.4f)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .align(Alignment.BottomCenter),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = logs,
                                key = null,
                                itemContent = {
                                    OutlinedCard(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .fillMaxWidth()
//                                            .padding(8.dp)
                                        ,
                                        colors = CardDefaults.cardColors(
//                                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        it.toDisplayText().Text()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}