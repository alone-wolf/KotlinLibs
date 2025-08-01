package top.writerpass.kotlinlibrary.shell.command

import java.io.File

interface ShellCommand {
    fun build(): String
    fun execute(): ShellResult

    fun pipe(next: ShellCommand): ShellCommand
    fun redirectTo(file: File, append: Boolean = false): ShellCommand
    fun redirectFrom(file: File): ShellCommand
    fun redirectErrorTo(file: File, append: Boolean = false): ShellCommand
    fun and(next: ShellCommand): ShellCommand
    fun or(next: ShellCommand): ShellCommand
    fun then(next: ShellCommand): ShellCommand
}

data class ShellResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String,
    val command: String
) {
    val isSuccess: Boolean get() = exitCode == 0
    val isFailure: Boolean get() = exitCode != 0
}

sealed class CommandOperator(val symbol: String) {
    object Pipe : CommandOperator("|")
    object And : CommandOperator("&&")
    object Or : CommandOperator("||")
    object Then : CommandOperator(";")
    data class RedirectOutput(val append: Boolean = false) :
        CommandOperator(if (append) ">>" else ">")

    data class RedirectInput(val file: File) : CommandOperator("<")
    data class RedirectError(val append: Boolean = false) :
        CommandOperator(if (append) "2>>" else "2>")
}

class ShellBuilder {
    private val commands = mutableListOf<Pair<ShellCommand, CommandOperator?>>()

    fun command(name: String, vararg args: String): ShellCommand = SingleCommand(name, *args)

    fun String.cmd(vararg args: String): ShellCommand = SingleCommand(this, *args)

    fun build(): String {
        return commands.joinToString(" ") { (cmd, op) ->
            when (op) {
                null -> cmd.build()
                else -> "${cmd.build()} ${op.symbol}"
            }
        }.trim()
    }

    fun execute(): ShellResult {
        return ShellExecutor.execute(build())
    }

    fun addCommand(command: ShellCommand, operator: CommandOperator? = null) {
        commands.add(command to operator)
    }
}

class SingleCommand(
    private val name: String,
    private vararg val args: String,
    private val options: MutableMap<String, String?> = mutableMapOf()
) : ShellCommand {

    override fun build(): String {
        val opts = options.entries.joinToString(" ") { (key, value) ->
            if (value == null) key else "$key $value"
        }
        val arguments = args.joinToString(" ")
        return listOf(name, opts, arguments).filter { it.isNotBlank() }.joinToString(" ")
    }

    override fun execute(): ShellResult {
        return ShellExecutor.execute(build())
    }

    override fun pipe(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Pipe)
    }

    override fun redirectTo(file: File, append: Boolean): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectOutput(append), file)
    }

    override fun redirectFrom(file: File): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectInput(file), file)
    }

    override fun redirectErrorTo(file: File, append: Boolean): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectError(append), file)
    }

    override fun and(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.And)
    }

    override fun or(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Or)
    }

    override fun then(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Then)
    }

    fun option(name: String, value: String? = null): SingleCommand = apply {
        options[name] = value
    }

    fun flag(name: String): SingleCommand = option(name)
    fun longOption(name: String, value: String? = null): SingleCommand = option("--$name", value)
    fun shortOption(name: String, value: String? = null): SingleCommand = option("-$name", value)
}

class CompositeCommand(
    private val first: ShellCommand,
    private val second: ShellCommand,
    private val operator: CommandOperator
) : ShellCommand {

    override fun build(): String {
        return "${first.build()} ${operator.symbol} ${second.build()}"
    }

    override fun execute(): ShellResult {
        return ShellExecutor.execute(build())
    }

    override fun pipe(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Pipe)
    }

    override fun redirectTo(file: File, append: Boolean): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectOutput(append), file)
    }

    override fun redirectFrom(file: File): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectInput(file), file)
    }

    override fun redirectErrorTo(file: File, append: Boolean): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectError(append), file)
    }

    override fun and(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.And)
    }

    override fun or(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Or)
    }

    override fun then(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Then)
    }
}

class RedirectedCommand(
    private val command: ShellCommand,
    private val operator: CommandOperator,
    private val file: File
) : ShellCommand {

    override fun build(): String {
        return when (operator) {
            is CommandOperator.RedirectOutput -> "${command.build()} ${operator.symbol} ${file.absolutePath}"
            is CommandOperator.RedirectInput -> "${command.build()} ${operator.symbol} ${file.absolutePath}"
            is CommandOperator.RedirectError -> "${command.build()} ${operator.symbol} ${file.absolutePath}"
            else -> command.build()
        }
    }

    override fun execute(): ShellResult {
        return ShellExecutor.execute(build())
    }

    override fun pipe(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Pipe)
    }

    override fun redirectTo(file: File, append: Boolean): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectOutput(append), file)
    }

    override fun redirectFrom(file: File): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectInput(file), file)
    }

    override fun redirectErrorTo(file: File, append: Boolean): ShellCommand {
        return RedirectedCommand(this, CommandOperator.RedirectError(append), file)
    }

    override fun and(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.And)
    }

    override fun or(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Or)
    }

    override fun then(next: ShellCommand): ShellCommand {
        return CompositeCommand(this, next, CommandOperator.Then)
    }
}

object ShellExecutor {
    fun execute(command: String): ShellResult {
        return try {
            val process = ProcessBuilder("/bin/sh", "-c", command)
                .redirectErrorStream(false)
                .start()

            val stdout = process.inputStream.bufferedReader().use { it.readText() }
            val stderr = process.errorStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            ShellResult(exitCode, stdout, stderr, command)
        } catch (e: Exception) {
            ShellResult(-1, "", e.message ?: "Unknown error", command)
        }
    }
}

fun shell(block: ShellBuilder.() -> ShellCommand): ShellResult {
    val builder = ShellBuilder()
    val command = builder.block()
    return command.execute()
}

fun String.asCommand(vararg args: String): ShellCommand = SingleCommand(this, *args)