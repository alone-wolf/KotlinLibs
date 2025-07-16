package top.writerpass.inputeventdispatcher

data class InputEventLog(
    val timestamp: Long,
    val eventType: EventType,
    val action: String,
    val keyCode: Int? = null,
    val keyLabel: String? = null,
    val motionInfo: MotionInfo? = null,
    val deviceInfo: DeviceInfo
) {
    enum class EventType {
        KEY_EVENT, MOTION_EVENT, UNKNOWN
    }

    data class MotionInfo(
        val x: Float?,
        val y: Float?,
        val axis: Map<String, Float>? = null
    )

    data class DeviceInfo(
        val id: Int,
        val name: String,
        val vendorId: Int,
        val productId: Int,
        val isVirtual: Boolean,
        val sources: List<String>
    )
}