package top.writerpass.kotlinlibrary.shell

class DeviceEntity(val deviceSerial: String) {
    val adb: DeviceCommandBuilderFactory
        get() = DeviceCommandBuilderFactory("adb", deviceSerial)
    val fastboot: DeviceCommandBuilderFactory
        get() = DeviceCommandBuilderFactory("fastboot", deviceSerial)
}

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

    fun newCommandBuilder(): AdbCommandBuilder {
        return AdbCommandBuilder(getArgs())
    }
}

object GlobalCommandBuilderFactory {
    val adb: CommandBuilder
        get() = CommandBuilder(mutableListOf("adb"))
    val fastboot: CommandBuilder
        get() = CommandBuilder(mutableListOf("fastboot"))
}

class AdbCommandBuilder(
    private val args: MutableList<String> = mutableListOf()
) : CommandBuilder(args = args) {
    // get-state
    fun getState(): CommandBuilder = apply {
        args.add("get-state")
    }

    // reboot
    fun reboot(): CommandBuilder = apply {
        args.add("reboot")
    }

    // reboot-bootloader
    fun rebootBootloader(): CommandBuilder = apply {
        args.add("reboot")
        args.add("bootloader")
    }

    // reboot-recovery
    fun rebootRecovery(): CommandBuilder = apply {
        args.add("reboot")
        args.add("recovery")
    }

    // reboot-sideload
    fun rebootSideload(): CommandBuilder = apply {
        args.add("reboot")
        args.add("sideload")
    }

    // reboot-sideload-auto-reboot
    fun rebootSideloadAutoReboot(): CommandBuilder = apply {
        args.add("reboot")
        args.add("sideload-auto-reboot")
    }

    // push
    fun push(source: String,destination: String): CommandBuilder = apply {
        args.add("push")
        args.add(source)
        args.add(destination)
    }

    // pull
    fun pull(source: String,destination: String): CommandBuilder = apply {
        args.add("pull")
        args.add(source)
        args.add(destination)
    }

    // shell
    fun shell(vararg shellArgs: String): CommandBuilder = apply {
        args.add("shell")
        args.addAll(shellArgs)
    }

    // install
    fun install(vararg installArgs: String): CommandBuilder = apply {
        args.add("install")
        args.addAll(installArgs)
    }
}

class FastbootCommandBuilder(
    private val args: MutableList<String> = mutableListOf()
) : CommandBuilder(args = args) {

}

open class CommandBuilder(
    private val args: MutableList<String> = mutableListOf()
) {

    fun build(): List<String> {
        return args
    }

    override fun toString(): String = build().joinToString(" ")
}

fun main() {
    DeviceEntity("AAA")
        .adb
        .newCommandBuilder().let { builder ->
            builder.getState()
        }
}
