package top.writerpass.kotlinlibrary.shell.platform_tools

class DeviceCommandBuilderFactory(
    private val baseCommand: String,
    private val deviceSerial: String?
) {
    private fun getArgs(): MutableList<String> {
        val s = mutableListOf(baseCommand)
        if (deviceSerial != null) {
            s.add("-s")
            s.add(deviceSerial)
        }
        return s
    }

    fun newCommandBuilder(): AdbDeviceCommandBuilder {
        return AdbDeviceCommandBuilder(getArgs())
    }
}