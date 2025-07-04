package top.writerpass.kotlinlibrary.shell.platform_tools

class AdbDeviceCommandBuilder(
    override val args: MutableList<String> = mutableListOf()
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