package top.writerpass.kotlinlibrary.shell.platform_tools

class DeviceCommandBuilderFactory(
    private val baseCommand: String,
    private val deviceSerial: String?
) {
    private fun getArgs(): MutableList<String> {
        val s = mutableListOf<String>()
        s.add(baseCommand)
        if (deviceSerial != null) {
            s.add("-s")
            s.add(deviceSerial)
        }
        return s
    }

    fun newCommandBuilder(): AdbDeviceCommands {
        return AdbDeviceCommands(getArgs())
    }
}

