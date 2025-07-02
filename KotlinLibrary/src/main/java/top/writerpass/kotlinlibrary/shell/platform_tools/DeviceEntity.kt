package top.writerpass.kotlinlibrary.shell.platform_tools

class DeviceEntity(val deviceSerial: String) {
    val adb: DeviceCommandBuilderFactory
        get() = DeviceCommandBuilderFactory("adb", deviceSerial)
    val fastboot: DeviceCommandBuilderFactory
        get() = DeviceCommandBuilderFactory("fastboot", deviceSerial)
}