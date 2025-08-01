package top.writerpass.kotlinlibrary.shell.command

import java.io.File

operator fun ShellCommand.plus(other: ShellCommand): ShellCommand = this.then(other)
operator fun ShellCommand.times(other: ShellCommand): ShellCommand = this.pipe(other)
operator fun ShellCommand.rem(other: ShellCommand): ShellCommand = this.and(other)
operator fun ShellCommand.div(other: ShellCommand): ShellCommand = this.or(other)

infix fun ShellCommand.pipe(other: ShellCommand): ShellCommand = this.pipe(other)
infix fun ShellCommand.and(other: ShellCommand): ShellCommand = this.and(other)
infix fun ShellCommand.or(other: ShellCommand): ShellCommand = this.or(other)
infix fun ShellCommand.then(other: ShellCommand): ShellCommand = this.then(other)

infix fun ShellCommand.redirectTo(file: File): ShellCommand = this.redirectTo(file)
infix fun ShellCommand.redirectTo(file: String): ShellCommand = this.redirectTo(File(file))
infix fun ShellCommand.appendTo(file: File): ShellCommand = this.redirectTo(file, true)
infix fun ShellCommand.appendTo(file: String): ShellCommand = this.redirectTo(File(file), true)
infix fun ShellCommand.redirectFrom(file: File): ShellCommand = this.redirectFrom(file)
infix fun ShellCommand.redirectFrom(file: String): ShellCommand = this.redirectFrom(File(file))
infix fun ShellCommand.redirectErrorTo(file: File): ShellCommand = this.redirectErrorTo(file)
infix fun ShellCommand.redirectErrorTo(file: String): ShellCommand = this.redirectErrorTo(File(file))
infix fun ShellCommand.appendErrorTo(file: File): ShellCommand = this.redirectErrorTo(file, true)
infix fun ShellCommand.appendErrorTo(file: String): ShellCommand = this.redirectErrorTo(File(file), true)

fun ls(vararg options: String): ShellCommand = SingleCommand("ls", *options)
fun pwd(): ShellCommand = SingleCommand("pwd")
fun cd(path: String? = null): ShellCommand = SingleCommand("cd", *path?.let { arrayOf(it) } ?: emptyArray())
fun cat(vararg files: String): ShellCommand = SingleCommand("cat", *files)
fun echo(vararg text: String): ShellCommand = SingleCommand("echo", *text)
fun grep(pattern: String, vararg files: String): ShellCommand = SingleCommand("grep", pattern, *files)
fun find(path: String, vararg options: String): ShellCommand = SingleCommand("find", path, *options)
fun mkdir(path: String, recursive: Boolean = false): ShellCommand {
    val cmd = SingleCommand("mkdir")
    return if (recursive) cmd.option("-p") else cmd
}
fun rm(path: String, recursive: Boolean = false, force: Boolean = false): ShellCommand {
    val cmd = SingleCommand("rm")
    if (recursive) cmd.option("-r")
    if (force) cmd.option("-f")
    return cmd
}
fun cp(source: String, destination: String, recursive: Boolean = false): ShellCommand {
    val cmd = SingleCommand("cp")
    if (recursive) cmd.option("-r")
    return cmd
}
fun mv(source: String, destination: String): ShellCommand = SingleCommand("mv", source, destination)
fun chmod(permissions: String, path: String): ShellCommand = SingleCommand("chmod", permissions, path)
fun chown(owner: String, path: String): ShellCommand = SingleCommand("chown", owner, path)
fun curl(url: String, vararg options: String): ShellCommand = SingleCommand("curl", url, *options)
fun wget(url: String, vararg options: String): ShellCommand = SingleCommand("wget", url, *options)
fun tar(archive: String, vararg options: String): ShellCommand = SingleCommand("tar", archive, *options)
fun gzip(file: String): ShellCommand = SingleCommand("gzip", file)
fun gunzip(file: String): ShellCommand = SingleCommand("gunzip", file)
fun ssh(host: String, command: String? = null): ShellCommand {
    val args = mutableListOf(host)
    command?.let { args.add(it) }
    return SingleCommand("ssh", *args.toTypedArray())
}
fun scp(source: String, destination: String, vararg options: String): ShellCommand = SingleCommand("scp", source, destination, *options)
fun git(vararg args: String): ShellCommand = SingleCommand("git", *args)
fun docker(vararg args: String): ShellCommand = SingleCommand("docker", *args)
fun kubectl(vararg args: String): ShellCommand = SingleCommand("kubectl", *args)
fun java(vararg args: String): ShellCommand = SingleCommand("java", *args)
fun javac(vararg args: String): ShellCommand = SingleCommand("javac", *args)
fun kotlin(vararg args: String): ShellCommand = SingleCommand("kotlin", *args)
fun gradle(vararg args: String): ShellCommand = SingleCommand("./gradlew", *args)
fun npm(vararg args: String): ShellCommand = SingleCommand("npm", *args)
fun yarn(vararg args: String): ShellCommand = SingleCommand("yarn", *args)
fun python(vararg args: String): ShellCommand = SingleCommand("python3", *args)
fun pip(vararg args: String): ShellCommand = SingleCommand("pip3", *args)

fun ShellCommand.withOptions(vararg options: String): ShellCommand {
    return if (this is SingleCommand) {
        SingleCommand(this.build().split(" ").first(), *this.build().split(" ").drop(1).toTypedArray())
    } else {
        this
    }
}

fun ShellCommand.withFlag(flag: String): ShellCommand {
    return if (this is SingleCommand) {
        SingleCommand(this.build().split(" ").first(), *this.build().split(" ").drop(1).toTypedArray())
    } else {
        this
    }
}

fun String.run(): ShellResult = this.asCommand().execute()
fun ShellCommand.run(): ShellResult = this.execute()
fun String.runAsync(): Process = ProcessBuilder("/bin/sh", "-c", this).start()
fun ShellCommand.runAsync(): Process = this.build().runAsync()

fun List<ShellCommand>.aggregate(): ShellCommand {
    return this.reduce { acc, cmd -> acc.then(cmd) }
}

fun Iterable<String>.toCommandList(): List<ShellCommand> {
    return this.map { it.asCommand() }
}

fun ShellCommand.silent(): ShellCommand {
    return this.redirectTo(File("/dev/null"))
}

fun ShellCommand.logOutput(logFile: String): ShellCommand {
    return this.redirectTo(File(logFile))
}

fun ShellCommand.logError(errorFile: String): ShellCommand {
    return this.redirectErrorTo(File(errorFile))
}

fun ShellCommand.tee(outputFile: String): ShellCommand {
    return this.pipe(SingleCommand("tee", outputFile))
}

fun ShellCommand.background(): ShellCommand {
    return SingleCommand("(", this.build(), ")&")
}

fun ShellCommand.timeout(seconds: Int): ShellCommand {
    return SingleCommand("timeout", seconds.toString(), this.build())
}

fun ShellCommand.retry(maxRetries: Int = 3): ShellCommand {
    val command = this.build()
    val retryScript = """
        n=0
        until [ \n -ge $maxRetries ]
        do
            $command && break
            n=\$((n+1))
            sleep 1
        done
    """.trimIndent()
    return SingleCommand("/bin/sh", "-c", retryScript)
}

fun ShellCommand.withWorkingDirectory(dir: String): ShellCommand {
    val command = this.build()
    return SingleCommand("cd", dir, "&&", command)
}

fun ShellCommand.env(vararg envVars: Pair<String, String>): ShellCommand {
    val envString = envVars.joinToString(" ") { (key, value) -> "$key=$value" }
    val command = this.build()
    return SingleCommand("env", envString, command)
}

fun ShellCommand.sudo(): ShellCommand {
    val command = this.build()
    return SingleCommand("sudo", command)
}

fun ShellCommand.parallel(vararg others: ShellCommand): ShellCommand {
    val commands = listOf(this, *others).map { it.build() }
    val parallelScript = commands.joinToString(" & ") + " && wait"
    return SingleCommand("/bin/sh", "-c", parallelScript)
}

fun ShellCommand.sequential(vararg others: ShellCommand): ShellCommand {
    val commands = listOf(this, *others)
    return commands.reduce { acc, cmd -> acc.then(cmd) }
}

fun ShellCommand.ifSuccess(block: () -> ShellCommand): ShellCommand {
    return this.and(block())
}

fun ShellCommand.ifFailure(block: () -> ShellCommand): ShellCommand {
    return this.or(block())
}

fun ShellCommand.always(block: () -> ShellCommand): ShellCommand {
    return this.then(block())
}

fun ShellCommand.toString(): String = this.build()

val ShellCommand.exitCode: Int get() = this.execute().exitCode
val ShellCommand.output: String get() = this.execute().stdout
val ShellCommand.error: String get() = this.execute().stderr
val ShellCommand.success: Boolean get() = this.execute().isSuccess