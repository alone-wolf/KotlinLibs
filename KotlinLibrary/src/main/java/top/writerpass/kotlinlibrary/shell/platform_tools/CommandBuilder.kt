package top.writerpass.kotlinlibrary.shell.platform_tools

abstract class CommandBuilder(
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
