package top.writerpass.kotlinlibrary.shell.platform_tools

class AdbDeviceCommands(
    override val args: MutableList<String> = mutableListOf()
) : Commands(args = args) {
    // get-state
    fun getState(): Commands = apply {
        args.add("get-state")
    }

    // reboot
    fun reboot(): Commands = apply {
        args.add("reboot")
    }

    // reboot-bootloader
    fun rebootBootloader(): Commands = apply {
        args.add("reboot")
        args.add("bootloader")
    }

    // reboot-recovery
    fun rebootRecovery(): Commands = apply {
        args.add("reboot")
        args.add("recovery")
    }

    // reboot-sideload
    fun rebootSideload(): Commands = apply {
        args.add("reboot")
        args.add("sideload")
    }

    // reboot-sideload-auto-reboot
    fun rebootSideloadAutoReboot(): Commands = apply {
        args.add("reboot")
        args.add("sideload-auto-reboot")
    }

    // push
    fun push(source: String,destination: String): Commands = apply {
        args.add("push")
        args.add(source)
        args.add(destination)
    }

    // pull
    fun pull(source: String,destination: String): Commands = apply {
        args.add("pull")
        args.add(source)
        args.add(destination)
    }

    // shell
    fun shell(vararg shellArgs: String): Commands = apply {
        args.add("shell")
        args.addAll(shellArgs)
    }

    // install
    fun install(vararg installArgs: String): Commands = apply {
        args.add("install")
        args.addAll(installArgs)
    }
}